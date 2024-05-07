package com.room7.moneygement.service;

import com.room7.moneygement.repository.QnARepository;
import com.room7.moneygement.serviceImpl.QnAServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;

@SpringBootTest
public class QnAServiceImplTest {

    @Mock
    private QnARepository qnaRepository;

    private QnAServiceImpl qnaService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        qnaService = new QnAServiceImpl(qnaRepository);
    }

    @Test
    public void testAskQuestion() {
        String question = "What is the weather today?";
        String expectedAnswer = "It's sunny today.";

        when(qnaService.askQuestion(question)).thenReturn(expectedAnswer);

        String actualAnswer = qnaService.askQuestion(question);

        Assertions.assertEquals(expectedAnswer, actualAnswer);
        verify(qnaRepository, times(1)).save(any());
    }
}