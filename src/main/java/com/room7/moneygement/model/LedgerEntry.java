package com.room7.moneygement.model;

import java.time.LocalDate;
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
@Table(name = "ledgerentry")
public class LedgerEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "entry_id")
	private Long entryId;

	@Column(name = "ledger_id")
	private Long ledgerId;

	@Column(name = "receipt_id")
	private Long receiptId;

	private Integer amount;

	private String description;

	private LocalDate date;

	private String category;

	@Column(name = "create_at")
	private LocalDateTime createAt;

	private Integer income;
}

