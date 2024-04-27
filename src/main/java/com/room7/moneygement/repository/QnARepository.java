package com.room7.moneygement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.room7.moneygement.model.QnA;

@Repository
public interface QnARepository extends JpaRepository<QnA, Long> {

}


