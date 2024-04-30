package com.room7.moneygement.serviceImpl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.room7.moneygement.model.User;
import com.room7.moneygement.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class EmailServiceImpl implements EmailService {

    private final String secretKey = "moneymoney";
    private final long expirationTime = 1000*60*10; //10분

    @Autowired
    private JavaMailSender mailSender;

    public String createEmailToken(String userEmail){
        return JWT.create()
                .withSubject(userEmail)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC512(secretKey.getBytes())); // JWT 토큰 서명을 SHA-512 알고리즘을 사용하여 서명함
    }

    public void sendVerificationEmail(User user) {
        String token = createEmailToken(user.getEmail());
        String verifyUrl = "http://localhost:8080/users/verifyEmail?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("회원가입 인증메일");
        message.setText("인증 링크: " + verifyUrl);
        mailSender.send(message);
    }
}