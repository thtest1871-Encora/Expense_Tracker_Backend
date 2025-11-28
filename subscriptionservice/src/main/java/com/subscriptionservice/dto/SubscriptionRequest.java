package com.subscriptionservice.dto;

public class SubscriptionRequest {
    private String platformName;
    private String planName;
    private Double amount;
    private String billingCycle; // WEEKLY, MONTHLY, YEARLY

    // Getters and Setters
    public String getPlatformName() { return platformName; }
    public void setPlatformName(String platformName) { this.platformName = platformName; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getBillingCycle() { return billingCycle; }
    public void setBillingCycle(String billingCycle) { this.billingCycle = billingCycle; }
}
