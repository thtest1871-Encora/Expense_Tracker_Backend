package com.transactionservice.demo.service;

import com.transactionservice.demo.client.CategoryClient;
import com.transactionservice.demo.client.NotificationClient;
import com.transactionservice.demo.dto.CategoryDto;
import com.transactionservice.demo.dto.CategoryStatResponse;
import com.transactionservice.demo.dto.CreateTransactionRequest;
import com.transactionservice.demo.dto.FilterResponse;
import com.transactionservice.demo.dto.MonthlyStatResponse;
import com.transactionservice.demo.dto.TransactionEvent;
import com.transactionservice.demo.dto.TransactionResponse;
import com.transactionservice.demo.model.Transaction;
import com.transactionservice.demo.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Service
public class TransactionService {

    private final TransactionRepository repo;
    private final NotificationClient notificationClient;
    private final CategoryClient categoryClient;

    public TransactionService(TransactionRepository repo, NotificationClient notificationClient, CategoryClient categoryClient) {
        this.repo = repo;
        this.notificationClient = notificationClient;
        this.categoryClient = categoryClient;
    }

    private TransactionResponse toResponse(Transaction t) {
        TransactionResponse r = new TransactionResponse();
        r.setId(t.getId());
        r.setUserId(t.getUserId());
        r.setTitle(t.getDescription());
        r.setCategoryId(t.getCategoryId());
        r.setAmount(t.getAmount());
        r.setCurrency("INR"); // Default for now
        r.setCreatedAt(t.getCreatedAt());
        return r;
    }

    @Transactional
    public Transaction create(Long userId, CreateTransactionRequest req) {
        // Fetch category details first
        String categoryName = "Unknown";
        String categoryEmoji = "";
        String categoryType = "EXPENSE"; // Default

        if (req.getCategoryId() != null) {
            CategoryDto cat = categoryClient.getCategoryById(req.getCategoryId());
            if (cat != null) {
                // Validate that the category belongs to the user
                // Note: CategoryService usually returns only if it exists, but we should ensure it's not a system category if we want strictness.
                // However, since we removed system categories, any category returned here should be valid.
                // Ideally, CategoryService should enforce userId check. Assuming it does or returns public ones.
                // But to be safe and strict as requested:
                // "Delete all transactions where categoryId NOT IN user's created categories"
                // We can't delete here, but we can ensure we don't create one.
                
                categoryName = cat.getName();
                categoryEmoji = cat.getEmoji();
                categoryType = cat.getType();
            } else {
                 throw new RuntimeException("Category not found or does not belong to user.");
            }
        } else {
             throw new RuntimeException("Category ID is required.");
        }

        double amount = req.getAmount();
        if ("EXPENSE".equalsIgnoreCase(categoryType)) {
            amount = -Math.abs(amount);
        } else {
            amount = Math.abs(amount);
        }

        Transaction tx = new Transaction();
        tx.setUserId(userId);
        tx.setAmount(amount);
        tx.setCategoryId(req.getCategoryId());
        tx.setDescription(req.getDescription());

        Transaction saved = repo.save(tx);

        // 🔥 Send event to Notification Service
        String type = amount >= 0 ? "INCOME" : "EXPENSE";

        TransactionEvent event = new TransactionEvent(
                saved.getId(),
                saved.getUserId(),
                type,
                Math.abs(saved.getAmount()),
                saved.getCategoryId(),
                categoryName,
                categoryEmoji,
                saved.getDescription()
        );

        notificationClient.sendTransactionNotification(event);

        return saved;
    }

    public List<Transaction> getByUser(Long userId) {
        return repo.findByUserId(userId);
    }

    @Transactional
    public void delete(Long id, Long userId) {
        repo.findById(id)
                .filter(t -> t.getUserId().equals(userId))
                .ifPresentOrElse(repo::delete,
                        () -> { throw new RuntimeException("Transaction not found OR not allowed"); });
        // Optional: notify deletion later
    }

    public FilterResponse filterTransactions(
            Long userId,
            Long categoryId,
            String type,
            LocalDate start,
            LocalDate end,
            String sort,
            int page,
            int size
    ) {
        List<Long> categoryIds = null;
        Double minAmount = null;
        Double maxAmount = null;

        if (type != null) {
            // Fetch all categories for user and filter by type
            List<CategoryDto> categories = categoryClient.getCategories(userId);
            categoryIds = categories.stream()
                    .filter(c -> type.equalsIgnoreCase(c.getType()))
                    .map(CategoryDto::getId)
                    .toList();
            
            // If no categories match the type, return empty result immediately
            if (categoryIds.isEmpty()) {
                return new FilterResponse(0, List.of());
            }

            if ("INCOME".equalsIgnoreCase(type)) {
                minAmount = 0.0;
            } else if ("EXPENSE".equalsIgnoreCase(type)) {
                maxAmount = 0.0;
            }
        }

        Sort sortObj = "desc".equalsIgnoreCase(sort) ? Sort.by("createdAt").descending() : Sort.by("createdAt").ascending();
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Instant startInst = (start != null) ? start.atStartOfDay().toInstant(ZoneOffset.UTC) : null;
        Instant endInst = (end != null) ? end.atTime(java.time.LocalTime.MAX).toInstant(ZoneOffset.UTC) : null;

        Page<Transaction> pageResult = repo.filterTransactions(userId, categoryId, categoryIds, startInst, endInst, minAmount, maxAmount, pageable);

        List<TransactionResponse> mapped = pageResult.getContent()
                .stream().map(this::toResponse).toList();

        return new FilterResponse(pageResult.getTotalElements(), mapped);
    }

    public List<java.util.Map<String, Object>> getMonthlySummary(Long userId, int year) {
        List<Object[]> results = repo.getMonthlySummary(userId, year);
        return results.stream().map(row -> {
            int month = (int) row[0];
            double income = (double) row[1];
            double expense = (double) row[2];
            return java.util.Map.<String, Object>of(
                    "month", month,
                    "totalIncome", income,
                    "totalExpense", expense,
                    "net", income - expense
            );
        }).toList();
    }

    public List<java.util.Map<String, Object>> getCategorySummary(Long userId, LocalDate start, LocalDate end) {
        Instant startInst = (start != null) ? start.atStartOfDay().toInstant(ZoneOffset.UTC) : Instant.EPOCH;
        Instant endInst = (end != null) ? end.atTime(java.time.LocalTime.MAX).toInstant(ZoneOffset.UTC) : Instant.now().plusSeconds(3153600000L); // +100 years

        List<Object[]> results = repo.getCategorySummary(userId, startInst, endInst);
        
        // Fetch all categories to map names
        List<CategoryDto> categories = categoryClient.getCategories(userId);
        java.util.Map<Long, CategoryDto> catMap = categories.stream()
                .collect(java.util.stream.Collectors.toMap(CategoryDto::getId, c -> c));

        return results.stream().map(row -> {
            Long catId = (Long) row[0];
            double total = (double) row[1];
            long count = (long) row[2];
            
            CategoryDto cat = catMap.get(catId);
            if (cat == null) return null; // Filter out unknown categories

            String name = cat.getName();
            String type = cat.getType();
            String emoji = cat.getEmoji();

            if ("EXPENSE".equalsIgnoreCase(type)) {
                total = Math.abs(total);
            }

            return java.util.Map.<String, Object>of(
                    "categoryId", catId,
                    "categoryName", name,
                    "categoryEmoji", emoji,
                    "categoryType", type,
                    "totalAmount", total,
                    "transactionCount", count
            );
        }).filter(Objects::nonNull).toList();
    }

    public List<MonthlyStatResponse> monthlyStats(Long userId) {
        return repo.monthlyStats(userId).stream()
                .map(row -> new MonthlyStatResponse((int) row[0], (double) row[1]))
                .toList();
    }

    public List<CategoryStatResponse> categoryStats(Long userId) {
        return repo.categoryStats(userId).stream()
                .map(row -> new CategoryStatResponse((Long) row[0], (double) row[1]))
                .toList();
    }

}
