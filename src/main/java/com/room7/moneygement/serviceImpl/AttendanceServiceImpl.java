package com.room7.moneygement.serviceImpl;

import com.room7.moneygement.dto.UserChallengeDTO;
import com.room7.moneygement.service.AttendanceService;
import org.springframework.stereotype.Service;

@Service
public class AttendanceServiceImpl implements AttendanceService {


    @Override
    public boolean checkAttendance(UserChallengeDTO userChallengeDTO) {
        // 출석체크 정보 저장 후 성공 여부 반환
        boolean isAttendanceSaved = saveAttendance(userChallengeDTO);

        // 출석체크 횟수가 25번 이상인 경우 리워드 제공
        if (userChallengeDTO.getAttendanceCount() >= 25) {
            provideReward(userChallengeDTO);
        }

        return isAttendanceSaved;
    }

    private void provideReward(UserChallengeDTO userChallengeDTO) {
        // 리워드 지급 로직
        // 출석체크 횟수가 25번 이상이면 리워드 제공
        // 리워드 제공 로직 구현
    }

    private boolean saveAttendance(UserChallengeDTO userChallengeDTO) {
        // 출석체크 정보 DB에 저장하는 로직
        // Repository 사용해 저장
        return true; // 임시로 성공 여부를 true로 반환
    }
}
