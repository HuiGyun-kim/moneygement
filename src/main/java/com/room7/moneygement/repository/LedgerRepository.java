package com.room7.moneygement.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.room7.moneygement.dto.LedgerDTO;
import com.room7.moneygement.model.Ledger;
import com.room7.moneygement.model.User;

@Repository
public interface LedgerRepository extends JpaRepository<Ledger, Long> {
	Page<Ledger> findByUserId(User userId, Pageable pageable);
}

