package com.room7.moneygement.serviceImpl;

import com.room7.moneygement.dto.QnADTO;
import com.room7.moneygement.model.QnA;
import com.room7.moneygement.repository.QnARepository;
import com.room7.moneygement.service.QnAService;
import org.springframework.beans.BeanUtils;
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
		// QnADTO에서 QnA 엔티티로 변환
		QnA qna = new QnA();
		// 프로퍼티를 복사하여 새 객체에 할당
		BeanUtils.copyProperties(qnaDTO, qna);
		qna.setCreatedAt(LocalDateTime.now());

		// 저장된 QnA 엔티티 반환
		QnA savedQnA = qnaRepository.save(qna);

		// 저장된 QnA 엔티티를 QnADTO로 변환하여 반환
		return convertToDTO(savedQnA);
	}

	@Override
	public QnADTO getQnAById(Long qnaId) {
		// ID로 QnA 엔티티 조회
		QnA qna = qnaRepository.findById(qnaId)
				.orElseThrow(() -> new IllegalArgumentException("QnA not found with id: " + qnaId));

		// 조회된 QnA 엔티티를 QnADTO로 변환하여 반환
		return convertToDTO(qna);
	}

	// 엔티티를 DTO로 변환하는 메서드
	private QnADTO convertToDTO(QnA qna) {
		QnADTO qnaDTO = new QnADTO();
		// 프로퍼티를 복사하여 새 객체에 할당
		BeanUtils.copyProperties(qna, qnaDTO);
		return qnaDTO;
	}
}
