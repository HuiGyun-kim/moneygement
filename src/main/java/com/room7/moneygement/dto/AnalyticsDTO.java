package com.room7.moneygement.dto;

import java.util.Date;

import lombok.Data;

@Data
public class AnalyticsDTO {
	private Long analyticsId;
	private Long userId;
	private String type;
	private String content;
	private Date createdAt;
}
