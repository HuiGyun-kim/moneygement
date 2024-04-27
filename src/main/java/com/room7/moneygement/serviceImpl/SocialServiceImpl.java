package com.room7.moneygement.serviceImpl;

import org.springframework.stereotype.Service;

import com.room7.moneygement.repository.SocialRepository;
import com.room7.moneygement.service.SocialService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SocialServiceImpl implements SocialService {
	private final SocialRepository socialRepository;

}
