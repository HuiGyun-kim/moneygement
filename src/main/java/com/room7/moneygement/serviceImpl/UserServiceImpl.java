package com.room7.moneygement.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.room7.moneygement.model.Ledger;
import com.room7.moneygement.model.User;
import com.room7.moneygement.repository.LedgerRepository;
import com.room7.moneygement.repository.UserRepository;
import com.room7.moneygement.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserRepository userRepository;
	// private final PasswordEncoder passwordEncoder;
	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email).orElse(null);
	}

	@Override
	public User findUserById(Long userId) {
		return userRepository.findById(userId).orElse(null);
	}

	// public boolean checkPassword(String rawPassword, String encodedPassword) {
	// 	return passwordEncoder.matches(rawPassword, encodedPassword);
	// }
}

