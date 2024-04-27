package com.room7.moneygement.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserChallengeDTO {
	private Long userChallengeId;
	private Long challengeId;
	private Long userId;
	private LocalDateTime joinDate;
	private String isCompleted;
}

