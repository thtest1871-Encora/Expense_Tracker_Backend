package com.example.expense.dto;

import java.time.LocalDate;

public class SubscriptionDto {

    private Long id;
    private Long userId;
    private String name;
    private LocalDate expiryDate;
    private Double amount;

    public SubscriptionDto() {}

    public SubscriptionDto(Long id, Long userId, String name, LocalDate expiryDate, Double amount) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.expiryDate = expiryDate;
        this.amount = amount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    @Override
    public String toString() {
        return "SubscriptionDto{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", expiryDate=" + expiryDate +
                ", amount=" + amount +
                '}';
    }
}
