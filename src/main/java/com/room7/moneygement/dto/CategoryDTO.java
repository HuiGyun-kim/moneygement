package com.room7.moneygement.dto;

import lombok.Data;

@Data
public class CategoryDTO {
	private Long categoryId;
	private String categoryName;

	public CategoryDTO(Long categoryId, String categoryName) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}
}
