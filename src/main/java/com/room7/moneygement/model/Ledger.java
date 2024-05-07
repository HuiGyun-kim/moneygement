package com.room7.moneygement.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "ledger")
public class Ledger {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ledger_id")
	private Long ledgerId;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User userId;

	private String title;

	@CreationTimestamp
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	// 일대다 관계 추가
	@OneToMany(mappedBy = "ledgerId", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<LedgerEntry> entries = new ArrayList<>();
}

