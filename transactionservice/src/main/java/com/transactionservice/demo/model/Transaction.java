package com.transactionservice.demo.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "transactions", indexes = @Index(name = "idx_user_id", columnList = "userId"))
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Double amount;
    private Long categoryId;
    private String description;

    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Double getAmount() { return amount; }
    public Long getCategoryId() { return categoryId; }
    public String getDescription() { return description; }
    public Instant getCreatedAt() { return createdAt; }

    public void setUserId(Long userId) { this.userId = userId; }
    public void setAmount(Double amount) { this.amount = amount; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public void setDescription(String description) { this.description = description; }
}
