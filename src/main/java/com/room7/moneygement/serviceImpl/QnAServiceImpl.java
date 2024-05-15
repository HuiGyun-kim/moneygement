package com.room7.moneygement.serviceImpl;

import com.room7.moneygement.dto.QnADTO;
import com.room7.moneygement.model.LedgerEntry;
import com.room7.moneygement.model.QnA;
import com.room7.moneygement.repository.LedgerEntryRepository;
import com.room7.moneygement.repository.QnARepository;
import com.room7.moneygement.service.QnAService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class QnAServiceImpl implements QnAService {

	private final QnARepository qnaRepository;
	private final LedgerEntryRepository ledgerEntryRepository;

	@Autowired
	public QnAServiceImpl(QnARepository qnaRepository, LedgerEntryRepository ledgerEntryRepository) {
		this.qnaRepository = qnaRepository;
		this.ledgerEntryRepository = ledgerEntryRepository;
	}

	@Override
	public QnADTO saveQnA(QnADTO qnaDTO) {
		QnA qna = new QnA();
		BeanUtils.copyProperties(qnaDTO, qna);
		qna.setCreatedAt(LocalDateTime.now());

		QnA savedQnA = qnaRepository.save(qna);
		return convertToDTO(savedQnA);
	}

	@Override
	public String generateAnswer(String question, List<LedgerEntry> ledgerEntries, Long userId) {
		// 질문 분석 및 맞춤형 답변 생성 로직을 구현합니다.
		// 사용자의 수입/지출 내역 데이터를 활용하여 적절한 답변을 생성합니다.

		// 이번 달 식비 관련 질문
		if (question.contains("이번 달 식비")) {
			int currentMonth = LocalDate.now().getMonthValue();
			int currentYear = LocalDate.now().getYear();
			Long totalExpense = ledgerEntryRepository.findTotalExpenseByUserIdAndCategoryAndYearAndMonth(userId, "식비", currentYear, currentMonth);
			return "이번 달 식비로 총 " + totalExpense + "원을 지출하셨습니다.";
		}

		// 이번 달 교통비 관련 질문
		if (question.contains("이번 달 교통비")) {
			int currentMonth = LocalDate.now().getMonthValue();
			int currentYear = LocalDate.now().getYear();
			Long totalExpense = ledgerEntryRepository.findTotalExpenseByUserIdAndCategoryAndYearAndMonth(userId, "교통비", currentYear, currentMonth);
			return "이번 달 교통비로 총 " + totalExpense + "원을 지출하셨습니다.";
		}

		// 월 평균 지출 관련 질문
		if (question.contains("월 평균 지출")) {
			Long averageMonthlyExpense = ledgerEntryRepository.findAverageMonthlyExpenseByUserId(userId);
			return "최근 3개월간 월 평균 지출은 " + averageMonthlyExpense + "원입니다.";
		}

		// 가장 많이 지출한 카테고리 관련 질문
		if (question.contains("가장 많이 지출한 카테고리")) {
			String mostExpensiveCategory = ledgerEntryRepository.findMostExpensiveCategoryByUserId(userId);
			return "최근 3개월간 가장 많이 지출한 카테고리는 " + mostExpensiveCategory + "입니다.";
		}

		// 절약 팁 관련 질문
		if (question.contains("절약 팁")) {
			String category = "외식비"; // 예시로 외식비 카테고리를 사용
			Long averageExpense = ledgerEntryRepository.findAverageMonthlyCategoryExpenseByUserId(userId, category);
			if (averageExpense != null) {
				if (averageExpense > 100000) {
					return "분석 결과, 외식 비용을 줄이고 집에서 요리하는 것이 좋겠습니다. 월 평균 외식비가 " + averageExpense + "원으로 다소 높은 편입니다.";
				} else {
					return "최근 3개월간 월 평균 외식비는 " + averageExpense + "원으로 적절한 수준입니다. 현재 식습관을 유지하시는 것이 좋겠습니다.";
				}
			} else {
				return "죄송합니다. 외식비 내역이 부족하여 분석할 수 없습니다.";
			}
		}

		// 구독 서비스 해지 관련 질문
		if (question.contains("구독 서비스 해지")) {
			return "불필요한 구독 서비스를 정리하는 것이 지출을 줄이는 데 도움이 됩니다. 현재 이용 중인 구독 서비스를 점검하고, 필요성이 낮은 서비스는 과감히 해지하는 것이 좋습니다.";
		}

		// 저축 목표 설정 관련 질문
		if (question.contains("저축 목표 설정")) {
			return "저축 목표를 설정하는 것이 중요합니다. 매달 일정 금액을 저축하고, 불필요한 지출을 줄이는 습관을 들이세요. 목표 금액을 달성하기 위해 꾸준히 노력하다 보면 어느새 큰 돈이 모일 거예요.";
		}

		// 기타 질문에 대한 기본 답변
		return "죄송합니다. 해당 질문에 대한 답변을 찾을 수 없습니다. 더 자세한 정보가 필요하다면 추가 질문을 남겨주세요.";
	}

	@Override
	public QnADTO getQnAById(Long qnaId) {
		QnA qna = qnaRepository.findById(qnaId)
			.orElseThrow(() -> new IllegalArgumentException("QnA not found with id: " + qnaId));
		return convertToDTO(qna);
	}

	private QnADTO convertToDTO(QnA qna) {
		QnADTO qnaDTO = new QnADTO();
		BeanUtils.copyProperties(qna, qnaDTO);
		return qnaDTO;
	}
}