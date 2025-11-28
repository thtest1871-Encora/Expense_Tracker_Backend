package com.transactionservice.demo.controller;

import com.transactionservice.demo.dto.CreateTransactionRequest;
import com.transactionservice.demo.model.Transaction;
import com.transactionservice.demo.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    private Long getUserIdFromRequest(HttpServletRequest req) {
        return (Long) req.getAttribute("userId"); // Set by API Gateway filter
    }

    @PostMapping
    public Transaction create(@RequestBody CreateTransactionRequest req,
                              HttpServletRequest request) {
        return service.create(getUserIdFromRequest(request), req);
    }

    @GetMapping
    public List<Transaction> getUserTransactions(HttpServletRequest request) {
        return service.getByUser(getUserIdFromRequest(request));
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, HttpServletRequest request) {
        service.delete(id, getUserIdFromRequest(request));
        return "Transaction deleted";
    }
    
    @GetMapping("/filter")
    public ResponseEntity<?> filter(
            HttpServletRequest request,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long userId = getUserIdFromRequest(request);
        return ResponseEntity.ok(service.filterTransactions(userId, category, start, end, sort, page, size));
    }

    @GetMapping("/stats/monthly")
    public ResponseEntity<?> monthlyStats(HttpServletRequest request) {
        return ResponseEntity.ok(service.monthlyStats(getUserIdFromRequest(request)));
    }

    @GetMapping("/stats/category")
    public ResponseEntity<?> categoryStats(HttpServletRequest request) {
        return ResponseEntity.ok(service.categoryStats(getUserIdFromRequest(request)));
    }

}
