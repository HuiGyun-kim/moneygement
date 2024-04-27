package com.room7.moneygement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.room7.moneygement.model.Receipt;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

}

