package com.room7.moneygement.serviceImpl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
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
}
