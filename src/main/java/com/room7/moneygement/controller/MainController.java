package com.room7.moneygement.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.room7.moneygement.dto.UserDTO;
import com.room7.moneygement.model.User;
import com.room7.moneygement.service.CustomUserDetails;
import com.room7.moneygement.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

	private final UserService userService;

	// 기본 홈 페이지를 반환하는 메서드
	@GetMapping("/")
	public String home(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
		System.out.println("model,  = " + model + ", userDTO = " + userDetails);
		if (userDetails != null) {
			model.addAttribute("user", userDetails.getUserDTO());
		}
		return "main/index";
	}

	//로그인 페이지를 반환하는 메서드
	@GetMapping("/login")
	public String login() {
		return "main/login";
	}
}
