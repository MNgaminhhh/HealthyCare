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

    @Column(unique = true)
    private String code;

    @Column(unique = true)
    private String email;

    private LocalDateTime expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Override
    public String toString() {
        return "EmailToken{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", code='" + code + '\'' +
                ", email='" + email + '\'' +
                ", expiryDate=" + expiryDate +
                ", account_email=" + (account != null ? account.getEmail() : null) +
                '}';
    }
}
