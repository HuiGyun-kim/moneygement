package com.room7.moneygement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class LedgerEntryDTO {
	private Long entryId;
	private Long ledgerId;
	private Long receiptId;
	private Integer amount;
	private String description;
	private LocalDate date;
	private String category;
	private LocalDateTime createAt;
	private boolean ledgerType;
}

