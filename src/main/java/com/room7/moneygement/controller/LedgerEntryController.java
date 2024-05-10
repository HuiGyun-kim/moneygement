package com.room7.moneygement.controller;


import com.room7.moneygement.model.LedgerEntry;
import com.room7.moneygement.repository.LedgerEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.room7.moneygement.dto.LedgerEntryDTO;
import com.room7.moneygement.service.LedgerEntryService;
import com.room7.moneygement.service.LedgerService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ledgerEntry")
public class LedgerEntryController {

	private final LedgerEntryRepository ledgerEntryRepository;
  private final LedgerEntryService ledgerEntryService;
	private final LedgerService ledgerService;
  
//	@GetMapping("/expenses")
//	public List<LedgerEntry> getExpense(@RequestParam("date")
//	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
//		return ledgerEntryRepository.findByDate(date);
//  }

	@GetMapping("/expenses")
	public List<LedgerEntry> getExpense(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam("userId") Long userId){
		return ledgerEntryRepository.findByDateAndUserId(date, userId);
	}

	@GetMapping("/entries")
	public ResponseEntity<List<LedgerEntryDTO>> getEntries(@RequestParam Long ledgerId) {
		return ResponseEntity.ok(ledgerEntryService.getEntriesByLedger(ledgerId));
	}

	@PostMapping("/add")
	public ResponseEntity<?> addLedgerEntry(@RequestBody LedgerEntryDTO ledgerEntryDTO) {
		ledgerEntryService.addLedgerEntry(ledgerEntryDTO);
		return ResponseEntity.ok("Entry added successfully");
	}
}