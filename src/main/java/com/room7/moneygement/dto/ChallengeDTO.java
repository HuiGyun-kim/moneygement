package com.room7.moneygement.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChallengeDTO {
	private Long challengeId;
	private String title;
	private String description;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private String reward;
}

