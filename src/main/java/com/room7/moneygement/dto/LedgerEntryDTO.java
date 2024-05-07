package com.room7.moneygement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.room7.moneygement.model.Ledger;
import com.room7.moneygement.model.User;

import lombok.Data;

@Data
public class LedgerEntryDTO {
	private Long entryId;
	private Long ledgerId;
	private User userId;
	private Long categoryId;
	private Long receiptId;
	private Long amount;
	private String description;
	private LocalDate date;
	private LocalDateTime createAt;
	private boolean ledgerType;

	public LedgerEntryDTO(Long amount, LocalDate date, String description) {
		this.amount = amount;
		this.date = date;
		this.description = description;
	}
}


