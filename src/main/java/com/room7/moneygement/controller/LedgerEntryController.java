package com.room7.moneygement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.room7.moneygement.dto.LedgerEntryDTO;
import com.room7.moneygement.service.LedgerEntryService;
import com.room7.moneygement.service.LedgerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ledgerEntry")
public class LedgerEntryController {
	private final LedgerEntryService ledgerEntryService;
	private final LedgerService ledgerService;

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