package com.hcmute.HealthyCare.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .sign(Algorithm.HMAC512(secretKey.getBytes()));
    }
    public String extractEmail(String token) {
        try {
            return JWT.require(Algorithm.HMAC512(secretKey.getBytes()))
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (TokenExpiredException e) {
            return null;
        }
    }
    
}
