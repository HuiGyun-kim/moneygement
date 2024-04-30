package com.room7.moneygement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
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
	@GetMapping("signup-email")
	public String signupEmail(){
		return "main/signup-email";
	}
}
