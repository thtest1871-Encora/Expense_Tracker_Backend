package com.example.expense.dto;

public class CreateUserProfileRequest {

	private Long userId;
    private String fullName;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
}
