package com.room7.moneygement.service;

import com.room7.moneygement.model.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    boolean isRewardEligible(Long userId);
    List<Attendance> getAllAttendance(Long userId);

    void checkAttendance(Long userId, LocalDate date);

    //출석체크 횟수 가져오는 메소드
    int getAttendanceCount(Long userId);
}


