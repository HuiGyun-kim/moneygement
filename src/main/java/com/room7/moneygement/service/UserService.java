package com.room7.moneygement.service;

import com.room7.moneygement.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface UserService {
	User findByUsername(String username);
	User save(User user);
	boolean existsByUsername(String userId);
	User findByEmail(String email);

	User findById(Long id);

	User findUserById(Long userId);

	boolean checkPassword(User user, String Password);

	boolean changePassword(User user, String currentPassword, String newPassword);

	User deleteUser(User user);
	// boolean checkPassword(String rawpassword, String encodedPassword);

	void updateUserProfileIntroduction(Long userId, String introduction);

	String getUserProfileIntroduction(Long userId);
}


