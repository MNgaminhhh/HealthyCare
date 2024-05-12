package com.hcmute.HealthyCare.util;

import com.hcmute.HealthyCare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    @Autowired
    private UserRepository userRepository;

//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> (UserDetails) userRepository.findByEmail(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//    }
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return username -> {
//            System.out.println("Searching for user: " + username); // Add this line
//            UserDetails userDetails = (UserDetails) userRepository.findByEmail(username)
//                    .orElseThrow(() -> {
//                        System.out.println("User not found: " + username); // Add this line
//                        return new UsernameNotFoundException("User not found");
//                    });
//            System.out.println("Found user: " + userDetails); // Add this line
//            return userDetails;
//        };
//    }


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

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder builder) throws Exception {
//        builder.userDetailsService(this.userDetailsService()).passwordEncoder(passwordEncoder());
//        return builder.build();
//    }
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
}
