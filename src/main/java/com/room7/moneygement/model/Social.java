package com.room7.moneygement.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "social")
public class Social {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "social_id")
	private Long socialId;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "social_kakao")
	private String socialKakao;

	@Column(name = "social_google")
	private String socialGoogle;

	@Column(name = "social_naver")
	private String socialNaver;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "related_user_id")
	private Long relatedUserId;

	private String content;

	@Enumerated(EnumType.STRING)
	private SocialType type;
}

