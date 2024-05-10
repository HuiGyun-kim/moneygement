package com.room7.moneygement.controller;
import com.room7.moneygement.dto.DiaryDTO;
import com.room7.moneygement.model.Diary;
import com.room7.moneygement.repository.DiaryRepository;
import com.room7.moneygement.service.CustomUserDetails;
import com.room7.moneygement.serviceImpl.DiaryServiceImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
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
	public ResponseEntity<?> saveDiary(@RequestBody DiaryDTO diaryDTO, @AuthenticationPrincipal CustomUserDetails userDetails){
		Long userId = userDetails.getUserId();
		Diary save = diaryServiceImpl.saveDiary(diaryDTO);
		return ResponseEntity.ok().body(Map.of("message", "저장되었습니다."));
    }

	@GetMapping("/checkDiary")
	public ResponseEntity<?> checkDiary(
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@RequestParam("userId") Long userId) {

		List<Diary> diaries = diaryServiceImpl.checkDiary(date, userId);

		if (diaries.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("일기가 존재하지 않습니다.");
		}
		else {
			return ResponseEntity.ok().body(diaries);
		}
	}
}