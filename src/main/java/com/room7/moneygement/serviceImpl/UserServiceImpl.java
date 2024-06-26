package com.room7.moneygement.serviceImpl;

import com.room7.moneygement.controller.UserController;
import com.room7.moneygement.service.S3Upload;
import com.room7.moneygement.repository.FollowRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.room7.moneygement.model.User;
import com.room7.moneygement.repository.UserRepository;
import com.room7.moneygement.service.UserService;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final FollowRepository followRepository;
	private final S3Upload s3Uploader;
	private UserController userController;

	// PasswordEncoder 빈 주입받기
	private final PasswordEncoder passwordEncoder;
	private final JavaMailSender mailSender;

	public UserServiceImpl(UserRepository userRepository, FollowRepository followRepository, S3Upload s3Uploader, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
		this.userRepository = userRepository;
		this.followRepository = followRepository;
		this.s3Uploader = s3Uploader;
		this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username).orElse(null);
	}
	@Override
	public String encodePassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
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
  
	@Override
	public User findById(Long id) { // findById 메서드 구현
		return userRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("User not found"));
	}

	public List<User> searchUsers(String searchType, String searchKey){
		if (searchType != null && searchKey != null && !searchKey.isEmpty()) {
			List<User> result = new ArrayList<>();

			if ("searchId".equals(searchType)) {
				Optional<User> user = userRepository.findByUsername(searchKey);
				user.ifPresent(result::add);
			}

			return result;
		}
		else {
			return userRepository.findAll();
		}
	}

	// 비밀번호 확인

	public boolean checkPassword(User user, String password) {
		return passwordEncoder.matches(password, user.getPassword());
	}

	// 비밀번호 변경
	@Transactional
	public boolean changePassword(User user, String currentPassword, String newPassword) {
		if (user != null && passwordEncoder.matches(currentPassword, user.getPassword())) {
			// 현재 비밀번호가 일치하는 경우 새 비밀번호로 변경
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
			return true;
		}
		return false;
	}

	// 회원 탈퇴
	@Transactional
	public User deleteUser(User user) {
		// 회원 탈퇴 시 연관된 정보들도 삭제해야함
		// 방명록, 댓글, 팔로우 등에서 해당 user의 정보 삭제후
		userRepository.delete(user);
		return user;
	}

	@Override
	@Transactional
	public void updateUserProfileIntroduction(Long userId, String introduction) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

		user.setIntroduction(introduction);
		userRepository.save(user);
	}

	@Override
	public String getUserProfileIntroduction(Long userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

		return user.getIntroduction();
	}

	@Override
	public boolean existsByEmail(String email){
		return userRepository.existsByEmail(email);
	}

	@Override
	public String generateResetPasswordLink(String email){
		Optional<User> userOptional = userRepository.findByEmail(email);
		if (!userOptional.isPresent()) {
			return null;
		}

		User user = userOptional.get();
		String token = userController.generateVerificationCode();
		user.setRememberToken(token);
		userRepository.save(user);

		return "https://localhost:8080/reset-password?token=" + token;
	}

	@Override
	public String findUsernameByEmail(String email){
		Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.map(User::getUsername).orElse(null);
    }

//	public void sendPasswordResetLink(String username) throws Exception{
//		User user = userRepository.findByUsername(username)
//				.orElseThrow(() -> new Exception("유저를 찾을 수 없습니다."));
//
//		String email = user.getEmail();
//		String resetLink = "http://localhost:8080/reset-password?token=moneymoney"; // 이거 나중에 링크 이름 바꿔야함.
//
//		SimpleMailMessage message = new SimpleMailMessage();
//		message.setTo(email);
//		message.setSubject("비밀번호 재설정 메일");
//		message.setText("아래 링크로 접속하여 비밀번호를 재설정 해주세요. " + resetLink);
//
//		mailSender.send(message);
//	}


}