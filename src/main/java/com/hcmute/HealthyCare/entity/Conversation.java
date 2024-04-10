package com.hcmute.HealthyCare.entity;

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
    @JoinColumn(name = "user1")
    private Account user1;

    @ManyToOne
    @JoinColumn(name = "user2")
    private Account user2;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Message> messages;

}
