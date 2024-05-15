package com.room7.moneygement.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.room7.moneygement.service.CustomUserDetails;
import com.room7.moneygement.service.FaqService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qna")
public class FaqController {

	private final FaqService faqService;


	@PostMapping("/ask")
	public ResponseEntity<Map<String, String>> askQuestion(@RequestBody Map<String, String> requestBody, @AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getUserId();
		String question = requestBody.get("question");

		// 질문 분석 및 맞춤형 답변 생성 로직을 호출합니다.
		String answer = faqService.generateAnswer(question, userId);

		// 응답 데이터를 JSON 형식으로 변경합니다.
		Map<String, String> response = new HashMap<>();
		response.put("answer", answer);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/faq")
	public ResponseEntity<List<String>> getFrequentlyAskedQuestions() {
		List<String> questions = faqService.getFrequentlyAskedQuestions();
		return ResponseEntity.ok(questions);
	}

}
