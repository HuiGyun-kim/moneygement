package com.room7.moneygement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.room7.moneygement.dto.UserChallengeDTO;
import com.room7.moneygement.service.AttendanceService;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/check")
    public ResponseEntity<String> checkAttendance(@RequestBody UserChallengeDTO userChallengeDTO) {
        // 출석체크 정보를 DB에 저장하는 메서드 호출
        boolean isAttendanceSuccessful = attendanceService.checkAttendance(userChallengeDTO);

        if (isAttendanceSuccessful) {
            return ResponseEntity.ok("출석체크가 완료되었습니다!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("출석체크에 실패했습니다.");
        }
    }
}



