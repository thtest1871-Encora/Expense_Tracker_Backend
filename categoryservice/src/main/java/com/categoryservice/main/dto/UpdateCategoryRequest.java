package com.categoryservice.main.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateCategoryRequest {

    @NotBlank
    private String name;

    private String emoji;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmoji() {
		return emoji;
	}

	public void setEmoji(String emoji) {
		this.emoji = emoji;
	}
    
    
    
}