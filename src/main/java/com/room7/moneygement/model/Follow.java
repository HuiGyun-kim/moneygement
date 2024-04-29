package com.room7.moneygement.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "follow")
public class Follow {

	@Id
	private Long followId;
	private Long followMemberId;
	private Long userId;

	// Getters and Setters
}
