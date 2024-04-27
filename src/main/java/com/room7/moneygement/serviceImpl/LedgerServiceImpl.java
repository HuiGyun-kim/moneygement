package com.room7.moneygement.serviceImpl;

import org.springframework.stereotype.Service;

import com.room7.moneygement.repository.LedgerRepository;
import com.room7.moneygement.service.LedgerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LedgerServiceImpl implements LedgerService {
	private final LedgerRepository ledgerRepository;

}

