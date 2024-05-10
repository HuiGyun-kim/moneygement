package com.room7.moneygement.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.room7.moneygement.dto.CategoryDTO;
import com.room7.moneygement.repository.CategoryRepository;
import com.room7.moneygement.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;

	@Override
	public List<CategoryDTO> getAllCategories() {
		return categoryRepository.findAll().stream()
			.map(category -> new CategoryDTO(category.getCategoryId(), category.getCategoryName()))
			.collect(Collectors.toList());
	}
}
