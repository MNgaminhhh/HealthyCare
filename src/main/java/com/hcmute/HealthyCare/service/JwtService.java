package com.hcmute.HealthyCare.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hcmute.HealthyCare.entity.Account;

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
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String getEmailFromToken(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
        return jwt.getSubject();
    }

    public boolean validateToken(String token, Account account) {
        final String email = getEmailFromToken(token);
        return (email.equals(account.getEmail()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
        return jwt.getExpiresAt().before(new Date());
    }
}
