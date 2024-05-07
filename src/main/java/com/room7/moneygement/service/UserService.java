package com.room7.moneygement.service;

import java.time.LocalDateTime;

import org.springframework.transaction.annotation.Transactional;

import com.room7.moneygement.model.User;

public interface UserService {
	User findByUsername(String username);
	User save(User user);
	boolean existsByUsername(String userId);
	User findByEmail(String email);

	User findById(Long id);

	User findUserById(Long userId);


	// boolean checkPassword(String rawpassword, String encodedPassword);
}

