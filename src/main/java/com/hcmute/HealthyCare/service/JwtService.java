package com.hcmute.HealthyCare.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    public String generateToken(String email) {
        return JWT.create()
                .withSubject(email)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(Algorithm.HMAC512(secretKey.getBytes()));
    }
    public String extractEmail(String token) {
        return JWT.require(Algorithm.HMAC512(secretKey.getBytes()))
                .build()
                .verify(token)
                .getSubject();
    }
}
