package com.room7.moneygement.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.room7.moneygement.dto.LedgerDTO;
import com.room7.moneygement.model.Ledger;
import com.room7.moneygement.model.User;
import com.room7.moneygement.repository.LedgerRepository;
import com.room7.moneygement.repository.UserRepository;
import com.room7.moneygement.service.LedgerService;
import com.room7.moneygement.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LedgerServiceImpl implements LedgerService {
	private final LedgerRepository ledgerRepository;
	private final UserRepository userRepository;
	private final UserService userService;

	public List<LedgerDTO> getLedgersByUser(Long userId) {
		User user = userService.findUserById(userId);
		List<Ledger> ledgers = ledgerRepository.findByUserId(user);
		return ledgers.stream()
			.map(ledger -> new LedgerDTO(
				ledger.getLedgerId(),
				ledger.getTitle(),
				ledger.getCreatedAt()))
			.collect(Collectors.toList());
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

