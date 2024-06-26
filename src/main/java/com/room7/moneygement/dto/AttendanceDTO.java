package com.room7.moneygement.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class AttendanceDTO {
	private Long id;
	private Long userId;
	private LocalDate date;
	private boolean attended;
}
