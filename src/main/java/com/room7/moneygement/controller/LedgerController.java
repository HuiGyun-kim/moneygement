package com.room7.moneygement.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.room7.moneygement.dto.LedgerDTO;
import com.room7.moneygement.model.Ledger;
import com.room7.moneygement.model.User;
import com.room7.moneygement.service.CustomUserDetails;
import com.room7.moneygement.service.LedgerService;
import com.room7.moneygement.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ledgers")
public class LedgerController {

	private final LedgerService ledgerService;
	private final UserService userService;

	@GetMapping("/ledger")
	public String showLedgerPage(@RequestParam(defaultValue = "0") int page, Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			if (authentication.getPrincipal() instanceof CustomUserDetails) {
				CustomUserDetails userDetails = (CustomUserDetails)authentication.getPrincipal();
				Long userId = userDetails.getUserId();
				Pageable pageable = PageRequest.of(page, 5); // 페이지당 5개씩 보여주도록 설정
				Page<LedgerDTO> ledgers = ledgerService.getLedgersByUser(userId, pageable);
				model.addAttribute("ledgers", ledgers);
			}
		}
		return "layout/ledger";
	}
	@GetMapping("/create")
	public String createLedgerForm(Model model) {
		model.addAttribute("ledger", new Ledger());
		return "layout/create";
	}

	@PostMapping("/create")
	public String createLedger(@ModelAttribute Ledger ledger) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			User user = userService.findUserById(userDetails.getUserId()); // User 객체 검색
			ledger.setUserId(user);
		}
		ledgerService.saveLedger(ledger);
		return "redirect:/ledgers/ledger";
	}

	@GetMapping("/edit/{id}")
	public String editLedgerForm(@PathVariable Long id, Model model) {
		Ledger ledger = ledgerService.getLedgerById(id);
		model.addAttribute("ledger", ledger);
		return "layout/edit";
	}

	@PostMapping("/edit")
	public String editLedger(@ModelAttribute Ledger ledger) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
			CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
			User user = userService.findUserById(userDetails.getUserId()); // User 객체 검색
			ledger.setUserId(user);
		}
		ledgerService.saveLedger(ledger);
		return "redirect:/ledgers/ledger";
	}

	@PostMapping("/delete/{id}")
	public String deleteLedger(@PathVariable Long id) {
		ledgerService.deleteLedger(id);
		return "redirect:/ledgers/ledger";
	}
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<LedgerDTO>> getLedgersByUser(@PathVariable Long userId, @RequestParam(defaultValue = "0") int page) {
		Pageable pageable = PageRequest.of(page, 5); // 페이지당 5개씩 보여주도록 설정
		Page<LedgerDTO> ledgers = ledgerService.getLedgersByUser(userId, pageable);
		return ResponseEntity.ok(ledgers.getContent());
	}
}