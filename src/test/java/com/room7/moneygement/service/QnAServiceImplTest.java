//package com.room7.moneygement.service;
//
//import com.room7.moneygement.repository.QnARepository;
//import com.room7.moneygement.serviceImpl.QnAServiceImpl;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.stubbing.OngoingStubbing;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class QnAServiceImplTest {
//
//    @Mock
//    private QnARepository qnaRepository;
//
//    private QnAServiceImpl qnaService;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//        qnaService = new QnAServiceImpl(qnaRepository);
//    }
//
//    @Test
//    public void testAskQuestion() throws Exception{
//        String question = "What is the weather today?";
//        String expectedAnswer = "It's sunny today.";
//
//        SseEmitter emitter = qnaService.askQuestion(question);
//
//        // 비동기적으로 응답을 처리하기 위해 onCompletion 메서드를 사용합니다.
//        emitter.onCompletion(() -> {
//            System.out.println("Request completed");
//        });
//
//        // 예상한 답변을 받으면 테스트를 성공으로 처리합니다.
//        emitter.onCompletion(() -> {
//            assertEquals(expectedAnswer, response.getBody());
//        });
//
//        verify(qnaRepository, times(1)).save(any());
//    }
//}