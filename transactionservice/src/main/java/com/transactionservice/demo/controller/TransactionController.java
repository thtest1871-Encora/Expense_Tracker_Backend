package com.transactionservice.demo.controller;

import com.transactionservice.demo.dto.ApiResponse;
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
        return Long.valueOf(req.getHeader("X-User-Id"));
    }

    @PostMapping
    public ApiResponse<Transaction> create(@RequestBody CreateTransactionRequest req,
                              HttpServletRequest request) {
        Transaction tx = service.create(getUserIdFromRequest(request), req);
        return ApiResponse.success("Transaction created successfully", tx);
    }

    @GetMapping
    public ApiResponse<List<Transaction>> getUserTransactions(HttpServletRequest request) {
        List<Transaction> list = service.getByUser(getUserIdFromRequest(request));
        return ApiResponse.success("Transactions retrieved", list);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id, HttpServletRequest request) {
        service.delete(id, getUserIdFromRequest(request));
        return ApiResponse.success("Transaction deleted", null);
    }

    @GetMapping("/filter")
    public ApiResponse<?> filter(
            HttpServletRequest request,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long userId = getUserIdFromRequest(request);
        return ApiResponse.success("Filtered transactions", service.filterTransactions(userId, category, start, end, sort, page, size));
    }

    @GetMapping("/stats/monthly")
    public ApiResponse<?> monthlyStats(HttpServletRequest request) {
        return ApiResponse.success("Monthly stats", service.monthlyStats(getUserIdFromRequest(request)));
    }

    @GetMapping("/stats/category")
    public ApiResponse<?> categoryStats(HttpServletRequest request) {
        return ApiResponse.success("Category stats", service.categoryStats(getUserIdFromRequest(request)));
    }

}
