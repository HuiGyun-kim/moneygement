package com.room7.moneygement.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
@Setter
@Getter
public class DiaryDTO {
	private Long diaryId;

	private Long userId;
	private String content;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime expenseAt;

	public DiaryDTO(String content) {
		this.content = content;
	}

}

