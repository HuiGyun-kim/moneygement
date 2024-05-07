package com.room7.moneygement.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	@ManyToOne
	@JoinColumn(name = "ledger_id")
	@JsonIgnore
	private Ledger ledgerId;

	@Column(name = "category_id")
	private Long categoryId;

	@Column(name = "receipt_id")
	private Long receiptId;

	private Long amount;

	private String description;

	private LocalDate date;

	@Column(name = "created_at")
	private LocalDateTime createAt;

	@Column(name = "ledger_type", columnDefinition = "TINYINT(1)")
	private Boolean LedgerType;

	public LedgerEntry() {

	}

	public LedgerEntry(Ledger ledger, Long amount, LocalDate date, String description, boolean ledgerType) {
		this.ledgerId = ledger;
		this.amount = amount;
		this.date = date;
		this.description = description;
		this.LedgerType = ledgerType;
	}
}

