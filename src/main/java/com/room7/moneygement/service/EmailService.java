package com.room7.moneygement.service;

import com.room7.moneygement.model.User;

public interface EmailService {
    String createEmailToken(String username);

    void sendVerificationEmail(User user);

    void sendPasswordVerificationEmail(String email, String code);

}
