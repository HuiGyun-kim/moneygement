package com.room7.moneygement.model;

<<<<<<< HEAD
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
=======
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
>>>>>>> develop
@Table(name = "category")
public class Category {

	@Id
<<<<<<< HEAD
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private Long categoryId;

	@Column(name = "category_name")
	private String categoryName;
=======
	private String categoryId;
	private String food;
	private String traffic;
	private String culture;
	private String education;
	private String etc;
	private String always;

	// Getters and Setters
>>>>>>> develop
}
