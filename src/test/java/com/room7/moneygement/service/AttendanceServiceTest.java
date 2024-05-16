package com.room7.moneygement.service;

import static org.junit.jupiter.api.Assertions.*;
import com.room7.moneygement.model.Attendance;
import com.room7.moneygement.repository.AttendanceRepository;
import com.room7.moneygement.serviceImpl.AttendanceServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest
class AttendanceServiceTest {
    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceServiceImpl attendanceService;

    @Test
    void testIsRewardEligible() {
        Long userId = 1L;

        boolean result = attendanceService.isRewardEligible(userId);

    }

    @Test
    void testGetAllAttendance() {
        Long userId = 1L;
        List<Attendance> attendanceList = new ArrayList<>();
        when(attendanceRepository.findByUserId(userId)).thenReturn(attendanceList);

        List<Attendance> result = attendanceService.getAllAttendance(userId);

        assertEquals(attendanceList, result);
    }

    @Test
    void testCheckAttendance() {
        Long userId = 1L;
        LocalDate date = LocalDate.now();

        attendanceService.checkAttendance(userId, date);
    }
}
