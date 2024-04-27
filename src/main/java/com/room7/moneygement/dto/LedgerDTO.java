package com.room7.moneygement.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LedgerDTO {
	private Long ledgerId;
	private Long userId;
	private String title;
	private LocalDateTime createdAt;
}

