package com.room7.moneygement.service;

import java.util.List;

public interface FaqService {
    String generateAnswer(String question, Long userId);
    List<String> getFrequentlyAskedQuestions();
}