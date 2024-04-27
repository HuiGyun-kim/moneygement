package com.room7.moneygement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.room7.moneygement.model.Social;

@Repository
public interface SocialRepository extends JpaRepository<Social, Long> {

}

