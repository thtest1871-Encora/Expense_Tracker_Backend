package com.transactionservice.demo.dto;

public class CreateTransactionRequest {
    private Double amount;
    private String category;
    private String description;

    public Double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDescription() { return description; }

    public void setAmount(Double amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
}
