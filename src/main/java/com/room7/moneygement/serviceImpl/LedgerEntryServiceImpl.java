package com.room7.moneygement.serviceImpl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.room7.moneygement.dto.FinancialInfoDTO;
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

	public List<LedgerEntryDTO> getEntriesByLedgerAndType(Long ledgerId, Boolean ledgerType) {
		Ledger ledger = ledgerRepository.findById(ledgerId).orElse(null);
		if (ledger == null)
			return Collections.emptyList();

		List<LedgerEntry> entries = ledgerEntryRepository.findByLedger_ledgerIdAndLedgerType(ledgerId, ledgerType);
		return entries.stream()
			.map(entry -> new LedgerEntryDTO(
				entry.getEntryId(),
				entry.getLedger().getLedgerId(),
				entry.getCategory().getCategoryId(),
				entry.getCategory().getCategoryName(),
				entry.getReceiptId(),
				entry.getCreateAt(),
				entry.getAmount(),
				entry.getDate(),
				entry.getDescription()))
			.collect(Collectors.toList());
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
	public List<LedgerEntryDTO> getMonthlyIncomeSummary(Long ledgerId, int year, int month) {
		return ledgerEntryRepository.findIncomeSummaryByMonthAndYearAndLedger(ledgerId, year, month);
	}
	@Override
	public List<LedgerEntryDTO> getMonthlyExpenseSummary(Long ledgerId, int year, int month) {
		return ledgerEntryRepository.findExpenseSummaryByMonthAndYearAndLedger(ledgerId, year, month);
	}

	private long calculateAverageMonthlyExpense(Long ledgerId, int year) {
		long totalExpense = 0;
		int monthCount = 12; // 예시로 12월까지 계산
		for (int month = 1; month <= monthCount; month++) {
			Long monthlyExpense = ledgerEntryRepository.findTotalByLedgerAndType(ledgerId, true, year, month).longValue();
			totalExpense += monthlyExpense;
		}
		return totalExpense / monthCount;
	}

	public FinancialInfoDTO calculateFinancialInfo(Long ledgerId, int year, int month) {
		List<LedgerEntryDTO> incomes = ledgerEntryRepository.findIncomeSummaryByMonthAndYearAndLedger(ledgerId, year, month);
		List<LedgerEntryDTO> expenses = ledgerEntryRepository.findExpenseSummaryByMonthAndYearAndLedger(ledgerId, year, month);

		long totalIncome = incomes.stream()
			.mapToLong(LedgerEntryDTO::getAmount)
			.sum();
		long totalExpense = expenses.stream()
			.mapToLong(LedgerEntryDTO::getAmount)
			.sum();

		long netAssets = totalIncome - totalExpense;
		long averageMonthlyExpense = calculateAverageMonthlyExpense(ledgerId, year);
		Long lastMonthExpense = ledgerEntryRepository.findTotalByLedgerAndType(ledgerId, true, year, month - 1).longValue();
		double expenseChange = lastMonthExpense != 0 ?
			(double) (totalExpense - lastMonthExpense) / lastMonthExpense * 100 :
			0;

		return new FinancialInfoDTO(
			BigDecimal.valueOf(totalIncome),
			BigDecimal.valueOf(totalExpense),
			BigDecimal.valueOf(netAssets),
			BigDecimal.valueOf(averageMonthlyExpense),
			BigDecimal.valueOf(expenseChange)
		);
	}
}
