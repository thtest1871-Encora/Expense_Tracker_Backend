package com.subscriptionservice.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String platformName;
    private String planName;
    private Double amount;
    private String billingCycle; // WEEKLY, MONTHLY, YEARLY
    private LocalDate nextDueDate;

    @Transient
    private long daysUntilRenewal;

    @Transient
    private String status; // ACTIVE, OVERDUE, DUE_SOON

    public Subscription() {}

    public Subscription(Long userId, String platformName, String planName, Double amount, String billingCycle, LocalDate nextDueDate) {
        this.userId = userId;
        this.platformName = platformName;
        this.planName = planName;
        this.amount = amount;
        this.billingCycle = billingCycle;
        this.nextDueDate = nextDueDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getPlatformName() { return platformName; }
    public void setPlatformName(String platformName) { this.platformName = platformName; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getBillingCycle() { return billingCycle; }
    public void setBillingCycle(String billingCycle) { this.billingCycle = billingCycle; }
    public LocalDate getNextDueDate() { return nextDueDate; }
    public void setNextDueDate(LocalDate nextDueDate) { this.nextDueDate = nextDueDate; }

    public long getDaysUntilRenewal() { return daysUntilRenewal; }
    public void setDaysUntilRenewal(long daysUntilRenewal) { this.daysUntilRenewal = daysUntilRenewal; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
