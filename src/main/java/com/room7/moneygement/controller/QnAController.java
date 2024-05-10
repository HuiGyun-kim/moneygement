package com.room7.moneygement.controller;

import com.room7.moneygement.dto.QnADTO;
import com.room7.moneygement.service.QnAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qna")
public class QnAController {

	private final QnAService qnaService;

	@Autowired
	public QnAController(QnAService qnaService) {
		this.qnaService = qnaService;
	}

	@GetMapping("/qna")
	public String showQnaPage() {
		return "qna"; // qna.html 파일 이름과 동일해야 합니다.
	}

	@PostMapping("/ask")
	public ResponseEntity<QnADTO> askQuestion(@RequestBody QnADTO qnaDTO) {
		// 사용자가 입력한 질문을 받아옵니다.
		String question = qnaDTO.getQuestion();

		// 앨런 AI와의 통신을 위한 예시 코드입니다.
		// 여기에서는 간단히 질문을 앨런 AI에게 보내고, 답변을 받아와서 QnADTO에 저장합니다.
		// 실제 환경에서는 앨런 AI와의 통신 방법에 따라 코드가 달라질 수 있습니다.
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
		return new ResponseEntity<>(qnaDTO, HttpStatus.OK);
	}

	// 앨런 AI와의 통신을 담당하는 메서드
	private String communicateWithAlan(String question) {
		// 여기에 앨런 AI와의 통신 코드를 작성합니다.
		// 실제로는 앨런 AI의 API를 호출하여 질문을 보내고 답변을 받아와야 합니다.
		// 이 예시 코드는 앨런 AI와의 통신을 간략하게 모사한 것일 뿐입니다.
		// 실제 환경에서는 앨런 AI의 API에 따라 통신 코드를 작성해야 합니다.
		// 아래는 간단한 예시 코드입니다.

		// 앨런 AI의 API에 질문을 보내고 답변을 받아옵니다.
		// 이 예시에서는 단순히 입력된 질문에 '앨런 AI가 대답합니다.'라는 답변을 반환합니다.
		String alanResponse = "앨런 AI가 대답합니다.";

		return alanResponse;
	}
}
