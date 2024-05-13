package com.room7.moneygement.controller;

import com.room7.moneygement.dto.AttendanceDTO;
import com.room7.moneygement.dto.UserChallengeDTO;
import com.room7.moneygement.model.Attendance;
import com.room7.moneygement.model.UserChallenge;
import com.room7.moneygement.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // 출석체크 기록 API
    @PostMapping("/check")
    public ResponseEntity<?> checkAttendance(Long userId) {
        attendanceService.checkAttendance(userId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 리워드 지급 가능 여부 확인 API
    @PostMapping("/reward/eligible")
    public ResponseEntity<?> checkRewardEligibility(Long userId) {
        boolean isEligible = attendanceService.isRewardEligible(userId);
        return ResponseEntity.status(HttpStatus.OK).body(isEligible);
    }

    //모든 출석체크 정보 조회하는 API 엔드포인트
    @GetMapping("/all")
    public ResponseEntity<List<AttendanceDTO>> getAllAttendance() {
        List<Attendance> attendances = attendanceService.getAllAttendance();
        List<AttendanceDTO> attendanceDTOs = attendances.stream()
                .map(attendance -> {
                    AttendanceDTO attendanceDTO = new AttendanceDTO();
                    attendanceDTO.setId(attendance.getId());
                    attendanceDTO.setUserId(attendance.getUserId());
                    attendanceDTO.setDate(attendance.getDate());
                    attendanceDTO.setCompleted(attendance.isCompleted());
                    return attendanceDTO;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(attendanceDTOs);
    }



}





