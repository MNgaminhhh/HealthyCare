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
@Table(name = "Doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phone;
    private LocalDate birthday;
    private String gender;
    private String education;
    private String workplace;
    private String introduction;
    private String specially;
    @Column(name = "number_of_year")
    private Float  numberofyear;

    @OneToOne
    @JoinColumn(name = "account_email")
    private Account account;

    @OneToMany(mappedBy = "doctor")
    @JsonManagedReference
    private List<Appointment> appointments; 
    public Doctor(String name, String address, String phone, LocalDate birthday, String gender, String education, String workplace, String introduction, String specially, Account account) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.birthday = birthday;
        this.gender = gender;
        this.education = education;
        this.workplace = workplace;
        this.introduction = introduction;
        this.specially = specially;
        this.account = account;
    }
    
}
