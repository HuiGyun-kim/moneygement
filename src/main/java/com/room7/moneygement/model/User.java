package com.room7.moneygement.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
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

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "remember_token")
	private String rememberToken;

	@Column(name = "is_deleted", columnDefinition = "TINYINT(1)")
	private Boolean isDeleted;

	@Enumerated(EnumType.STRING)
	private UserRole role;

	@Column(name = "profile_img")
	private String profileImg;

	private String provider;

	private int exp;

  private String introduction;
  
	@OneToMany(mappedBy = "followMemberId", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Follow> followers = new ArrayList<>();

	@OneToMany(mappedBy = "userId", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Follow> followings = new ArrayList<>();
}

