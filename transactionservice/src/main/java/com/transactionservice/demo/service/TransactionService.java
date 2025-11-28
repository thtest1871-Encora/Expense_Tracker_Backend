package com.transactionservice.demo.service;

import com.transactionservice.demo.dto.CategoryStatResponse;
import com.transactionservice.demo.dto.CreateTransactionRequest;
import com.transactionservice.demo.dto.FilterResponse;
import com.transactionservice.demo.dto.MonthlyStatResponse;
import com.transactionservice.demo.dto.TransactionResponse;
import com.transactionservice.demo.model.Transaction;
import com.transactionservice.demo.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repo;
    private TransactionResponse toResponse(Transaction t) {
        TransactionResponse r = new TransactionResponse();
        r.setId(t.getId());
        r.setUserId(t.getUserId());
        r.setTitle(t.getDescription()); // mapping description as title
        r.setCategory(t.getCategory());
        r.setAmount(t.getAmount());
        r.setDate(t.getCreatedAt().toLocalDate()); // convert LocalDateTime → LocalDate
        return r;
    }


    public TransactionService(TransactionRepository repo) {
        this.repo = repo;
    }

    public Transaction create(Long userId, CreateTransactionRequest req) {
        Transaction tx = new Transaction();
        tx.setUserId(userId);
        tx.setAmount(req.getAmount());
        tx.setCategory(req.getCategory());
        tx.setDescription(req.getDescription());
        return repo.save(tx);
    }

    public List<Transaction> getByUser(Long userId) {
        return repo.findByUserId(userId);
    }

    public void delete(Long id, Long userId) {
        repo.findById(id)
                .filter(t -> t.getUserId().equals(userId))
                .ifPresentOrElse(repo::delete,
                        () -> { throw new RuntimeException("Transaction not found OR not allowed"); });
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
