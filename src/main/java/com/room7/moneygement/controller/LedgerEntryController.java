package com.room7.moneygement.controller;

import com.room7.moneygement.dto.CategoryDTO;
import com.room7.moneygement.model.Category;
import com.room7.moneygement.model.LedgerEntry;
import com.room7.moneygement.repository.CategoryRepository;
import com.room7.moneygement.repository.LedgerEntryRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.room7.moneygement.dto.LedgerEntryDTO;
import com.room7.moneygement.service.CategoryService;
import com.room7.moneygement.service.CustomUserDetails;
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
	public ResponseEntity<Map<String, Object>> getEntries(
		@RequestParam Long ledgerId,
		@RequestParam(required = false, defaultValue = "false") Boolean ledgerType,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<LedgerEntryDTO> entries = ledgerEntryService.getEntriesByLedgerAndType(ledgerId, ledgerType, pageable);

		Map<String, Object> response = new HashMap<>();
		response.put("content", entries.getContent());
		response.put("number", entries.getNumber());
		response.put("totalPages", entries.getTotalPages());
		response.put("hasPrevious", entries.hasPrevious());
		response.put("hasNext", entries.hasNext());

		return ResponseEntity.ok(response);
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
	@GetMapping("/entriesAll")
	public ResponseEntity<?> getEntriesByUserAndDate(
		@RequestParam("userId") Long userId,
		@RequestParam("year") int year,
		@RequestParam("month") int month
	) {
		try {
			LocalDate startDate = LocalDate.of(year, month, 1);
			LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

			List<LedgerEntry> entries = ledgerEntryRepository.findByUserIdAndDateBetween(userId, startDate, endDate);
			return ResponseEntity.ok(entries);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving entries: " + e.getMessage());
		}
	}
	@GetMapping("/fortuneRequest")
	public ResponseEntity<String> diaryRequestProxy(@RequestParam Map<String, String> allParams) {
		RestTemplate restTemplate = new RestTemplate();
		String baseUrl = "https://kdt-api-function.azurewebsites.net/api/v1/question";
		String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
			.queryParam("content", allParams.get("content"))
			.queryParam("client_id", allParams.get("client_id"))
			.toUriString();
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		return ResponseEntity.ok()
			.headers(response.getHeaders())
			.body(response.getBody());
	}
	@GetMapping("/spendingReport")
	public ResponseEntity<Map<String, Object>> getSpendingReport(@AuthenticationPrincipal CustomUserDetails currentUser) {
		if (currentUser != null) {
			Long userId = currentUser.getUser().getUserId();
			Map<String, Object> report = ledgerEntryService.getSpendingReport(userId);
			return ResponseEntity.ok(report);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}