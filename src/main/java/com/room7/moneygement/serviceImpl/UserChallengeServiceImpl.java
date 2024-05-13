package com.room7.moneygement.serviceImpl;

import com.room7.moneygement.dto.UserChallengeDTO;
import com.room7.moneygement.model.LedgerEntry;
import com.room7.moneygement.model.User;
import com.room7.moneygement.model.UserChallenge;
import com.room7.moneygement.repository.LedgerEntryRepository;
import com.room7.moneygement.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.room7.moneygement.repository.UserChallengeRepository;
import com.room7.moneygement.service.UserChallengeService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserChallengeServiceImpl implements UserChallengeService {
	private final UserChallengeRepository userChallengeRepository;
	private final LedgerEntryRepository ledgerEntryRepository;
	private final UserRepository userRepository;

	public UserChallenge addUserChallenge(UserChallenge userChallenge){
		return userChallengeRepository.save(userChallenge);
	}

	public Long getLatestTargetAmountByUserId(Long userId) {
		List<UserChallenge> userChallenges = userChallengeRepository.findByUserId(userId);
		if (!userChallenges.isEmpty()) {
			return userChallenges.get(userChallenges.size() - 1).getTargetAmount();
		}
		return null;
	}

	public Long getExpenseAmount(Long userId, LocalDate startDate, LocalDate endDate) {

		List<LedgerEntry> expenses = ledgerEntryRepository.findByUserIdAndDateBetweenAndLedgerType(
				userId, startDate, endDate, true);


        return expenses.stream()
				.mapToLong(LedgerEntry::getAmount)
				.sum();
	}

	@Scheduled(cron = "0 59 23 L * ?") // 마지막 날 23:59 실행
	public void checkAndCompleteChallenges() {
		List<UserChallenge> challenges = userChallengeRepository.findAll();
		LocalDate now = LocalDate.from(LocalDateTime.now());
		challenges.forEach(challenge -> {
			Long remain = challenge.getTargetAmount() - getExpenseAmount(challenge.getUserId(), LocalDate.from(challenge.getJoinDate()), now);
			if (remain > 0) {
				challenge.setIsCompleted(true);
				userChallengeRepository.save(challenge);
				increaseUserExp(challenge.getUserId(), 50);
				challenge.setIsCompleted(false);
				userChallengeRepository.save(challenge);
			}
		});
	}

	private void increaseUserExp(Long userId, int exp) {
		User user = userRepository.findById(userId).orElse(null);
		if (user != null) {
			user.setExp(user.getExp() + exp);
			userRepository.save(user);
		}
	}

//	public void updateExpOnCompleted(Long challengeId) {
//		UserChallenge challenge = userChallengeRepository.findById(challengeId).orElse(null);
//		if (challenge != null && challenge.getIsCompleted()){
//			User user = userRepository.findById(challenge.getUserId()).orElse(null);
//			if (user != null){
//				user.setExp(user.getExp() + 50);
//				userRepository.save(user);
//
//				challenge.setIsCompleted(false);
//				userChallengeRepository.save(challenge);
//			}
//		}
//	}
}

