package com.hcmute.HealthyCare.entity;

import java.time.LocalDate;

import com.hcmute.HealthyCare.enums.Rolee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String email;
    private String password;
    private String avatar;
    private Rolee role;
    private String name;
    private String address;
    private String phone;
    private LocalDate birthday;
    private String gender;
    private String education;
    private Float numberofyear;
    private String workplace;
    private String introduction;
    private String specially;
    private String underlyingDisease;
}
