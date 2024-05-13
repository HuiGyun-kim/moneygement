package com.room7.moneygement.service;

import com.room7.moneygement.model.Attendance;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceService {
    void checkAttendance(Long userId);
    boolean isRewardEligible(Long userId);
    List<Attendance> getAllAttendance(Long userId);
    int getAttendanceCount(Long userId);
}


