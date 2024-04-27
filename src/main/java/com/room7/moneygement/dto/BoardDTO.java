package com.room7.moneygement.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BoardDTO {
	private Long boardId;
	private Long userId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String type;
}

