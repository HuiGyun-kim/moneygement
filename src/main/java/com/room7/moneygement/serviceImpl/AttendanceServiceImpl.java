package com.room7.moneygement.serviceImpl;

import com.room7.moneygement.model.Attendance;
import com.room7.moneygement.repository.AttendanceRepository;
import com.room7.moneygement.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    @Autowired
    public AttendanceServiceImpl(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    // 사용자의 출석체크를 저장하는 메서드
    @Override
    public void checkAttendance(Long userId) {
        LocalDate today = LocalDate.now();
        Attendance attendance = new Attendance();
        attendance.setUserId(userId);
        attendance.setDate(today);
        attendanceRepository.save(attendance);
    }

    // 사용자가 리워드를 받을 수 있는지 확인하는 메서드
    @Override
    public boolean isRewardEligible(Long userId) {
        // 현재 달의 시작일과 종료일 계산
        LocalDate startDate = LocalDate.now().withDayOfMonth(1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        // 해당 기간 동안의 출석체크 기록 조회
        List<Attendance> attendances = attendanceRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        // 출석체크 횟수가 25회 이상인 경우 리워드 지급 가능
        return attendances.size() >= 25;
    }

    //출석체크 정보 가져오는 메서드
    @Override
    public List<Attendance> getAllAttendance(Long userId) {
        return attendanceRepository.findByUserId(userId);
    }

    //출석체크 횟수 가져오는 메소드
    @Override
    public int getAttendanceCount(Long userId) {
        return attendanceRepository.countByUserId(userId);
    }
}

