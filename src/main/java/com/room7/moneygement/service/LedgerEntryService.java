package com.room7.moneygement.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.room7.moneygement.dto.LedgerEntryDTO;
import com.room7.moneygement.model.LedgerEntry;

public interface LedgerEntryService {
	void addLedgerEntry(LedgerEntryDTO ledgerEntryDTO);
	void updateEntry(LedgerEntry entry);
	void deleteEntry(Long id);
	LedgerEntry getEntryById(Long id);
	Page<LedgerEntryDTO> getEntriesByLedgerAndType(Long ledgerId, Boolean ledgerType, Pageable pageable);
	Map<String, Object> getSpendingReport(Long userId);
}

