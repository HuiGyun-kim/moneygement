package com.room7.moneygement.controller;
import com.room7.moneygement.dto.DiaryDTO;
import com.room7.moneygement.model.Diary;
import com.room7.moneygement.repository.DiaryRepository;
import com.room7.moneygement.service.CustomUserDetails;
import com.room7.moneygement.serviceImpl.DiaryServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

	private final DiaryRepository diaryRepository;
	private final DiaryServiceImpl diaryServiceImpl;

	@GetMapping("/diaryRequest")
	public ResponseEntity<String> diaryRequestProxy(@RequestParam Map<String, String> allParams) {
		RestTemplate restTemplate = new RestTemplate();
		String baseUrl = "https://kdt-api-function.azurewebsites.net/api/v1/question";
		String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.queryParam("content", allParams.get("content"))
				.queryParam("client_id", allParams.get("client_id"))
				.toUriString();
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		return ResponseEntity.ok()
				.headers(response.getHeaders())
				.body(response.getBody());
	}

	@PostMapping("/saveDiary")
	public String saveDiary(@RequestBody DiaryDTO diaryDTO, @AuthenticationPrincipal CustomUserDetails userDetails){
		Long userId = userDetails.getUserId();
		Diary save = diaryServiceImpl.saveDiary(diaryDTO);
		return "저장되었습니다.";
    }
}