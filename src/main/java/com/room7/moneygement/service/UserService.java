package com.room7.moneygement.service;

import com.room7.moneygement.model.User;

public interface UserService {
	User findByUsername(String username);
	User save(User user);
	boolean existsByUsername(String userId);
	User findByEmail(String email);



}

