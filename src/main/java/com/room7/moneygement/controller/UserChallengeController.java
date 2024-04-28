package com.room7.moneygement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.room7.moneygement.service.UserChallengeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/userChallenges")
public class UserChallengeController {

	private final UserChallengeService userChallengeService;
}