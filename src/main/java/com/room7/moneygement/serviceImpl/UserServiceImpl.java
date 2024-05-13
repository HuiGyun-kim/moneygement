package com.room7.moneygement.serviceImpl;
import com.room7.moneygement.controller.S3Uploader;
import com.room7.moneygement.repository.FollowRepository;

import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.room7.moneygement.model.User;
import com.room7.moneygement.repository.UserRepository;
import com.room7.moneygement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final FollowRepository followRepository;
	private final S3Uploader s3Uploader;

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
	@Override
	public User findById(Long id) { // findById 메서드 구현
		return userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));
	}

//	--------------------------------------------
@Override
@Transactional
public String uploadProfileImage(MultipartFile file, User user) throws IOException {
	User requestUser = userRepository.findById(user.getUserId())
			.orElseThrow(() -> new RuntimeException("로그인 후 사용하세요."));
	if (file != null && !file.isEmpty()) {
		String profileImageUrl = s3Uploader.upload(file, "profile");
		requestUser.setProfileImg(profileImageUrl);
		userRepository.save(requestUser);
	} else {
		requestUser.setProfileImg("/img/main/img_2.png"); // 기본 이미지 경로
	}
	return "프로필 사진 업로드에 성공하였습니다.";
}

	@Override
	@Transactional
	public String deleteProfileImage(User user) {
		User requestUser = userRepository.findById(user.getUserId()).orElseThrow(
				() -> new IllegalArgumentException("로그인 후 사용하세요.")
		);
		String s3Url = user.getProfileImg();
		String s3Key = s3Url.substring(s3Url.indexOf("profile/"));

		s3Uploader.fileDelete(s3Key);
		requestUser.setProfileImg("/img/main/img_2.png");

		return "프로필 사진 제거에 성공했습니다.";
	}

}

