package com.room7.moneygement.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.room7.moneygement.model.UserRole;
import com.room7.moneygement.repository.UserRepository;
import com.room7.moneygement.serviceImpl.EmailServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.room7.moneygement.model.User;
import org.springframework.web.bind.annotation.RequestMapping;

import com.room7.moneygement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	private final EmailServiceImpl emailService;
	private final UserRepository userRepository;
	private final Map<String, String> verificationCodes = new HashMap<>();


	@PostMapping("/signup")
	public String signup(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email) {
		User user = new User();
		user.setUsername(username);

		// 비밀번호 해싱
		String hashedPassword = userService.encodePassword(password);
		user.setPassword(hashedPassword);
		user.setEmail(email);
		user.setCreatedAt(LocalDateTime.now());
		user.setIsDeleted(false);
		user.setRole(UserRole.LV1);
		user.setProfileImg("https://moneygement.s3.ap-northeast-2.amazonaws.com/static/Group+16.png");
		user.setUpdatedAt(LocalDateTime.now());

		userService.save(user);

		return "redirect:/login";
	}

	@GetMapping("/checkUsername")
	@ResponseBody
	public Map<String, Boolean> checkUsername(@RequestParam("username") String username) {
		boolean isAvailable = !userService.existsByUsername(username);
		Map<String, Boolean> response = new HashMap<>();
		response.put("isAvailable", isAvailable);
		return response;
	}

	@GetMapping("/checkEmail")
	@ResponseBody
	public Map<String, Boolean> checkEmail(@RequestParam("email") String email) {
		boolean isAvailable = !userService.existsByEmail(email);
		Map<String, Boolean> response = new HashMap<>();
		response.put("isAvailable", isAvailable);
		return response;
	}

	@GetMapping("/verifyEmail")
	@ResponseBody
	public RedirectView verifyEmail(@RequestParam String token) {
		try {
			String userEmail = JWT.require(Algorithm.HMAC512("moneymoney".getBytes()))
					.build()
					.verify(token)
					.getSubject();

			String successMessage = URLEncoder.encode("이메일 인증이 완료되었습니다. 창을 닫아주세요.", "UTF-8");
			return new RedirectView("/emailVerified?status=success&message=" + successMessage);

		}
		catch (Exception e) {
			return new RedirectView("/emailVerified?message=유효시간이 지났습니다. 다시 시도해주세요.");
		}
	}

	@PostMapping("/sendEmail")
	public ResponseEntity<String> sendEmail(@RequestParam("email") String email) {
		try {
			User user = new User();
			user.setEmail(email);
			emailService.sendVerificationEmail(user);
			return ResponseEntity.ok("인증 이메일을 보냈습니다.");
		}
		catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증 이메일 전송 실패");
		}
	}

	// 이메일 인증번호 생성 및 전송
	@PostMapping("/send-id-verification-code")
	public ResponseEntity<String> sendIdVerificationCode(@RequestParam String email) {
		User user = userService.findByEmail(email);
		if (user == null) {
			return ResponseEntity.badRequest().body("해당 이메일로 등록된 사용자가 없습니다.");
		}

		String code = generateVerificationCode();
		verificationCodes.put(email, code);

		// 이메일을 통해 인증번호 전송
		emailService.sendIdVerificationEmail(email, code);

		return ResponseEntity.ok("인증 이메일을 전송했습니다.");
	}

	// 인증번호 확인
	@PostMapping("/verify-id-code")
	public ResponseEntity<String> verifyIdCode(@RequestParam String email, @RequestParam String code) {
		String storedCode = verificationCodes.get(email);
		if (storedCode != null && storedCode.equals(code)) {
			return ResponseEntity.ok("인증되었습니다.");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증번호가 올바르지 않습니다.");
	}

	public String generateVerificationCode() {
		return String.format("%06d", new Random().nextInt(999999));
	}

	@PostMapping("/find-id")
	@ResponseBody
	public Map<String, String> findEmail(@RequestParam String email) {
		return userRepository.findByEmail(email)
				.map(user -> {
					Map<String, String> result = new HashMap<>();
					result.put("username", user.getUsername());
					result.put("signupDate", user.getCreatedAt().toLocalDate().toString());
					return result;
				})
				.orElse(Collections.emptyMap());
	}

	@PostMapping("/send-password-verification-code")
	public ResponseEntity<String> sendPasswordVerificationCode(@RequestParam String username){
		User user = userService.findByUsername(username);
		if (user == null){
			return ResponseEntity.badRequest().body("해당 아이디로 등록된 사용자가 없습니다.");
		}

		String email = user.getEmail();
		String code = generateVerificationCode();
		verificationCodes.put(email, code);
		System.out.println(verificationCodes);

		emailService.sendPasswordVerificationEmail(email, code);

		return ResponseEntity.ok("인증 이메일을 전송했습니다.");
	}

	@PostMapping("/verify-password-code")
	public ResponseEntity<String> verifyPasswordCode(@RequestParam String username, @RequestParam String code){
		User user = userService.findByUsername(username);
		if (user == null){
			return ResponseEntity.badRequest().body("해당 아이디로 등록된 사용자가 없습니다.");
		}
		String email = user.getEmail();
		String storedCode = verificationCodes.get(email);
		System.out.println(storedCode);
		System.out.println(code);
		if (storedCode != null && storedCode.equals(code)) {
			return ResponseEntity.ok("인증되었습니다.");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증번호가 틀렸습니다. 다시 확인해주세요..");
	}

//	@PostMapping("/send-reset-link")
//	public String sendResetLink(@RequestParam String username, Model model) {
//		try {
//			userService.sendPasswordResetLink(username);
//			model.addAttribute("message", "비밀번호 초기화 링크가 이메일로 전송되었습니다.");
//		} catch (Exception e) {
//			model.addAttribute("error", "비밀번호 초기화 링크를 보내는데 실패했습니다. 다시 시도해주세요.");
//		}
//		return "find-password";
//	}


}