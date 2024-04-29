package com.room7.moneygement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.room7.moneygement.model.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
