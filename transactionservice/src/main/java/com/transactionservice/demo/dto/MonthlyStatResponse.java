package com.transactionservice.demo.dto;

public class MonthlyStatResponse {
    private int month;
    private double total;

    public MonthlyStatResponse() {
    }

    public MonthlyStatResponse(int month, double total) {
        this.month = month;
        this.total = total;
    }

    public int getMonth() {
        return month;
    }
    public void setMonth(int month) {
        this.month = month;
    }

    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
}
