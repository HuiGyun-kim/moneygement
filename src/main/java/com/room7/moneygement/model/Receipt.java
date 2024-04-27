package com.room7.moneygement.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "receipt")
public class Receipt {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "receipt_id")
	private Long receiptId;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "recognized_text")
	private String recognizedText;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}

