package com.hcmute.HealthyCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hcmute.HealthyCare.entity.Account;

public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByEmailAndPassword(String email, String password); 
    Account findByEmail(String email); 
}
