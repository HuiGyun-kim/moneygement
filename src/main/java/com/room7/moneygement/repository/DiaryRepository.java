package com.room7.moneygement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.room7.moneygement.model.Diary;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    @Query("SELECT d FROM Diary d WHERE d.expenseAt >= :startDate AND d.expenseAt <= :endDate AND d.userId = :userId")
    List<Diary> findByExpenseAtAndUserId(LocalDateTime startDate, LocalDateTime endDate, Long userId);
}
