package com.room7.moneygement.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DiaryDTO {
	private Long diaryId;
	private Long userId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime expenseAt;
}
