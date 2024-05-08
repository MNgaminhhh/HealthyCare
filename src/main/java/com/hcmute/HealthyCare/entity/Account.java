package com.hcmute.HealthyCare.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hcmute.HealthyCare.enums.Rolee;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Account")
public class Account implements UserDetails{

    @Id
    @Column(unique = true)
    private String email;

    private String password;
    private String avatar;

    @Enumerated(EnumType.STRING)
    private Rolee role;

    private boolean verified;
    @Transient
    private Collection<? extends GrantedAuthority> authorities;
    public Account(String email, String password, String avatar, Rolee role) {
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.role = role;
        this.verified = false;
    }
    @OneToOne(mappedBy = "account")
    private EmailToken emailToken;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnore
    private Patient patient;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    @JsonIgnore
    private Doctor doctor;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Return the authorities associated with this user
        return this.authorities;
    }

    @Override
    public String getPassword() {
        // Return the password associated with this user
        return this.password;
    }

    @Override
    public String getUsername() {
        // Return the username associated with this user, in your case, it could be the email
        return this.email;
    }

    // Implement other UserDetails methods as necessary

    @Override
    public boolean isAccountNonExpired() {
        // Implement account non-expired logic
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Implement account non-locked logic
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Implement credentials non-expired logic
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Implement enabled logic
        return true;
    }

}
