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
	List<LedgerEntry> findByUserIdAndDateBetween(@Param("userId") Long userId, @Param("startDate") LocalDate startDate,
		@Param("endDate") LocalDate endDate);

	@Query("SELECT e FROM LedgerEntry e WHERE e.date = :date AND e.ledger.userId.userId = :userId")
	List<LedgerEntry> findByDateAndUserId(LocalDate date, Long userId);

	@Query("SELECT e FROM LedgerEntry e WHERE e.ledger.userId.userId = :userId AND e.date BETWEEN :startDate AND :endDate AND e.ledgerType = :ledgerType")
	List<LedgerEntry> findByUserIdAndDateBetweenAndLedgerType(Long userId, LocalDate startDate, LocalDate endDate,
		Boolean ledgerType);

	@Query("SELECT SUM(e.amount) FROM LedgerEntry e WHERE e.ledger.userId.userId = :userId AND e.ledgerType = :ledgerType AND MONTH(e.date) = :month AND YEAR(e.date) = :year")
	Long findTotalByUserIdAndType(@Param("userId") Long userId, @Param("ledgerType") Boolean ledgerType,
		@Param("year") int year, @Param("month") int month);

	@Query("SELECT SUM(e.amount) FROM LedgerEntry e WHERE e.ledger.userId.userId = :userId AND e.ledgerType = :ledgerType AND e.date BETWEEN :startDate AND :endDate")
	Long findTotalByUserIdAndTypeBetweenDates(@Param("userId") Long userId, @Param("ledgerType") Boolean ledgerType,
		@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	@Query("SELECT e FROM LedgerEntry e WHERE e.ledger.userId.userId = :userId AND e.date = :date AND e.ledgerType = :ledgerType")
	List<LedgerEntry> findByUserIdAndDateAndLedgerType(Long userId, LocalDate date, Boolean ledgerType);

	@Query("SELECT le FROM LedgerEntry le WHERE le.ledger.userId.userId = :userId")
	List<LedgerEntry> findByUserId(@Param("userId") Long userId);

	@Query("SELECT SUM(le.amount) FROM LedgerEntry le WHERE le.ledger.userId.userId = :userId AND le.category.categoryName = :categoryName AND YEAR(le.date) = :year AND MONTH(le.date) = :month")
	Long findTotalExpenseByUserIdAndCategoryAndYearAndMonth(@Param("userId") Long userId,@Param("categoryName") String categoryName,@Param("year") int year,@Param("month") int month	);

	@Query("SELECT AVG(expenseSum) FROM (SELECT SUM(le.amount) AS expenseSum FROM LedgerEntry le WHERE le.ledger.userId.userId = :userId AND le.ledgerType = true GROUP BY YEAR(le.date), MONTH(le.date) ORDER BY YEAR(le.date) DESC, MONTH(le.date) DESC LIMIT 3) subquery")
	Long findAverageMonthlyExpenseByUserId(@Param("userId") Long userId);

	@Query("SELECT c.categoryName FROM Category c WHERE c.categoryId = (SELECT le.category.categoryId FROM LedgerEntry le WHERE le.ledger.userId.userId = :userId AND le.ledgerType = true GROUP BY le.category.categoryId ORDER BY SUM(le.amount) DESC LIMIT 1)")
	String findMostExpensiveCategoryByUserId(@Param("userId") Long userId);

	@Query("SELECT COALESCE(AVG(expenseSum), 0) FROM (SELECT SUM(le.amount) AS expenseSum FROM LedgerEntry le WHERE le.ledger.userId.userId = :userId AND le.category.categoryName = :categoryName AND le.ledgerType = true GROUP BY YEAR(le.date), MONTH(le.date) ORDER BY YEAR(le.date) DESC, MONTH(le.date) DESC LIMIT 3) subquery")
	Long findAverageMonthlyCategoryExpenseByUserId(
		@Param("userId") Long userId,
		@Param("categoryName") String categoryName
	);
}

