package com.room7.moneygement.serviceImpl;

import org.springframework.stereotype.Service;

import com.room7.moneygement.repository.QnARepository;
import com.room7.moneygement.service.QnAService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnAServiceImpl implements QnAService {
	private final QnARepository qnaRepository;

}
