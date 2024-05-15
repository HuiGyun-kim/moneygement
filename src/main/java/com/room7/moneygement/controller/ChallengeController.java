package com.room7.moneygement.controller;

import com.room7.moneygement.model.Challenge;
import com.room7.moneygement.repository.ChallengeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.room7.moneygement.service.ChallengeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/challenges")
public class ChallengeController {

	private final ChallengeService challengeService;
	private final ChallengeRepository challengeRepository;

	@Scheduled(cron = "0 0 0 1 * ?")
	public void createNewChallenge() {
		Challenge challenge = new Challenge();
		challenge.setTitle("이달의 거지 챌린지");
		challenge.setDescription("이달의 거지");
		challenge.setReward("50");
		challenge.setStartDate(LocalDate.now().withDayOfMonth(1).atStartOfDay());
		challenge.setEndDate(LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).atStartOfDay());
		challengeRepository.save(challenge);
	}


}