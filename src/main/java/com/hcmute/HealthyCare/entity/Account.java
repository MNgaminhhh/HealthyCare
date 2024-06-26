    package com.hcmute.HealthyCare.entity;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;
    import jakarta.persistence.*;

    import com.fasterxml.jackson.annotation.JsonIgnore;
    import com.hcmute.HealthyCare.enums.Rolee;

    @Data
    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @Table(name = "Account")
    public class Account{

        @Id
        @Column(unique = true)
        private String email;

        private String password;
        private String avatar;

        @Enumerated(EnumType.STRING)
        private Rolee role;
        private boolean isadmin;
        private boolean verified;
        public Account(String email, String password, String avatar, Rolee role) {
            this.email = email;
            this.password = password;
            this.avatar = avatar;
            this.role = role;
            this.isadmin = false;
            this.verified = false;
        }
        @OneToOne(mappedBy = "account")
        @JsonIgnore
        private EmailToken emailToken;

        @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
        @JsonIgnore
        private Patient patient;

        @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
        @JsonIgnore
        private Doctor doctor;

        
    }
