package com.room7.moneygement.service;

import java.util.List;

import com.room7.moneygement.dto.QnADTO;
import com.room7.moneygement.model.LedgerEntry;

public interface QnAService {
    QnADTO saveQnA(QnADTO qnaDTO);
    String generateAnswer(String question, List<LedgerEntry> ledgerEntries, Long userId);

    QnADTO getQnAById(Long qnaId);
}