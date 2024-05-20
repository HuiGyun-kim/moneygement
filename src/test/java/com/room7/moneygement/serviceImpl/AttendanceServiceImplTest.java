package com.room7.moneygement.serviceImpl;

import com.room7.moneygement.repository.AttendanceRepository;
import org.springframework.boot.test.context.SpringBootTest;
import com.room7.moneygement.model.Attendance;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AttendanceServiceImplTest {
    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private AttendanceServiceImpl attendanceService;

    @Test
    void testCheckAttendance() {
        // Given
        Long userId = 1L;
        LocalDate date = LocalDate.now();

        // When
        attendanceService.checkAttendance(userId, date);

        // Then
        // Verify that attendanceRepository's save method was called with correct parameters
        verify(attendanceRepository, times(1)).save(any(Attendance.class));
    }

    @Test
    void testIsRewardEligible() {
        // Given
        Long userId = 1L;
        List<Attendance> attendanceList = new ArrayList<>();
        // Assuming retrieval of attendance list from repository
        when(attendanceRepository.findByUserIdAndDateBetween(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(attendanceList);

        // When
        boolean result = attendanceService.isRewardEligible(userId);

        // Then
        assertFalse(result);
    }

    @Test
    void testGetAllAttendance() {
        // Given
        Long userId = 1L;
        List<Attendance> attendanceList = new ArrayList<>();
        // Assuming retrieval of attendance list from repository
        when(attendanceRepository.findByUserId(userId)).thenReturn(attendanceList);

        // When
        List<Attendance> result = attendanceService.getAllAttendance(userId);

        // Then
        assertEquals(attendanceList, result);
    }

    @Test
    void testGetAttendanceCount() {
        // Given
        Long userId = 1L;
        int count = 10;
        // Assuming retrieval of count from repository
        when(attendanceRepository.countByUserId(userId)).thenReturn(count);

        // When
        int result = attendanceService.getAttendanceCount(userId);

        // Then
        assertEquals(count, result);
    }
}