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

	// boolean checkPassword(String rawpassword, String encodedPassword);

//	-----------------------------------------------

	String uploadProfileImage(MultipartFile file, User user) throws IOException;

	String deleteProfileImage(User user);
}


