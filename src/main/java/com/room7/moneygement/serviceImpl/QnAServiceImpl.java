package com.room7.moneygement.serviceImpl;

import com.room7.moneygement.dto.QnADTO;
import com.room7.moneygement.model.QnA;
import com.room7.moneygement.repository.QnARepository;
import com.room7.moneygement.service.QnAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;

@Service
public class QnAServiceImpl implements QnAService {

	private final QnARepository qnaRepository;

	@Autowired
	public QnAServiceImpl(QnARepository qnaRepository) {
		this.qnaRepository = qnaRepository;
	}

	@Override
	public QnADTO saveQnA(QnADTO qnaDTO) {
		QnA qna = new QnA();
		qna.setUserId(qnaDTO.getUserId());
		qna.setQuestion(qnaDTO.getQuestion());
		qna.setAnswer(qnaDTO.getAnswer());
		qna.setCreatedAt(LocalDateTime.now());

		QnA savedQnA = qnaRepository.save(qna);

		return convertToDTO(savedQnA);
	}

	@Override
	public QnADTO getQnAById(Long qnaId) {
		QnA qna = qnaRepository.findById(qnaId)
				.orElseThrow(() -> new IllegalArgumentException("QnA not found with id: " + qnaId));

		return convertToDTO(qna);
	}

	// 엔티티를 DTO로 변환하는 메서드
	private QnADTO convertToDTO(QnA qna) {
		QnADTO qnaDTO = new QnADTO();
		qnaDTO.setQnaId(qna.getQnaId());
		qnaDTO.setUserId(qna.getUserId());
		qnaDTO.setQuestion(qna.getQuestion());
		qnaDTO.setAnswer(qna.getAnswer());
		qnaDTO.setCreatedAt(qna.getCreatedAt());
		return qnaDTO;
	}
}
