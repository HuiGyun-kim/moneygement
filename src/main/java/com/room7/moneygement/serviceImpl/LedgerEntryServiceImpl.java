package com.room7.moneygement.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.room7.moneygement.dto.LedgerEntryDTO;
import com.room7.moneygement.model.Category;
import com.room7.moneygement.model.Ledger;
import com.room7.moneygement.model.LedgerEntry;
import com.room7.moneygement.repository.CategoryRepository;
import com.room7.moneygement.repository.LedgerEntryRepository;
import com.room7.moneygement.repository.LedgerRepository;
import com.room7.moneygement.service.LedgerEntryService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LedgerEntryServiceImpl implements LedgerEntryService {
	private final LedgerEntryRepository ledgerEntryRepository;
	private final LedgerRepository ledgerRepository;
	private final CategoryRepository categoryRepository;

	@Override
	public Page<LedgerEntryDTO> getEntriesByLedgerAndType(Long ledgerId, Boolean ledgerType, Pageable pageable) {
		Ledger ledger = ledgerRepository.findById(ledgerId).orElse(null);
		if (ledger == null) {
			return new PageImpl<>(Collections.emptyList());
		}

		Page<LedgerEntry> entries = ledgerEntryRepository.findByLedgerAndLedgerTypeOrderByDateDesc(ledger, ledgerType, pageable);
		return entries.map(entry -> new LedgerEntryDTO(
			entry.getEntryId(),
			entry.getLedger().getLedgerId(),
			entry.getCategory().getCategoryId(),
			entry.getCategory().getCategoryName(),
			entry.getReceiptId(),
			entry.getCreateAt(),
			entry.getAmount(),
			entry.getDate(),
			entry.getDescription()));
	}

	@Override
	public void addLedgerEntry(LedgerEntryDTO ledgerEntryDTO) {
		Long ledgerId = ledgerEntryDTO.getLedgerId();
		if (ledgerId == null) {
			throw new IllegalArgumentException("Ledger ID is required");
		}

		Ledger ledger = ledgerRepository.findById(ledgerId)
			.orElseThrow(() -> new EntityNotFoundException("Ledger not found"));

		LedgerEntry ledgerEntry = new LedgerEntry(
			ledger,
			ledgerEntryDTO.getAmount(),
			ledgerEntryDTO.getDate(),
			ledgerEntryDTO.getDescription(),
			ledgerEntryDTO.isLedgerType()
		);

		if (ledgerEntryDTO.getCategoryId() != null) {
			Category category = categoryRepository.findById(ledgerEntryDTO.getCategoryId())
				.orElseThrow(() -> new EntityNotFoundException("Category not found"));
			ledgerEntry.setCategory(category); // 직접 Category 객체를 설정
		}

		ledgerEntryRepository.save(ledgerEntry);
	}

	@Override
	public void updateEntry(LedgerEntry entry) {
		// 업데이트할 내역의 ID가 있어야 합니다.
		if (entry.getEntryId() == null) {
			throw new IllegalArgumentException("ID가 없는 항목은 업데이트할 수 없습니다.");
		}
		// 존재하면 업데이트
		ledgerEntryRepository.save(entry);
	}

	@Override
	public void deleteEntry(Long id) {
		ledgerEntryRepository.deleteById(id);
	}

	@Override
	public LedgerEntry getEntryById(Long id) {
		return ledgerEntryRepository.findById(id).orElse(null);
	}

	@Override
	public Map<String, Object> getSpendingReport(Long userId) {
		LocalDate now = LocalDate.now();
		int currentYear = now.getYear();
		int currentMonth = now.getMonthValue();

		Long income = ledgerEntryRepository.findTotalByLedgerAndType(userId, false, currentYear, currentMonth);
		Long expense = ledgerEntryRepository.findTotalByLedgerAndType(userId, true, currentYear, currentMonth);

		LocalDate threeMonthsAgo = now.minusMonths(3);
		int year3 = threeMonthsAgo.getYear();
		int month3 = threeMonthsAgo.getMonthValue();
		Long incomeThreeMonthsAgo = ledgerEntryRepository.findTotalByLedgerAndType(userId, false, year3, month3);
		Long expenseThreeMonthsAgo = ledgerEntryRepository.findTotalByLedgerAndType(userId, true, year3, month3);

		// 전달 지출 계산
		LocalDate lastMonth = now.minusMonths(1);
		int lastMonthYear = lastMonth.getYear();
		int lastMonthValue = lastMonth.getMonthValue();
		Long lastMonthExpense = ledgerEntryRepository.findTotalByLedgerAndType(userId, true, lastMonthYear,
			lastMonthValue);
		// 전달 수입 계산
		Long lastMonthIncome = ledgerEntryRepository.findTotalByLedgerAndType(userId, false, lastMonthYear,
			lastMonthValue);

		// 이번 달 현재까지의 지출 계산
		LocalDate startOfMonth = LocalDate.of(currentYear, currentMonth, 1);
		Long expenseThisMonth = ledgerEntryRepository.findTotalByLedgerAndTypeBetweenDates(userId, true, startOfMonth,
			now);

		// 전달 대비 이번 달의 지출 비교
		Long expenseDifference =
			(lastMonthExpense != null ? lastMonthExpense : 0L) - (expenseThisMonth != null ? expenseThisMonth : 0L);
		// 이번 달 현재까지의 수입 계산
		Long incomeThisMonth = ledgerEntryRepository.findTotalByLedgerAndTypeBetweenDates(userId, false, startOfMonth,
			now);

		List<Map<String, Object>> monthlyData = new ArrayList<>();
		for (int i = 11; i >= 0; i--) {
			LocalDate targetDate = now.minusMonths(i);
			int year = targetDate.getYear();
			int month = targetDate.getMonthValue();
			Long monthlyIncome = ledgerEntryRepository.findTotalByLedgerAndType(userId, false, year, month);
			Long monthlyExpense = ledgerEntryRepository.findTotalByLedgerAndType(userId, true, year, month);

			Map<String, Object> data = new HashMap<>();
			data.put("year", year);
			data.put("month", month);
			data.put("income", monthlyIncome != null ? monthlyIncome : 0L);
			data.put("expense", monthlyExpense != null ? monthlyExpense : 0L);
			monthlyData.add(data);
		}

		Map<String, Object> report = new HashMap<>();
		report.put("income", income != null ? income : 0L);
		report.put("expense", expense != null ? expense : 0L);
		report.put("incomeThreeMonthsAgo", incomeThreeMonthsAgo != null ? incomeThreeMonthsAgo : 0L);
		report.put("expenseThreeMonthsAgo", expenseThreeMonthsAgo != null ? expenseThreeMonthsAgo : 0L);
		report.put("monthlyData", monthlyData);
		report.put("expenseDifference", expenseDifference);
		report.put("lastMonthIncome", lastMonthIncome != null ? lastMonthIncome : 0L);
		report.put("lastMonthExpense", lastMonthExpense != null ? lastMonthExpense : 0L);

		return report;
	}
}
