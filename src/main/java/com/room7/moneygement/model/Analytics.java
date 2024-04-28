package com.room7.moneygement.model;

import java.util.Date;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "analytics")
public class Analytics {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "analytics_id")
	private Long analyticsId;

	@Column(name = "user_id")
	private Long userId;

	private String type;

	@Column(name = "content")
	private String content;

	@Column(name = "created_at")
	private Date createdAt;
}