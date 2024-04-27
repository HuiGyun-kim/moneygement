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
@Table(name = "qna")
public class QnA {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "qna_id")
	private Long qnaId;

	@Column(name = "user_id")
	private Long userId;

	private String question;

	private String answer;

	@Column(name = "created_at")
	private LocalDateTime createdAt;
}

