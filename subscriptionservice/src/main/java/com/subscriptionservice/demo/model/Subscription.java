package com.subscriptionservice.demo.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;              // owner

    private String name;              // e.g., "Netflix"
    private String provider;          // e.g., "NETFLIX", "SPOTIFY" (optional)
    private String planName;          // "Basic", "Premium", etc.

    private Double amount;            // monthly/yearly cost

    private LocalDate startDate;
    private LocalDate endDate;        // used for expiry notifications

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status = SubscriptionStatus.ACTIVE;

    private String billingCycle;      // "MONTHLY", "YEARLY", "CUSTOM"

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // getters & setters

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getName() { return name; }
    public String getProvider() { return provider; }
    public String getPlanName() { return planName; }
    public Double getAmount() { return amount; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public SubscriptionStatus getStatus() { return status; }
    public String getBillingCycle() { return billingCycle; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }
    public void setProvider(String provider) { this.provider = provider; }
    public void setPlanName(String planName) { this.planName = planName; }
    public void setAmount(Double amount) { this.amount = amount; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setStatus(SubscriptionStatus status) { this.status = status; }
    public void setBillingCycle(String billingCycle) { this.billingCycle = billingCycle; }
}
