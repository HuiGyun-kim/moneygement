package com.room7.moneygement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

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
}