package com.room7.moneygement.controller;

import com.room7.moneygement.model.LedgerEntry;
import com.room7.moneygement.repository.LedgerEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.room7.moneygement.service.LedgerEntryService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ledgerEntry")
public class LedgerEntryController {

	@Autowired
	private final LedgerEntryRepository ledgerEntryRepository;

	@GetMapping("/expenses")
	public List<LedgerEntry> getExpense(@RequestParam("date")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
		return ledgerEntryRepository.findByDate(date);
	}
}