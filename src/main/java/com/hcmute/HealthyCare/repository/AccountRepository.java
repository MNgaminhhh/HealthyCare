package com.hcmute.HealthyCare.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hcmute.HealthyCare.entity.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Account findByEmailAndPassword(String email, String password); 
    Account findByEmail(String email); 
    boolean existsByEmailAndAndPassword(String email, String password);

    @Query("SELECT a FROM Account a Where a.doctor.id = :id")
    Optional<Account> findAccountByDoctor(@Param("id") Long dId);

    @Query("SELECT a FROM Account a Where a.patient.id = :id")
    Optional<Account> findAccountByPatient(@Param("id") Long pId);
}
