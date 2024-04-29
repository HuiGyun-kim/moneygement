package com.room7.moneygement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.room7.moneygement.model.User;
import com.room7.moneygement.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	// @PostMapping 경로 수정, @ResponseBody 제거
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password) {
		User user = userService.findByUsername(username);
		if (user != null) {
			return "redirect:/main/index"; // 로그인 성공 시 메인 페이지로 리다이렉트
		}
		return "redirect:/login?error=true"; // 로그인 실패 시 로그인 페이지로 리다이렉트
	}

}