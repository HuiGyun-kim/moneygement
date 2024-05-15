package com.room7.moneygement.serviceImpl;

import java.util.Date;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.room7.moneygement.model.User;
import com.room7.moneygement.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final String secretKey = "moneymoney";
    private final long expirationTime = 1000*60*10; //10분

    private final JavaMailSender mailSender;

    public String createEmailToken(String userEmail){
        return JWT.create()
                .withSubject(userEmail)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC512(secretKey.getBytes())); // JWT 토큰 서명을 SHA-512 알고리즘을 사용하여 서명함
    }

    public void sendVerificationEmail(User user) {
        String token = createEmailToken(user.getEmail());
        String verifyUrl = "http://ec2-43-200-116-55.ap-northeast-2.compute.amazonaws.com:8080/users/verifyEmail?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("회원가입 인증메일");
        message.setText("인증 링크: " + verifyUrl);
        mailSender.send(message);
    }

    @Override
    public void sendPasswordVerificationEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("비밀번호 찾기 인증번호");
        message.setText("인증번호는 " + code + "입니다. 인증번호 입력란에 입력해주세요.");

        mailSender.send(message);
    }

    // ID 찾기용 인증번호 이메일 전송
    public void sendIdVerificationEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("ID 찾기 인증번호");
        message.setText("인증번호는 " + code + "입니다. 인증번호 입력란에 입력해주세요.");

        mailSender.send(message);
    }
    public void PasswordResetLink(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("비밀번호 찾기 인증번호");
        message.setText("인증번호는 " + code + "입니다. 인증번호 입력란에 입력해주세요.");

        mailSender.send(message);
    }


}