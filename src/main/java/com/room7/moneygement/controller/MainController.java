package com.room7.moneygement.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.room7.moneygement.service.CategoryService;
import com.room7.moneygement.service.CustomUserDetails;
import com.room7.moneygement.service.LedgerService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MainController {

	private final CategoryService categoryService;
	private final LedgerService ledgerService;

	// 기본 홈 페이지를 반환하는 메서드
	@GetMapping("/")
	public String home() {
		return "main/index";
	}

	//로그인 페이지를 반환하는 메서드
	@GetMapping("/login")
	public String login() {
		return "main/login";
	}

	//회원가입 선택 페이지를 반환하는 메서드
	@GetMapping("/signup")
	public String signup() {
		return "main/signup";
	}

	//회원가입 선택시 이메일을 사용해서 반환할 때 사용하는 메서드
	@GetMapping("/signup-email")
	public String signupEmail() {
		return "main/signup-email";
	}

	@GetMapping("/emailVerified")
	public String emailVerified(@RequestParam(value = "status", required = false) String status,
								@RequestParam(value = "message", required = false) String message, Model model) {
		model.addAttribute("status", status);
		model.addAttribute("message", message);

		return "main/emailVerified";
	}

	@GetMapping("/find-id")
	public String findId() {
		return "main/find-id";
	}

	@GetMapping("/find-password")
	public String findPassword() {
		return "main/find-password";
	}

	@GetMapping("/myDiary")
	public String myDiary(Model model, @AuthenticationPrincipal CustomUserDetails userDetails){
		model.addAttribute("user", userDetails.getUserDTO());
		return "main/my-diary";
	}
	@GetMapping("checkChallenge")
	public String checkChallenge(Model model, @AuthenticationPrincipal CustomUserDetails userDetails){
		model.addAttribute("user", userDetails.getUserDTO());
		return "main/checkChallenge";
	}

	@GetMapping("challengeList")
	public String challengeList(Model model, @AuthenticationPrincipal CustomUserDetails userDetails){
		model.addAttribute("user", userDetails.getUserDTO());
		return "main/challengeList";
	}

	@GetMapping("/poorChallenge")
	public String monthBest(Model model, @AuthenticationPrincipal CustomUserDetails userDetails){
		model.addAttribute("user", userDetails.getUserDTO());
		return "main/poorChallenge";
	}

	@GetMapping("/history")
	public String history(Model model,@AuthenticationPrincipal CustomUserDetails userDetails) {
		return "layout/history";
	}

	// 마이페이지 - 회원정보 수정 / 탈퇴 뷰
	@GetMapping("/change-password")
	public String changePassword() {
		return "/myPage/change-Password";
	}

	// 마이페이지 -  탈퇴 뷰
	@GetMapping("/delete-account")
	public String deleteAccount() {
		return "/myPage/deleteAccount";
	}
}

