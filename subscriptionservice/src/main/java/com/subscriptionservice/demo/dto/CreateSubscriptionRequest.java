package com.subscriptionservice.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class CreateSubscriptionRequest {

    @NotBlank
    private String name;          // Netflix, Spotify, etc.

    private String provider;      // optional standardized name
    private String planName;      // Basic, Premium, etc.

    @NotNull
    private Double amount;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String billingCycle;  // MONTHLY/YEARLY/CUSTOM

    // getters / setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getBillingCycle() { return billingCycle; }
    public void setBillingCycle(String billingCycle) { this.billingCycle = billingCycle; }
}
