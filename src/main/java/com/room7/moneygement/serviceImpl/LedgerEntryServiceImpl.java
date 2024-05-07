package com.room7.moneygement.serviceImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.room7.moneygement.dto.LedgerEntryDTO;
import com.room7.moneygement.model.Ledger;
import com.room7.moneygement.model.LedgerEntry;
import com.room7.moneygement.repository.LedgerEntryRepository;
import com.room7.moneygement.repository.LedgerRepository;
import com.room7.moneygement.service.LedgerEntryService;
import com.room7.moneygement.service.LedgerService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LedgerEntryServiceImpl implements LedgerEntryService {
	private final LedgerEntryRepository ledgerEntryRepository;
	private final LedgerRepository ledgerRepository;
	private final LedgerService ledgerService;

	@Override
	public List<LedgerEntryDTO> getEntriesByLedger(Long ledgerId) {
		Ledger ledger = ledgerRepository.findById(ledgerId).orElse(null);
		if (ledger == null)
			return Collections.emptyList();

		List<LedgerEntry> entries = ledger.getEntries(); // Ledger 엔터티에 연관된 엔트리들을 가져옵니다.
		return entries.stream()
			.map(entry -> new LedgerEntryDTO(
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

		ledgerEntryRepository.save(ledgerEntry);
	}
}
