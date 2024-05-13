package com.hcmute.HealthyCare.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Conversation")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1", nullable = false)
    private Account user1;

    @ManyToOne
    @JoinColumn(name = "user2", nullable = false)
    private Account user2;

    @OneToMany(mappedBy = "conversation")
    private List<Message> messages;
}
