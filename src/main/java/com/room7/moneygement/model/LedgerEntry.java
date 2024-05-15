package com.room7.moneygement.model;

import java.time.LocalDate;
import java.time.LocalDateTime;


import org.hibernate.annotations.CreationTimestamp;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

	@JsonIgnoreProperties({"entries"})
	@ManyToOne
	@JoinColumn(name = "ledger_id")
	private Ledger ledger;

	@ManyToOne
	@JoinColumn(name = "category_id",referencedColumnName = "category_id")
	private Category category;

	private Long amount;

	private String description;

	private LocalDate date = LocalDate.now();

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createAt;

	@Column(name = "ledger_type", columnDefinition = "TINYINT(1)")
	private Boolean ledgerType;

	public LedgerEntry() {

	}

	public LedgerEntry(Ledger ledger, Long amount, LocalDate date, String description, boolean ledgerType) {
		this.ledger = ledger;
		this.amount = amount;
		this.date = date;
		this.description = description;
		this.ledgerType = ledgerType;
	}
}

