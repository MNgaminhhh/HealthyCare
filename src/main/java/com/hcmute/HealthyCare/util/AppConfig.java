package com.hcmute.HealthyCare.util;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcmute.HealthyCare.HealthyCareApplication;
import com.hcmute.HealthyCare.service.JwtService;
import com.hcmute.HealthyCare.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import java.io.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class AppConfig {
    @Autowired
    private JwtAuthFilter authFilter;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService() ;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeHttpRequests()
                .requestMatchers("/reset-password","/forgot-password","/api/forgot-password","/api/reset-password","/verification","/api/register","/api/resend","/register",
                        "/api/email/add","/api/getAllBlog","/api/account/**","/api/search","/search","/api/alluser", "/api/email/check","/api/user/**","/api/alluser", "/fonts/**","/src/**", "/css/**", "/img/**", "/register","/api/email/checktoken","/","/api/login", "/api/updateAppointmentStatus").permitAll()
                .requestMatchers("/doctor/**","/api/**","/setting","/api/info","/profile","/community/**","/community/addBlog","/api/createNewBlog","/api/createNewComment", "/api/getCommentByBlog", "/schedule/**",
                        "/api/getDoctorByEmail", "/api/createAppointment", "/api/getAppointmentOfUser", "/api/deleteAppointment","/api/getAppointmentById", "/api/deleteBlog", "/api/editBlog").authenticated()
                .requestMatchers("/doctor/**","/api/**","/setting","/api/info","/profile","/community/**","/community/addBlog","/api/createNewBlog","/api/createNewComment", "/api/getCommentByBlog", "/schedule/**",
                        "/api/getDoctorByEmail", "/api/createAppointment", "/api/getAppointmentOfUser", "/api/deleteAppointment","/api/getAppointmentById", "/api/deleteBlog").authenticated()
                .requestMatchers("/message","/message/**").authenticated()
                .requestMatchers("/admin/account","/admin","/admin/post").authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("jwt")
                .permitAll()
                .and()
                .build();
    }


    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DatabaseReference firebaseDatabaseReference() throws IOException {
        ClassLoader classLoader = HealthyCareApplication.class.getClassLoader();
        InputStream serviceAccountStream = classLoader.getResourceAsStream("serviceAccountKey.json");

        if (serviceAccountStream == null) {
            throw new FileNotFoundException("Firebase service account key file not found");
        }

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                .setDatabaseUrl("https://healthycare-16dac-default-rtdb.firebaseio.com/")
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }

        return FirebaseDatabase.getInstance().getReference();
    }


}
