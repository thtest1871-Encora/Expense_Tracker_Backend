package com.example.expense.dto;

public class UpdateUserProfileRequest {

    private String fullName;
    private String phone;
    private String avatarUrl;
    private java.time.LocalDate dob;
    private Integer age;
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
    public java.time.LocalDate getDob() {
        return dob;
    }
    public void setDob(java.time.LocalDate dob) {
        this.dob = dob;
    }
    public Integer getAge() {
        return age;
    }
    public void setAge(Integer age) {
        this.age = age;
    }
    
    
}
