package com.room7.moneygement.service;

import java.util.List;

import com.room7.moneygement.dto.LedgerEntryDTO;
import com.room7.moneygement.model.LedgerEntry;

public interface LedgerEntryService {
	void addLedgerEntry(LedgerEntryDTO ledgerEntryDTO);
	void updateEntry(LedgerEntry entry);
	void deleteEntry(Long id);
	LedgerEntry getEntryById(Long id);
	List<LedgerEntryDTO> getMonthlyIncomeSummary(Long ledgerId, int year, int month);
	List<LedgerEntryDTO> getMonthlyExpenseSummary(Long ledgerId, int year, int month);
}

