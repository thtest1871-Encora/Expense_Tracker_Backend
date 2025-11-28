package com.transactionservice.demo.service;

import com.transactionservice.demo.client.NotificationClient;
import com.transactionservice.demo.dto.CategoryStatResponse;
import com.transactionservice.demo.dto.CreateTransactionRequest;
import com.transactionservice.demo.dto.FilterResponse;
import com.transactionservice.demo.dto.MonthlyStatResponse;
import com.transactionservice.demo.dto.TransactionEvent;
import com.transactionservice.demo.dto.TransactionResponse;
import com.transactionservice.demo.model.Transaction;
import com.transactionservice.demo.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repo;
    private final NotificationClient notificationClient;

    public TransactionService(TransactionRepository repo, NotificationClient notificationClient) {
        this.repo = repo;
        this.notificationClient = notificationClient;
    }

    private TransactionResponse toResponse(Transaction t) {
        TransactionResponse r = new TransactionResponse();
        r.setId(t.getId());
        r.setUserId(t.getUserId());
        r.setTitle(t.getDescription());
        r.setCategory(t.getCategory());
        r.setAmount(t.getAmount());
        r.setDate(t.getCreatedAt().toLocalDate());
        return r;
    }

    @Transactional
    public Transaction create(Long userId, CreateTransactionRequest req) {
        Transaction tx = new Transaction();
        tx.setUserId(userId);
        tx.setAmount(req.getAmount());
        tx.setCategory(req.getCategory());
        tx.setDescription(req.getDescription());

        Transaction saved = repo.save(tx);

        // 🔥 Send event to Notification Service
        String type = saved.getAmount() >= 0 ? "Income" : "Expense";

        TransactionEvent event = new TransactionEvent(
                saved.getId(),
                saved.getUserId(),
                type,
                saved.getAmount(),
                saved.getCategory(),
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
            String category,
            LocalDate start,
            LocalDate end,
            String sort,
            int page,
            int size
    ) {
        List<Transaction> list = repo.filterTransactions(userId, category, start, end);

        if ("desc".equalsIgnoreCase(sort)) {
            list.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        } else {
            list.sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
        }

        int from = page * size;
        int to = Math.min(from + size, list.size());

        List<TransactionResponse> mapped = list.subList(from, to)
                .stream().map(this::toResponse).toList();

        return new FilterResponse(list.size(), mapped);
    }

    public List<MonthlyStatResponse> monthlyStats(Long userId) {
        return repo.monthlyStats(userId).stream()
                .map(row -> new MonthlyStatResponse((int) row[0], (double) row[1]))
                .toList();
    }

    public List<CategoryStatResponse> categoryStats(Long userId) {
        return repo.categoryStats(userId).stream()
                .map(row -> new CategoryStatResponse((String) row[0], (double) row[1]))
                .toList();
    }

}
