package com.room7.moneygement.controller;

import com.room7.moneygement.dto.CategoryDTO;
import com.room7.moneygement.dto.FinancialInfoDTO;
import com.room7.moneygement.model.Category;
import com.room7.moneygement.model.LedgerEntry;
import com.room7.moneygement.repository.CategoryRepository;
import com.room7.moneygement.repository.LedgerEntryRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.room7.moneygement.dto.LedgerEntryDTO;
import com.room7.moneygement.service.CategoryService;
import com.room7.moneygement.service.LedgerEntryService;
import com.room7.moneygement.serviceImpl.LedgerEntryServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


import java.time.LocalDate;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ledgerEntry")
public class LedgerEntryController {

	private final LedgerEntryRepository ledgerEntryRepository;
	private final LedgerEntryService ledgerEntryService;
	private final CategoryService categoryService;
	private final CategoryRepository categoryRepository;
	private final LedgerEntryServiceImpl ledgerEntryServiceImpl;

	@GetMapping("/expenses")
	public ResponseEntity<?> getExpense(@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam("userId") Long userId){
		try {
			List<LedgerEntry> entries = ledgerEntryRepository.findByDateAndUserId(date, userId);
			return ResponseEntity.ok(entries);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving expenses: " + e.getMessage());
		}
	}

	@GetMapping("/entries")
	public ResponseEntity<List<LedgerEntryDTO>> getEntries(
		@RequestParam Long ledgerId,
		@RequestParam(required = false, defaultValue = "false") Boolean ledgerType) {
		return ResponseEntity.ok(ledgerEntryServiceImpl.getEntriesByLedgerAndType(ledgerId, ledgerType));
	}

	@PostMapping("/add")
	public ResponseEntity<?> addLedgerEntry(@RequestBody LedgerEntryDTO ledgerEntryDTO) {
		ledgerEntryService.addLedgerEntry(ledgerEntryDTO);
		return ResponseEntity.ok(Map.of("message", "Entry added successfully"));
	}

	@PutMapping("/edit/{id}")
	public ResponseEntity<?> editEntry(@PathVariable Long id, @RequestBody LedgerEntryDTO entryDTO) {
		LedgerEntry entry = ledgerEntryService.getEntryById(id);
		if (entry == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entry not found");
		}
		if (entryDTO.getCategoryId() != null) {
			Category category = categoryRepository.findById(entryDTO.getCategoryId())
				.orElseThrow(() -> new EntityNotFoundException("Category not found"));
			entry.setCategory(category);
		}
		// DTO에서 데이터를 가져와 LedgerEntry 객체를 업데이트
		entry.setAmount(entryDTO.getAmount());
		entry.setDate(entryDTO.getDate());
		entry.setDescription(entryDTO.getDescription());

		ledgerEntryService.updateEntry(entry);
		return ResponseEntity.ok("Entry updated successfully");
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteEntry(@PathVariable Long id) {
		// id로 데이터베이스에서 항목을 찾고 삭제하는 서비스 메서드 호출
		ledgerEntryService.deleteEntry(id);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/categories")
	public ResponseEntity<List<CategoryDTO>> getAllCategories() {
		List<CategoryDTO> categories = categoryService.getAllCategories();
		return ResponseEntity.ok(categories);
	}
	@PostMapping("/addExpense")
	public ResponseEntity<?> addExpenseEntry(@RequestBody LedgerEntryDTO ledgerEntryDTO) {
		ledgerEntryService.addLedgerEntry(ledgerEntryDTO);
		return ResponseEntity.ok(Map.of("message", "Expense entry added successfully"));
	}
	@GetMapping("/monthly-income-summary")
	public ResponseEntity<List<LedgerEntryDTO>> getMonthlyIncomeSummary(@RequestParam Long ledgerId, @RequestParam int year, @RequestParam int month) {
		List<LedgerEntryDTO> incomeSummary = ledgerEntryService.getMonthlyIncomeSummary(ledgerId, year, month);
		return ResponseEntity.ok(incomeSummary);
	}

	@GetMapping("/monthly-expense-summary")
	public ResponseEntity<List<LedgerEntryDTO>> getMonthlyExpenseSummary(@RequestParam Long ledgerId, @RequestParam int year, @RequestParam int month) {
		List<LedgerEntryDTO> expenseSummary = ledgerEntryService.getMonthlyExpenseSummary(ledgerId, year, month);
		return ResponseEntity.ok(expenseSummary);
	}
	@GetMapping("/financial-info")
	public ResponseEntity<FinancialInfoDTO> getFinancialInfo(@RequestParam Long ledgerId, @RequestParam int year, @RequestParam int month) {
		FinancialInfoDTO financialInfo = ledgerEntryService.calculateFinancialInfo(ledgerId, year, month);
		return ResponseEntity.ok(financialInfo);
	}
}