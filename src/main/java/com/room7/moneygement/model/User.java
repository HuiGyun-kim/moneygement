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
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	private String username;

	private String email;

	private String password;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "remember_token")
	private String rememberToken;

	@Column(name = "is_deleted", columnDefinition = "TINYINT(1)")
	private Boolean isDeleted;

	@Enumerated(EnumType.STRING)
	private UserRole role;

	@Column(name = "profile_img")
	private String profileImg;

	@Column(name = "report_count")
	private Long reportCount;
}

