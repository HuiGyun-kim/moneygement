package com.room7.moneygement.serviceImpl;

import org.springframework.stereotype.Service;

import com.room7.moneygement.repository.ChallengeRepository;
import com.room7.moneygement.service.ChallengeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {
	private final ChallengeRepository challengeRepository;

}

