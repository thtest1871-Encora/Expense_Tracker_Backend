package com.transactionservice.demo.dto;

import java.time.LocalDate;

public class TransactionResponse {
    private Long id;
    private Long userId;
    private String title;
    private String category;
    private double amount;
    private LocalDate date;

    public TransactionResponse() {}

    public TransactionResponse(Long id, Long userId, String title, String category, double amount, LocalDate date) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.category = category;
        this.amount = amount;
        this.date = date;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

    // getters + setters
}
