package com.room7.moneygement.service;

import com.room7.moneygement.model.User;

public interface UserService {
	User findByUsername(String username);

	// boolean checkPassword(String rawpassword, String encodedPassword);
}

