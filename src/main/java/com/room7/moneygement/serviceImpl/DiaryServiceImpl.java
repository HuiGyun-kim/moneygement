package com.room7.moneygement.serviceImpl;

import com.room7.moneygement.dto.DiaryDTO;
import com.room7.moneygement.model.Diary;
import org.springframework.stereotype.Service;

import com.room7.moneygement.repository.DiaryRepository;
import com.room7.moneygement.service.DiaryService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {
	private final DiaryRepository diaryRepository;

	public Diary saveDiary(DiaryDTO diaryDTO) {
		Diary diary = new Diary();
		diary.setUserId(diaryDTO.getUserId());
		diary.setContent(diaryDTO.getContent());
		diary.setCreatedAt(LocalDateTime.now());
		diary.setUpdatedAt(LocalDateTime.now());
		diary.setExpenseAt(diaryDTO.getExpenseAt());
		return diaryRepository.save(diary);
	}

	public List<Diary> checkDiary(LocalDate date, Long userId) {
		LocalDateTime startOfDay = date.atStartOfDay();
		LocalDateTime endOfDay = date.atTime(23, 59, 59);
		return diaryRepository.findByExpenseAtAndUserId(startOfDay, endOfDay, userId);
	}
}