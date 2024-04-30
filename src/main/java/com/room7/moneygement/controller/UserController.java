package com.room7.moneygement.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.room7.moneygement.model.User;
import com.room7.moneygement.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, @RequestParam(required = false) boolean rememberMe,
		HttpServletResponse response, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
		User user = userService.findByUsername(username);
		if (user != null && user.getPassword().equals(password)) {
			if (rememberMe) {
				// 사용자 아이디를 URL 인코딩하여 쿠키에 저장
				String encodedUsername = URLEncoder.encode(username, StandardCharsets.UTF_8.toString());
				Cookie rememberUserCookie = new Cookie("rememberUser", encodedUsername);
				rememberUserCookie.setMaxAge(60 * 60 * 24 * 30); // 30일
				rememberUserCookie.setPath("/");
				response.addCookie(rememberUserCookie);
			}
			return "redirect:/"; // 로그인 성공 시 메인 페이지로 리다이렉트
		} else {
			// 로그인 실패 메시지를 URL 인코딩하여 쿠키에 저장
			String errorMessage = URLEncoder.encode("아이디나 비밀번호가 맞지 않습니다.", StandardCharsets.UTF_8.toString());
			Cookie loginErrorCookie = new Cookie("loginError", errorMessage);
			loginErrorCookie.setMaxAge(10); // 10초 동안만 유지
			loginErrorCookie.setPath("/");
			response.addCookie(loginErrorCookie);

			redirectAttributes.addFlashAttribute("error", "아이디나 비밀번호가 맞지 않습니다.");
			return "redirect:/login"; // 로그인 실패 시 로그인 페이지로 리다이렉트
		}
	}

}