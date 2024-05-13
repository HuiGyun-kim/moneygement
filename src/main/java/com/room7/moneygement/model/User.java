package com.room7.moneygement.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	// 이 사용자를 팔로우하는 사람들 (즉, 이 사용자가 'following_id')
	@OneToMany(mappedBy = "following")
	private Set<Follow> followers = new HashSet<>();

	// 이 사용자가 팔로우하는 사람들 (즉, 이 사용자가 'follower_id')
	@OneToMany(mappedBy = "follower")
	private Set<Follow> followings = new HashSet<>();

	public int getCurrentLevel() {
		int baseExpPerLevel = 100;
		int maxLevel = 6;
		int expIncreasePerLevel = 500;
		int level = 1;

		int requiredExp = baseExpPerLevel;
		while(level < maxLevel && this.exp >= requiredExp) {
			level++;
			requiredExp = baseExpPerLevel + (level - 1) * expIncreasePerLevel;
		}

		return level;
	}

}

