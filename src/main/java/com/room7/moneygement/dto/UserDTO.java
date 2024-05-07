package com.room7.moneygement.dto;

import java.time.LocalDateTime;

import com.room7.moneygement.model.UserRole;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDTO {
	private Long userId;
	private String username;
	private String email;
	private String password;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String rememberToken;
	private Integer isDeleted;
	private UserRole role;
	private String profileImg;
	private String provider;
	private int exp;
	private String introduction;
}
