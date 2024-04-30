package com.room7.moneygement.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.room7.moneygement.model.UserRole;
import com.room7.moneygement.serviceImpl.EmailServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.room7.moneygement.model.User;
import com.room7.moneygement.service.UserService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	private final EmailServiceImpl emailService;

	// @PostMapping 경로 수정, @ResponseBody 제거
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password) {
		User user = userService.findByUsername(username);
		if (user != null) {
			return "redirect:/main/index"; // 로그인 성공 시 메인 페이지로 리다이렉트
		}
		return "redirect:/login?error=true"; // 로그인 실패 시 로그인 페이지로 리다이렉트
	}

	@PostMapping("/signup")
	public String signup(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("email") String email) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(email);
		user.setCreatedAt(LocalDateTime.now());
		user.setIsDeleted(false);
		user.setRole(UserRole.LV1);
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

	@GetMapping("/verifyEmail")
	@ResponseBody
	public ResponseEntity<String> verifyEmail(@RequestParam String token){
		try{
			String userEmail = JWT.require(Algorithm.HMAC512("moneymoney".getBytes()))
					.build()
					.verify(token)
					.getSubject();

			return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
		}
		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효시간이 지났습니다. 다시 시도해주세요.");
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

			e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증 이메일 전송 실패");
		}
	}

}
