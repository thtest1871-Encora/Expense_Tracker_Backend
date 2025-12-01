package com.transactionservice.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateTransactionRequest {
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private String description;

    public Double getAmount() { return amount; }
    public Long getCategoryId() { return categoryId; }
    public String getDescription() { return description; }

    public void setAmount(Double amount) { this.amount = amount; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public void setDescription(String description) { this.description = description; }
}
