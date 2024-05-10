package com.room7.moneygement.controller;

import com.room7.moneygement.dto.QnADTO;
import com.room7.moneygement.service.QnAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/qna")
public class QnAController {

	@Value("${alan.api.url}")
	private String alanApiUrl;

	@Value("${alan.api.key}")
	private String alanApiKey;

	private final QnAService qnaService;

	@Autowired
	public QnAController(QnAService qnaService) {
		this.qnaService = qnaService;
	}

	@GetMapping("/chat")
	public String showQnaPage() {
		return "qnachat"; // qnachat.html 파일 이름과 동일해야 합니다.
	}

	@PostMapping("/ask")
	public ResponseEntity<QnADTO> askQuestion(@RequestBody QnADTO qnaDTO) {
		// 사용자가 입력한 질문을 받아옵니다.
		String question = qnaDTO.getQuestion();
		// 앨런 AI와의 통신을 통해 답변을 받습니다.
		String alanApiResponse = communicateWithAlan(question);
		// 앨런 AI로부터 받은 답변을 QnADTO에 저장합니다.
		qnaDTO.setAnswer(alanApiResponse);
		// 답변이 포함된 DTO를 저장하고 응답합니다.
		QnADTO savedQnA = qnaService.saveQnA(qnaDTO);
		return new ResponseEntity<>(savedQnA, HttpStatus.CREATED);
	}

	@GetMapping("/{qnaId}")
	public ResponseEntity<QnADTO> getAnswer(@PathVariable Long qnaId) {
		QnADTO qnaDTO = qnaService.getQnAById(qnaId);
		if (qnaDTO == null) {
			return new ResponseEntity<>(qnaDTO, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// 앨런 AI와의 통신을 담당하는 메서드
	private String communicateWithAlan(String question) {
		// 앨런 AI API와의 통신을 위한 RestTemplate 객체 생성
		RestTemplate restTemplate = new RestTemplate();
		// 앨런 AI API에 POST 요청을 보냅니다.
		String alanApiResponse = restTemplate.getForObject(alanApiUrl + "?content=" + question, String.class);
		return alanApiResponse;
	}
}
