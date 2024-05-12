package com.room7.moneygement.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.room7.moneygement.dto.LedgerDTO;
import com.room7.moneygement.model.Ledger;

public interface LedgerService {
	Page<LedgerDTO> getLedgersByUser(Long userId, Pageable pageable);
	void saveLedger(Ledger ledger);
	Ledger getLedgerById(Long id);
	void deleteLedger(Long id);
}

