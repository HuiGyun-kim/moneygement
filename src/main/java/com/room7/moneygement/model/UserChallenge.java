package com.room7.moneygement.model;

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
@Table(name = "userchallenge")
public class UserChallenge {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_challenge_id")
	private Long userChallengeId;

	@Column(name = "challenge_id")
	private Long challengeId;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "join_date")
	private LocalDateTime joinDate;

	@Column(name = "is_completed", columnDefinition = "TINYINT(1)")
	private Boolean isCompleted;
}
