package com.room7.moneygement.controller;

import java.util.List;

import com.room7.moneygement.dto.QnADTO;
import com.room7.moneygement.model.LedgerEntry;
import com.room7.moneygement.repository.LedgerEntryRepository;
import com.room7.moneygement.service.CustomUserDetails;
import com.room7.moneygement.service.QnAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QnAController {

	@Value("${alan.api.url}")
	private String alanApiUrl;

	@Value("${alan.api.key}")
	private String alanApiKey;

	private final QnAService qnaService;
	private final LedgerEntryRepository ledgerEntryRepository;


	@PostMapping("/ask")
	public ResponseEntity<QnADTO> askQuestion(@RequestBody QnADTO qnaDTO, @AuthenticationPrincipal CustomUserDetails userDetails) {
		Long userId = userDetails.getUserId();
		String question = qnaDTO.getQuestion();

		// 로그인한 사용자의 전체 가계부 목록에서 수입/지출 내역 데이터를 가져옵니다.
		List<LedgerEntry> ledgerEntries = ledgerEntryRepository.findByUserId(userId);

		// 질문 분석 및 맞춤형 답변 생성 로직을 호출합니다.
		String answer = qnaService.generateAnswer(question, ledgerEntries, userId);

		qnaDTO.setUserId(userId);
		qnaDTO.setAnswer(answer);

		QnADTO savedQnA = qnaService.saveQnA(qnaDTO);
		return new ResponseEntity<>(savedQnA, HttpStatus.CREATED);
	}

	@GetMapping("/faq")
	public ResponseEntity<List<String>> getFrequentlyAskedQuestions() {
		List<String> questions = qnaService.getFrequentlyAskedQuestions();
		return ResponseEntity.ok(questions);
	}

	@GetMapping("/{qnaId}")
	public ResponseEntity<QnADTO> getAnswer(@PathVariable Long qnaId) {
		QnADTO qnaDTO = qnaService.getQnAById(qnaId);
		if (qnaDTO != null) {
			return new ResponseEntity<>(qnaDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
