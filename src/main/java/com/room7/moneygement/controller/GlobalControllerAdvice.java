package com.room7.moneygement.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.room7.moneygement.dto.UserDTO;
import com.room7.moneygement.model.User;
import com.room7.moneygement.service.CustomUserDetails;

@ControllerAdvice
public class GlobalControllerAdvice {
	@ModelAttribute("currentUser")
	public User addUserToModel(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		return customUserDetails != null ? customUserDetails.getUser() : null;
	}
}