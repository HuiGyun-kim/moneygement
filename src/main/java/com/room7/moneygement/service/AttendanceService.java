package com.room7.moneygement.service;

import com.room7.moneygement.dto.UserChallengeDTO;
import com.room7.moneygement.model.User;

public interface AttendanceService {
    boolean checkAttendance(UserChallengeDTO userChallengeDTO); //출석체크 수행 메서드
}

