package com.example.proje.services;

import com.example.proje.entities.PasswordToken;
import com.example.proje.entities.User;
import com.example.proje.repo.PasswordTokenRepository;
import com.example.proje.repo.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

@Service
public class PasswordTokenService {
    @Value("${password.token.expires.in}")
    private Long expireSeconds;
    private final UserRepository userRepository;

    private final PasswordTokenRepository passwordTokenRepository;

    public PasswordTokenService(UserRepository userRepository, PasswordTokenRepository passwordTokenRepository) {
        this.userRepository = userRepository;
        this.passwordTokenRepository = passwordTokenRepository;
    }

    public boolean validateToken(PasswordToken passwordToken){
        return passwordToken.getExpiryDate().before(new Date());
    }

    public String createPasswordToken(User user){
        Random random = new Random();
        PasswordToken passwordToken = new PasswordToken();
        passwordToken.setUser(user);
        passwordToken.setToken(String.valueOf(random.nextInt(90001)+10000));
        passwordToken.setExpiryDate(Date.from(Instant.now().plusSeconds(expireSeconds)));
        passwordTokenRepository.save(passwordToken);
        return passwordToken.getToken();
    }

    public PasswordToken findTokenByToken(String token) {
        return passwordTokenRepository.findTokenByToken(token);
    }
}
