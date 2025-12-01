package com.transactionservice.demo.dto;

public class CategoryStatResponse {
    private Long categoryId;
    private double total;

    public CategoryStatResponse() {
    }

    public CategoryStatResponse(Long categoryId, double total) {
        this.categoryId = categoryId;
        this.total = total;
    }

    public Long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
}
