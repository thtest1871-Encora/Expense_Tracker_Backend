package com.transactionservice.demo.dto;

import java.time.Instant;
import java.time.LocalDate;

public class TransactionResponse {
    private Long id;
    private Long userId;
    private String title;
    private Long categoryId;
    private double amount;
    private String currency;
    private LocalDate createdAt;
    
    private String categoryName;
    private String categoryEmoji;
    private String categoryType;

    public TransactionResponse() {}

    public TransactionResponse(Long id, Long userId, String title, Long categoryId, double amount, String currency, LocalDate createdAt) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.categoryId = categoryId;
        this.amount = amount;
        this.currency = currency;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate localDate) {
        this.createdAt = localDate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryEmoji() {
        return categoryEmoji;
    }

    public void setCategoryEmoji(String categoryEmoji) {
        this.categoryEmoji = categoryEmoji;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
}
