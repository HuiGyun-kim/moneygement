package com.room7.moneygement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.room7.moneygement.model.Ledger;
import com.room7.moneygement.model.User;

@Repository
public interface LedgerRepository extends JpaRepository<Ledger, Long> {
	List<Ledger> findByUserId(User userId);

	@Query("SELECT l.ledgerId FROM Ledger l WHERE l.userId.userId = :userId")
	List<Long> findLedgerIdsByUserId(@Param("userId") Long userId);
}

