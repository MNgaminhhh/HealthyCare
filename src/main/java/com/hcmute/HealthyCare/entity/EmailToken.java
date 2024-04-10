package com.hcmute.HealthyCare.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EmailToken")
public class EmailToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String token;

    @ManyToOne
    @JoinColumn(name = "account_email", referencedColumnName = "email")
    private Account account;

    private LocalDateTime expiryDate;
}
