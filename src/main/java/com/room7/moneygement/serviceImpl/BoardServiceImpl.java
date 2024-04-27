package com.room7.moneygement.serviceImpl;

import org.springframework.stereotype.Service;

import com.room7.moneygement.repository.BoardRepository;
import com.room7.moneygement.service.BoardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
	private final BoardRepository boardRepository;

}

