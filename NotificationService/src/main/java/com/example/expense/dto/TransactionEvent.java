package com.example.expense.dto;

public class TransactionEvent {

    private Long id;         // transaction id
    private Long userId;
    private String type;     // CREDIT / DEBIT
    private Double amount;
    private String category; // category name or id (string for flexibility)
    private String note;

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
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    @Override
    public String toString() {
        return "TransactionEvent{" +
                "id=" + id +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
