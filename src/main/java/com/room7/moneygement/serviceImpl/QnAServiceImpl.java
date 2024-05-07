package com.room7.moneygement.serviceImpl;

import com.room7.moneygement.model.QnA;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.room7.moneygement.repository.QnARepository;
import com.room7.moneygement.service.QnAService;
import org.springframework.web.client.RestTemplate;

@Service
public class QnAServiceImpl implements QnAService {

	private final QnARepository qnARepository;

	@Value("${alan.api.url}")
	private String alanApiUrl;

	@Value("${alan.api.key}")
	private String alanApiKey;

	public QnAServiceImpl(QnARepository qnARepository) {
		this.qnARepository = qnARepository;
	}

	@Override
	public String askQuestion(String question) {
		// Alan Beta API를 호출하여 질문에 대한 답변을 받음
		String answer = callAlanBetaApi(question);

		// 질문한 목록과 대답을 데이터베이스에 저장
		QnA qna = new QnA();
		qna.setQuestion(question);
		qna.setAnswer(answer);
		qnARepository.save(qna);

		return answer;
	}

	private String callAlanBetaApi(String question) {
		RestTemplate restTemplate = new RestTemplate();
		String url = alanApiUrl + "/ask"; // Alan Beta API 엔드포인트
		String body = "{\"question\": \"" + question + "\"}";

		// API 호출
		String response = restTemplate.postForObject(url, body, String.class);
		return response;
	}
}