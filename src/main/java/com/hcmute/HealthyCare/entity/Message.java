package com.hcmute.HealthyCare.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;
    @Column(name = "send_on")
    private LocalDateTime sendOn; 
    private boolean seen; 

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Account receiver;
    
    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

}
