package com.room7.moneygement.serviceImpl;

import org.springframework.stereotype.Service;

import com.room7.moneygement.repository.CommentRepository;
import com.room7.moneygement.service.CommentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;

}

