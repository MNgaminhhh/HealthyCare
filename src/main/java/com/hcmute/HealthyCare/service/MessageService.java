package com.hcmute.HealthyCare.service;

import com.hcmute.HealthyCare.entity.*;
import com.hcmute.HealthyCare.repository.BlogRepository;
import com.hcmute.HealthyCare.repository.ConversationRepository;
import com.hcmute.HealthyCare.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@Service
public class MessageService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    public Message sendMessage(Long conversationId, Message messageDTO, Account account) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserInfoDetails userInfo = (UserInfoDetails) authentication.getPrincipal();
            String userEmail = userInfo.getUsername();
        }
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

        Account user2 = conversation.getUser2();
        Account user1 = conversation.getUser1();
        LocalDateTime currentTime = LocalDateTime.now();
        Message message = new Message();
        message.setContent(messageDTO.getContent());
        message.setSendOn(currentTime);
        message.setSeen(false);
        message.setConversation(conversation);
        if (user2 != null && user2.getEmail().equals(account.getEmail())) {
            message.setReceiver(user1);
        } else if (user1 != null && user1.getEmail().equals(account.getEmail())) {
            message.setReceiver(user2);
        } else {
            message.setReceiver(user2);
        }
        Message savedMessage = messageRepository.save(message);
        sendToFirebase(savedMessage);

        return savedMessage;
    }
    private void sendToFirebase(Message message) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Messages");
        DatabaseReference conversationRef = ref.child(message.getConversation().getId().toString());
        DatabaseReference messageRef = conversationRef.child(generateMessageKey());

        messageRef.child("content").setValueAsync(message.getContent());
        messageRef.child("receiver").setValueAsync(message.getReceiver().getEmail());
        messageRef.child("send").setValueAsync(message.getSendOn().toString());
        messageRef.child("seen").setValueAsync(message.isSeen());
    }
    private String generateMessageKey() {
        return UUID.randomUUID().toString();
    }

    public List<Message> getConversationMessages(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

        return conversation.getMessages();
    }
}
