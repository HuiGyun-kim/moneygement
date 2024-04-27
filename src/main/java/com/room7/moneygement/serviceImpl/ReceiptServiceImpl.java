package com.room7.moneygement.serviceImpl;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.room7.moneygement.repository.ReceiptRepository;
import com.room7.moneygement.service.ReceiptService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {
	private final ReceiptRepository receiptRepository;

}

