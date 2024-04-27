package com.room7.moneygement.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "challenge")
public class Challenge {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "challenge_id")
	private Long challengeId;

	private String title;

	@Lob
	private String description;

	@Column(name = "start_date")
	private LocalDateTime startDate;
	@Column(name = "end_date")
	private LocalDateTime endDate;
	private String reward;
}
