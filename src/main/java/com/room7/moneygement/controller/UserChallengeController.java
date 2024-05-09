package com.room7.moneygement.controller;

import com.room7.moneygement.model.UserChallenge;
import com.room7.moneygement.repository.UserChallengeRepository;
import com.room7.moneygement.service.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userChallenges")
public class UserChallengeController {

	private final UserChallengeRepository userChallengeRepository;

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
}
