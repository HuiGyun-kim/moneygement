package com.room7.moneygement.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserChallengeDTO {
	private Long userChallengeId;
	private Long challengeId;
	private Long userId;
	private LocalDateTime joinDate;
	private String isCompleted;
	private AttendanceStatusDTO attendanceStatus; //Enum 타입으로 출석체크 완료 여부 표시
	private int attendanceCount; //출석체크 횟수
	private Long targetAmount;

	public UserChallengeDTO(Long userId, Long targetAmount) {
	}

	public int getAttendanceCount() {
		return attendanceCount;
	}

	public void setAttendanceCount(int attendanceCount) {
		this.attendanceCount = attendanceCount;
	}
}

