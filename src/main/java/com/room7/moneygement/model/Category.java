package com.room7.moneygement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class Category {

	@Id
	private String categoryId;
	private String food;
	private String traffic;
	private String culture;
	private String education;
	private String etc;
	private String always;

	// Getters and Setters
}
