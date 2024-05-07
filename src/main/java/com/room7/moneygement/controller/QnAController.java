package com.room7.moneygement.controller;

import com.room7.moneygement.dto.QnADTO;
import com.room7.moneygement.service.QnAService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/qna")
@RestController
public class QnAController {

	private final QnAService qnaService;

	public QnAController(QnAService qnaService) {
		this.qnaService = qnaService;
	}

	@GetMapping("/ask")
	public String showQnAForm() {
		return "qna";
	}

	@PostMapping("/response")
	public QnADTO askQuestion(@RequestParam String question) {
		String answer = qnaService.askQuestion(question);
		QnADTO qnaDTO = new QnADTO();
		qnaDTO.setQuestion(question);
		qnaDTO.setAnswer(answer);
		return qnaDTO;
	}
}