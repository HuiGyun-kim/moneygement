package com.room7.moneygement.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReceiptDTO {
	private Long receiptId;
	private Long userId;
	private String imageUrl;
	private String recognizedText;
	private LocalDateTime updatedAt;
}

