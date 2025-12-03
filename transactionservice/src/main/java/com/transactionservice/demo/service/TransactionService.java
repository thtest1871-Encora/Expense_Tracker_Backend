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

    private TransactionResponse toResponse(Transaction t, java.util.Map<Long, CategoryDto> catMap) {
        TransactionResponse r = new TransactionResponse();
        r.setId(t.getId());
        r.setUserId(t.getUserId());
        r.setTitle(t.getDescription());
        r.setCategoryId(t.getCategoryId());
        r.setAmount(t.getAmount());
        r.setCurrency("INR");
        r.setCreatedAt(t.getCreatedAt());
        
        if (catMap != null && catMap.containsKey(t.getCategoryId())) {
            CategoryDto c = catMap.get(t.getCategoryId());
            r.setCategoryName(c.getName());
            r.setCategoryEmoji(c.getEmoji());
            r.setCategoryType(c.getType());
        }
        return r;
    }

    @Transactional
    public TransactionResponse create(Long userId, CreateTransactionRequest req) {
        if (req.getCategoryId() == null) {
             throw new RuntimeException("Category ID is required.");
        }

        // 1. Fetch Category
        CategoryDto cat = categoryClient.getCategoryById(req.getCategoryId());
        if (cat == null) {
             throw new RuntimeException("Category not found.");
        }

        // 2. Validate Amount vs Category Type
        double amount = req.getAmount();
        String type = cat.getType();
        

        if ("INCOME".equalsIgnoreCase(type)) {
            if (amount <= 0) {
                throw new RuntimeException("Category type does not match transaction amount. Income must be positive.");
            }
            // Force positive just in case, though validation above handles it
            amount = Math.abs(amount);
        } else if ("EXPENSE".equalsIgnoreCase(type)) {
            if (amount >= 0) {
                // Prompt: "Convert stored value to negative if provided positive by client"
                amount = -Math.abs(amount);
            } else {
                // Already negative
                amount = -Math.abs(amount); // Ensure it's negative
            }
        } else {
            throw new RuntimeException("Invalid category type: " + type);
        }

        Transaction tx = new Transaction();
        tx.setUserId(userId);
        tx.setAmount(amount);
        tx.setCategoryId(req.getCategoryId());
        tx.setDescription(req.getDescription());
		if (req.getCreatedAt() != null || !LocalDate.parse(req.getCreatedAt()).isAfter(LocalDate.now())) {
			tx.setCreatedAt(LocalDate.parse(req.getCreatedAt()));
		}
		else {
			tx.setCreatedAt(LocalDate.now());
		}

        Transaction saved = repo.save(tx);

        // Send Notification
        String notifType = amount >= 0 ? "INCOME" : "EXPENSE";
        TransactionEvent event = new TransactionEvent(
                saved.getId(),
                saved.getUserId(),
                notifType,
                Math.abs(saved.getAmount()),
                saved.getCategoryId(),
                cat.getName(),
                cat.getEmoji(),
                saved.getDescription()
        );
        notificationClient.sendTransactionNotification(event);

        // Return Response with Metadata
        return toResponse(saved, java.util.Map.of(cat.getId(), cat));
    }

    public List<TransactionResponse> getByUser(Long userId) {
        List<Transaction> list = repo.findByUserId(userId);
        
        // Fetch all categories for mapping
        List<CategoryDto> categories = categoryClient.getCategories(userId);
        java.util.Map<Long, CategoryDto> catMap = categories.stream()
                .collect(java.util.stream.Collectors.toMap(CategoryDto::getId, c -> c));

        return list.stream()
                .sorted(java.util.Comparator.comparing(Transaction::getCreatedAt).reversed()) // Sort DESC
                .map(t -> toResponse(t, catMap))
                .toList();
    }

    @Transactional
    public void delete(Long id, Long userId) {
        repo.findById(id)
                .filter(t -> t.getUserId().equals(userId))
                .ifPresentOrElse(repo::delete,
                        () -> { throw new RuntimeException("Transaction not found OR not allowed"); });
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

        // Fetch all categories for mapping & filtering
        List<CategoryDto> categories = categoryClient.getCategories(userId);
        java.util.Map<Long, CategoryDto> catMap = categories.stream()
                .collect(java.util.stream.Collectors.toMap(CategoryDto::getId, c -> c));

        if (type != null) {
            categoryIds = categories.stream()
                    .filter(c -> type.equalsIgnoreCase(c.getType()))
                    .map(CategoryDto::getId)
                    .toList();
            
            if (categoryIds.isEmpty()) {
                return new FilterResponse(0, List.of());
            }

            if ("INCOME".equalsIgnoreCase(type)) {
                minAmount = 0.0;
            } else if ("EXPENSE".equalsIgnoreCase(type)) {
                maxAmount = 0.0;
            }
        }

        // Force Sort by CreatedAt DESC
        Sort sortObj = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page, size, sortObj);

        Instant startInst = (start != null) ? start.atStartOfDay().toInstant(ZoneOffset.UTC) : null;
        Instant endInst = (end != null) ? end.atTime(java.time.LocalTime.MAX).toInstant(ZoneOffset.UTC) : null;

        Page<Transaction> pageResult = repo.filterTransactions(userId, categoryId, categoryIds, startInst, endInst, minAmount, maxAmount, pageable);

        List<TransactionResponse> mapped = pageResult.getContent()
                .stream()
                .map(t -> toResponse(t, catMap))
                .toList();

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
