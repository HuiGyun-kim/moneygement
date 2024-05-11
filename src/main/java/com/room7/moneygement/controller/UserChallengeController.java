package com.room7.moneygement.controller;

import com.room7.moneygement.model.LedgerEntry;
import com.room7.moneygement.model.UserChallenge;
import com.room7.moneygement.repository.LedgerEntryRepository;
import com.room7.moneygement.repository.UserChallengeRepository;
import com.room7.moneygement.service.CustomUserDetails;
import com.room7.moneygement.serviceImpl.UserChallengeServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userChallenges")
public class UserChallengeController {

	private final UserChallengeRepository userChallengeRepository;
	private final UserChallengeServiceImpl userChallengeServiceImpl;
	private final LedgerEntryRepository ledgerEntryRepository;

	@PostMapping("/addUserChallenge")
	public ResponseEntity<?> addUserChallenge(@RequestBody Map<String, Object> payload,
											  @AuthenticationPrincipal CustomUserDetails userDetails) {
		Long targetAmount = Long.parseLong(payload.get("targetAmount").toString());
		Long userId = userDetails.getUser().getUserId();
		UserChallenge userChallenge = new UserChallenge();
		userChallenge.setUserId(userId);
		userChallenge.setChallengeId(1L);
		userChallenge.setTargetAmount(targetAmount);
		userChallenge.setJoinDate(LocalDateTime.now());
		userChallenge.setIsCompleted(false);

		userChallengeRepository.save(userChallenge);

		Map<String, String> response = new HashMap<>();
		response.put("message", "success");
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/displayTarget/{userId}")
	public ResponseEntity<?> getUserChallenge(@PathVariable Long userId){
		Long targetAmount = userChallengeServiceImpl.getLatestTargetAmountByUserId(userId);

		if (targetAmount != null){
			return ResponseEntity.ok(targetAmount);
		}
		else{
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/displayExpense/{userId}")
	public ResponseEntity<?> getMonthlyExpenses(@PathVariable Long userId) {
		LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), 1);
		LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

		List<LedgerEntry> expenses = ledgerEntryRepository.findByUserIdAndDateBetweenAndLedgerType(
				userId, startDate, endDate, true);

		long totalExpenses = expenses.stream()
				.mapToLong(LedgerEntry::getAmount)
				.sum();


		return ResponseEntity.ok(totalExpenses);
	}
	@GetMapping("/isJoinDate/{userId}")
	public ResponseEntity<Boolean> isJoinDate(@PathVariable Long userId) {
		LocalDate today = LocalDate.now();
		LocalDateTime startDateTime = today.withDayOfMonth(1).atStartOfDay();
		LocalDateTime endDateTime = today.withDayOfMonth(today.lengthOfMonth()).atTime(23, 59, 59, 999999999);
		boolean hasJoined = userChallengeRepository.existsByUserIdAndJoinDateBetween(userId, startDateTime, endDateTime);
		return ResponseEntity.ok(hasJoined);
	}
}
