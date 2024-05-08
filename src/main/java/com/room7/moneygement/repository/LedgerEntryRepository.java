package com.room7.moneygement.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.room7.moneygement.model.Ledger;
import com.room7.moneygement.model.LedgerEntry;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
    List<LedgerEntry> findByDate(LocalDate date);
    @Query("SELECT le FROM LedgerEntry le WHERE le.date = :date AND le.ledgerId.userId.userId = :userId")
    List<LedgerEntry> findByDateAndUserId(LocalDate date, Long userId);
}

