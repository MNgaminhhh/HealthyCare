package com.hcmute.HealthyCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.EmailToken;

public interface EmailTokenRepository extends JpaRepository<EmailToken, Long> {
    EmailToken findByToken(String token);
    boolean existsByToken(String token);
}

