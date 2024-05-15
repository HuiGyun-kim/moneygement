package com.room7.moneygement.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.room7.moneygement.model.User;

import lombok.Data;

@Data
public class LedgerEntryDTO {
	private Long entryId;
	private Long ledgerId;
	private User userId;
	private Long categoryId;
	private String categoryName;
	private Long amount;
	private String description;
	private LocalDate date;
	private LocalDateTime createAt;
	private boolean ledgerType;


	public LedgerEntryDTO(Long entryId, Long ledgerId, Long categoryId, String categoryName, LocalDateTime createAt, Long amount, LocalDate date, String description) {
		this.entryId = entryId;
		this.ledgerId = ledgerId;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.createAt = createAt;
		this.amount = amount;
		this.date = date;
		this.description = description;
	}

	public LedgerEntryDTO(LocalDate date, Long amount, String description, String categoryName, Boolean ledgerType) {
		this.date = date;
		this.amount = amount;
		this.description = description;
		this.categoryName = categoryName; // category의 name 필드를 문자열로 받습니다.
		this.ledgerType = ledgerType;
	}
	public LedgerEntryDTO() {}

	@JsonCreator
	public LedgerEntryDTO(@JsonProperty("entryId") Long entryId,
		@JsonProperty("ledgerId") Long ledgerId,
		@JsonProperty("categoryId") Long categoryId,
		@JsonProperty("categoryName") String categoryName,
		@JsonProperty("createAt") LocalDateTime createAt,
		@JsonProperty("amount") Long amount,
		@JsonProperty("date") LocalDate date,
		@JsonProperty("description") String description,
		@JsonProperty("ledgerType") boolean ledgerType) {
		this.entryId = entryId;
		this.ledgerId = ledgerId;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.createAt = createAt;
		this.amount = amount;
		this.date = date;
		this.description = description;
		this.ledgerType = ledgerType;
	}
}


