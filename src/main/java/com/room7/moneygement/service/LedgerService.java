package com.room7.moneygement.service;

import java.util.List;

import com.room7.moneygement.dto.LedgerDTO;
import com.room7.moneygement.model.Ledger;

public interface LedgerService {
	List<LedgerDTO> getLedgersByUser(Long userId);
	void saveLedger(Ledger ledger);
	Ledger getLedgerById(Long id);
	void deleteLedger(Long id);
	List<Long> getLedgerIdsByUser(Long userId);
}

