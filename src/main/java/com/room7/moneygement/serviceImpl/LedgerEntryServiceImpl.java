package com.room7.moneygement.serviceImpl;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.room7.moneygement.repository.LedgerEntryRepository;
import com.room7.moneygement.service.LedgerEntryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LedgerEntryServiceImpl implements LedgerEntryService {
	private final LedgerEntryRepository ledgerEntryRepository;

}

