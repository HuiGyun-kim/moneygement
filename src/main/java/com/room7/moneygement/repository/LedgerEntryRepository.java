package com.room7.moneygement.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.room7.moneygement.dto.LedgerEntryDTO;
import com.room7.moneygement.model.LedgerEntry;
import java.time.LocalDate;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {

    List<LedgerEntry> findByDate(LocalDate date);
    List<LedgerEntry> findByLedger_ledgerIdAndLedgerType(Long ledgerId, Boolean ledgerType);

    @Query("SELECT new com.room7.moneygement.dto.LedgerEntryDTO(e.date, SUM(e.amount), MIN(e.description), MIN(e.category.categoryName), e.ledgerType) FROM LedgerEntry e WHERE e.ledgerType = false AND e.ledger.ledgerId = :ledgerId AND MONTH(e.date) = :month AND YEAR(e.date) = :year GROUP BY e.date ORDER BY e.date")
    List<LedgerEntryDTO> findIncomeSummaryByMonthAndYearAndLedger(@Param("ledgerId") Long ledgerId, @Param("year") int year, @Param("month") int month);

    @Query("SELECT new com.room7.moneygement.dto.LedgerEntryDTO(e.date, SUM(e.amount), MIN(e.description), MIN(e.category.categoryName), e.ledgerType) FROM LedgerEntry e WHERE e.ledgerType = true AND e.ledger.ledgerId = :ledgerId AND MONTH(e.date) = :month AND YEAR(e.date) = :year GROUP BY e.date ORDER BY e.date")
    List<LedgerEntryDTO> findExpenseSummaryByMonthAndYearAndLedger(@Param("ledgerId") Long ledgerId, @Param("year") int year, @Param("month") int month);

    @Query("SELECT SUM(e.amount) FROM LedgerEntry e WHERE e.ledger.ledgerId = :ledgerId AND e.ledgerType = :ledgerType AND MONTH(e.date) = :month AND YEAR(e.date) = :year")
    BigDecimal findTotalByLedgerAndType(@Param("ledgerId") Long ledgerId, @Param("ledgerType") Boolean ledgerType, @Param("year") int year, @Param("month") int month);

    @Query("SELECT le FROM LedgerEntry le WHERE le.date = :date AND le.ledger.userId.userId = :userId")
    List<LedgerEntry> findByDateAndUserId(LocalDate date, Long userId);

}

