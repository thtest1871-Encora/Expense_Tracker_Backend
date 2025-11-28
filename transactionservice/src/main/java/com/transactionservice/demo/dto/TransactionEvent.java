package com.transactionservice.demo.dto;

public class TransactionEvent {

    private Long id;         // transaction id
    private Long userId;
    private String type;     // Income / Expense
    private Double amount;
    private String category;
    private String note;     // maps to "description" in Transaction

    public TransactionEvent() {}

    public TransactionEvent(Long id, Long userId, String type, Double amount, String category, String note) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.note = note;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getType() { return type; }
    public Double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getNote() { return note; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setType(String type) { this.type = type; }
    public void setAmount(Double amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    public void setNote(String note) { this.note = note; }
}
