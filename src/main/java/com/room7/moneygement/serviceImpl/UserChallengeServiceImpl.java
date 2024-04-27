package com.room7.moneygement.serviceImpl;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.room7.moneygement.repository.UserChallengeRepository;
import com.room7.moneygement.service.UserChallengeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserChallengeServiceImpl implements UserChallengeService {
	private final UserChallengeRepository userChallengeRepository;

}

