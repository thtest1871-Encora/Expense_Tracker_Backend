package com.subscriptionservice.demo.dto;

import java.time.LocalDate;

public class ExpiringSubscriptionResponse {

    private Long userId;
    private Long subscriptionId;
    private String name;
    private LocalDate endDate;
    private Double amount;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getSubscriptionId() { return subscriptionId; }
    public void setSubscriptionId(Long subscriptionId) { this.subscriptionId = subscriptionId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
