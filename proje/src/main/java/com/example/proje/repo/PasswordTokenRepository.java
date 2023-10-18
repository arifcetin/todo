package com.example.proje.repo;

import com.example.proje.entities.PasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface PasswordTokenRepository extends JpaRepository<PasswordToken,Long> {
    PasswordToken findTokenByToken(String token);

}
