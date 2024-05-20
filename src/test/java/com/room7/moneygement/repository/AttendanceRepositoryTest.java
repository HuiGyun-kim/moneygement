package com.room7.moneygement.repository;

import static org.junit.jupiter.api.Assertions.*;
import com.room7.moneygement.model.Attendance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
class AttendanceRepositoryTest {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindByUserIdAndDateBetween() {

        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        Long userId = 1L;

        List<Attendance> result = attendanceRepository.findByUserIdAndDateBetween(userId, startDate, endDate);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}