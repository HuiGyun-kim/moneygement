package com.room7.moneygement.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserDTO {
	private Long userId;
	private String username;
	private String email;
	private String password;
	private LocalDateTime createdAt;
	private String rememberToken;
	private Integer isDeleted;
	private String role;
	private String profileImg;
}
