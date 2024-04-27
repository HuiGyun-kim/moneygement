package com.room7.moneygement.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SocialDTO {
	private Long socialId;
	private Long userId;
	private String socialKakao;
	private Integer socialGoogle;
	private LocalDateTime createdAt;
	private Long relatedUserId;
	private String content;
	private String type;
}
