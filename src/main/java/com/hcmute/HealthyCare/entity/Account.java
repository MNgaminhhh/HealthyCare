package com.hcmute.HealthyCare.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.hcmute.HealthyCare.enums.Rolee;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Account")
public class Account {

    @Id
    @Column(unique = true)
    private String email;

    private String password;
    private String avatar;

    @Enumerated(EnumType.STRING)
    private Rolee role;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Patient patient;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Doctor doctor;
}
