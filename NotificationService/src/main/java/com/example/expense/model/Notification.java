package com.example.expense.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "notifications",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "txn_id"}),
        @UniqueConstraint(columnNames = {"user_id", "subscription_id"})
})
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable=false)
    private Long userId;

    private String type; // TRANSACTION / SUBSCRIPTION

    @Column(columnDefinition = "TEXT", nullable=false)
    private String message;

    @Column(name="created_at")
    private Instant createdAt = Instant.now();

    private String status = "UNREAD";

    @Column(name="txn_id")
    private Long txnId;

    @Column(name="subscription_id")
    private Long subscriptionId;

    public Notification() {}

    public Notification(Long userId, String type, String message, Long txnId, Long subscriptionId) {
        this.userId = userId;
        this.type = type;
        this.message = message;
        this.txnId = txnId;
        this.subscriptionId = subscriptionId;
        this.createdAt = Instant.now();
        this.status = "UNREAD";
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getTxnId() {
		return txnId;
	}

	public void setTxnId(Long txnId) {
		this.txnId = txnId;
	}

	public Long getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(Long subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

    
}
