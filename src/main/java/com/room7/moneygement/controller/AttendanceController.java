package com.room7.moneygement.controller;

import com.room7.moneygement.dto.AttendanceDTO;
import com.room7.moneygement.dto.UserChallengeDTO;
import com.room7.moneygement.model.Attendance;
import com.room7.moneygement.model.UserChallenge;
import com.room7.moneygement.service.AttendanceService;
import com.room7.moneygement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final UserService userService;


    // 출석 체크하는 메소드
    @PostMapping("/check")
    public ResponseEntity<?> checkAttendance(@RequestBody Map<String, String> request) {
        try {
            // 요청에서 날짜 정보 가져오기
            String dateString = request.get("date");
            LocalDate date = LocalDate.parse(dateString);

            // 현재 로그인한 사용자의 ID 가져오기 (예시로 userId = 1로 가정)
            Long userId = 1L; // 실제로는 세션 또는 인증 정보를 통해 가져와야 합니다.

            // 출석체크 저장
            attendanceService.checkAttendance(userId, date);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // 예외 발생 시 에러 응답
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while checking attendance.");
        }
    }

    @PostMapping("/attendance/check")
    public ResponseEntity<String> checkAttendance(@RequestBody AttendanceDTO requestData) {
        try {
            Long userId = requestData.getUserId();
            LocalDate date = requestData.getDate();

            // 출석체크를 처리하는 서비스 메소드 호출
            attendanceService.checkAttendance(userId, date);

            // 출석체크가 성공적으로 처리되었음을 응답으로 전송
            return ResponseEntity.ok("출석체크가 완료되었습니다!");
        } catch (Exception e) {
            // 출석체크 처리 중 에러가 발생한 경우 에러 응답을 전송
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("출석체크에 실패했습니다.");
        }
    }


    // 리워드 지급 가능 여부 확인 API
    @PostMapping("/reward/eligible")
    public ResponseEntity<?> checkRewardEligibility(@RequestBody Map<String, Object> requestData) {
        try {
            Long userId = Long.parseLong(requestData.get("userId").toString()); // 프론트엔드에서 전달된 userId 가져오기

            // 출석체크 리워드 지급 가능 여부 확인 서비스 메소드 호출
            boolean isEligible = attendanceService.isRewardEligible(userId);

            if (isEligible) {
                // 리워드 지급 가능한 경우 200 상태 코드와 메시지를 응답으로 전송
                return ResponseEntity.ok("Reward eligible.");
            } else {
                // 리워드 지급 불가능한 경우 403 상태 코드와 메시지를 응답으로 전송
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not eligible for reward.");
            }
        } catch (Exception e) {
            // 처리 중 에러가 발생한 경우 500 상태 코드와 에러 메시지를 응답으로 전송
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred while checking reward eligibility.");
        }
    }


    @GetMapping("/all")
    public ResponseEntity<List<AttendanceDTO>> getAllAttendance(Long userId) {
        //유저id 통해 출석 정보 조회
        List<Attendance> attendances = attendanceService.getAllAttendance(userId);
        //출석 정보를 dto로 변환
        List<AttendanceDTO> attendanceDTOs = attendances.stream()
                .map(this::mapAttendanceToDTO)
                .collect(Collectors.toList());
        //변환된 dto 목록을 응답으로 반환
        return ResponseEntity.ok(attendanceDTOs);
    }

    //Attendance 객체를 AttendanceDTO로 매핑하는 메서드
    private AttendanceDTO mapAttendanceToDTO(Attendance attendance) {
        AttendanceDTO attendanceDTO = new AttendanceDTO();
        attendanceDTO.setId(attendance.getId());
        attendanceDTO.setUserId(attendance.getUserId());
        attendanceDTO.setDate(attendance.getDate());
        return attendanceDTO;
    }



}





