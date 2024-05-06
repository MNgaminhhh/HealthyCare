package com.hcmute.HealthyCare.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import com.hcmute.HealthyCare.service.UserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AppConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable() 
            .authorizeRequests() 
            .anyRequest().permitAll()
            .and()
            .build();

        // return http
        //     .authorizeRequests()
        //         .antMatchers("/api/login").permitAll() // Cho phép tất cả mọi người truy cập /api/login
        //         .anyRequest().authenticated() // Các request còn lại phải xác thực mới được phép truy cập
        //         .and()
        //     .formLogin()
        //         .loginPage("/login")
        //         .permitAll() // Cho phép tất cả mọi người truy cập trang đăng nhập
        //         .and()
        //     .logout()
        //         .permitAll();
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
