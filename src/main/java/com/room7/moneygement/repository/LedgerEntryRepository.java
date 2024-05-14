package com.room7.moneygement.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.room7.moneygement.dto.LedgerEntryDTO;
import com.room7.moneygement.model.Ledger;
import com.room7.moneygement.model.LedgerEntry;
import java.time.LocalDate;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {

    Page<LedgerEntry> findByLedgerAndLedgerTypeOrderByDateDesc(Ledger ledger, Boolean ledgerType, Pageable pageable);

    @Query("SELECT le FROM LedgerEntry le WHERE le.ledger.userId.userId = :userId AND le.date BETWEEN :startDate AND :endDate")
    List<LedgerEntry> findByUserIdAndDateBetween(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(e.amount) FROM LedgerEntry e WHERE e.ledger.ledgerId = :ledgerId AND e.ledgerType = :ledgerType AND MONTH(e.date) = :month AND YEAR(e.date) = :year")
    Long findTotalByLedgerAndType(@Param("ledgerId") Long ledgerId, @Param("ledgerType") Boolean ledgerType, @Param("year") int year, @Param("month") int month);

    @Query("SELECT e FROM LedgerEntry e WHERE e.date = :date AND e.ledger.userId.userId = :userId")
    List<LedgerEntry> findByDateAndUserId(LocalDate date, Long userId);

    @Query("SELECT e FROM LedgerEntry e WHERE e.ledger.userId.userId = :userId AND e.date BETWEEN :startDate AND :endDate AND e.ledgerType = :ledgerType")
    List<LedgerEntry> findByUserIdAndDateBetweenAndLedgerType(Long userId, LocalDate startDate, LocalDate endDate, Boolean ledgerType);

    @Query("SELECT SUM(e.amount) FROM LedgerEntry e WHERE e.ledger.userId.userId = :userId AND e.ledgerType = :ledgerType AND e.date BETWEEN :startDate AND :endDate")
    Long findTotalByLedgerAndTypeBetweenDates(@Param("userId") Long userId, @Param("ledgerType") Boolean ledgerType, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}

