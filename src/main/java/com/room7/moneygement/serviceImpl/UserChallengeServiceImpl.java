package com.room7.moneygement.serviceImpl;

import com.room7.moneygement.dto.UserChallengeDTO;
import com.room7.moneygement.model.UserChallenge;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.room7.moneygement.repository.UserChallengeRepository;
import com.room7.moneygement.service.UserChallengeService;

import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserChallengeServiceImpl implements UserChallengeService {
	private final UserChallengeRepository userChallengeRepository;

	public UserChallenge addUserChallenge(UserChallenge userChallenge){
		return userChallengeRepository.save(userChallenge);
	}

	public Long getLatestTargetAmountByUserId(Long userId) {
		List<UserChallenge> userChallenges = userChallengeRepository.findByUserId(userId);
		if (!userChallenges.isEmpty()) {
			return userChallenges.get(userChallenges.size() - 1).getTargetAmount();
		}
		return null; // 혹은 적절한 예외 처리
	}
}

