package com.room7.moneygement.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.room7.moneygement.dto.LedgerDTO;
import com.room7.moneygement.model.Ledger;
import com.room7.moneygement.model.User;
import com.room7.moneygement.repository.LedgerEntryRepository;
import com.room7.moneygement.repository.LedgerRepository;
import com.room7.moneygement.service.LedgerService;
import com.room7.moneygement.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LedgerServiceImpl implements LedgerService {
	private final LedgerRepository ledgerRepository;
	private final UserService userService;
	private final LedgerEntryRepository ledgerEntryRepository;

	@Override
	public Page<LedgerDTO> getLedgersByUser(Long userId, Pageable pageable) {
		User user = userService.findUserById(userId);
		Page<Ledger> ledgers = ledgerRepository.findByUserId(user, pageable);
		return ledgers.map(ledger -> new LedgerDTO(ledger.getLedgerId(), ledger.getTitle(), ledger.getCreatedAt()));
	}
	@Override
	public void saveLedger(Ledger ledger) {
		ledgerRepository.save(ledger);
	}

	@Override
	public Ledger getLedgerById(Long userId) {
		return ledgerRepository.findById(userId).orElse(null);
	}

	@Override
	public void deleteLedger(Long id) {
		ledgerRepository.deleteById(id);
	}
}

