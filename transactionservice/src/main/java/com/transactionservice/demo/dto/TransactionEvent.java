package com.transactionservice.demo.dto;

public class TransactionEvent {

    private Long id;
    private Long userId;
    private String type;     // INCOME / EXPENSE
    private Double amount;
    private Long categoryId;
    private String categoryName;
    private String categoryEmoji;
    private String description;

    public TransactionEvent() {}

    public TransactionEvent(Long id, Long userId, String type, Double amount, Long categoryId, String categoryName, String categoryEmoji, String description) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryEmoji = categoryEmoji;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getCategoryEmoji() { return categoryEmoji; }
    public void setCategoryEmoji(String categoryEmoji) { this.categoryEmoji = categoryEmoji; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
