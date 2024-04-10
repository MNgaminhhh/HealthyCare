package com.hcmute.HealthyCare.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phone;
    private LocalDate birthday;
    private String gender;
    private String underlyingDisease;
    @OneToOne
    @JoinColumn(name = "account_email")
    private Account account;

    @OneToMany(mappedBy = "patient") 
    @JsonManagedReference 
    private List<Appointment> appointments; 
}

