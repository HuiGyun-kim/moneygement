package com.room7.moneygement.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class QnADTO {
	private Long qnaId;
	private Long userId;
	private String question;
	private String answer;
	private LocalDateTime createdAt;
}