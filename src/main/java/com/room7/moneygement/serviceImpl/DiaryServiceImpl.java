package com.room7.moneygement.serviceImpl;

import com.room7.moneygement.dto.DiaryDTO;
import com.room7.moneygement.model.Diary;
import org.springframework.stereotype.Service;

import com.room7.moneygement.repository.DiaryRepository;
import com.room7.moneygement.service.DiaryService;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

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
		diary.setExpenseAt(LocalDateTime.now());
		return diaryRepository.save(diary);
	}
}