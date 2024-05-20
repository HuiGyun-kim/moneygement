package com.room7.moneygement.dto;

import static org.junit.jupiter.api.Assertions.*;
import com.room7.moneygement.dto.AttendanceDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


class AttendanceDTOTest {
    @Test
    void testAttendanceDTO() {
        Long id = 1L;
        Long userId = 123L;
        LocalDate date = LocalDate.now();
        boolean attended = true;

        AttendanceDTO attendanceDTO = new AttendanceDTO();
        attendanceDTO.setId(id);
        attendanceDTO.setUserId(userId);
        attendanceDTO.setDate(date);
        attendanceDTO.setAttended(attended);

        assertEquals(id, attendanceDTO.getId());
        assertEquals(userId, attendanceDTO.getUserId());
        assertEquals(date, attendanceDTO.getDate());
        assertEquals(attended, attendanceDTO.isAttended());
    }
}