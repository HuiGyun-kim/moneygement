package com.room7.moneygement.service;

import java.util.List;

import com.room7.moneygement.dto.LedgerEntryDTO;

public interface LedgerEntryService {
	List<LedgerEntryDTO> getEntriesByLedger(Long ledgerId);
	void addLedgerEntry(LedgerEntryDTO ledgerEntryDTO);
}

