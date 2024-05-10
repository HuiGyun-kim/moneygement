package com.room7.moneygement.service;

import com.room7.moneygement.dto.QnADTO;

public interface QnAService {
    QnADTO saveQnA(QnADTO qnadto);
    QnADTO getQnAById(Long qnaId);
}