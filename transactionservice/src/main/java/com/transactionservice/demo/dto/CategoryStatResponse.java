package com.transactionservice.demo.dto;

public class CategoryStatResponse {
    private String category;
    private double total;

    public CategoryStatResponse() {
    }

    public CategoryStatResponse(String category, double total) {
        this.category = category;
        this.total = total;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
}
