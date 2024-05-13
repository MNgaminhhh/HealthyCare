package com.hcmute.HealthyCare.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Conversation;
import com.hcmute.HealthyCare.repository.ConversationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private UserService userService;
    public List<Conversation> findAll() {
        return conversationRepository.findAll();
    }
    public Conversation addConversation(Account acc1, Account acc2, Conversation newConversation) {
        if (acc1 != null && acc2 != null) {
            if (acc1.getEmail().compareTo(acc2.getEmail()) < 0) {
                newConversation.setUser1(acc1);
                newConversation.setUser2(acc2);
            } else {
                newConversation.setUser1(acc2);
                newConversation.setUser2(acc1);
            }
            Conversation savedConversation = conversationRepository.save(newConversation);
            saveConversationFirebase(savedConversation);
            return savedConversation;
        } else {
            throw new IllegalArgumentException("One or both accounts do not exist");
        }
    }
    public Conversation findidconversation(Account acc1, Account acc2) {
        Conversation conversation = conversationRepository.findByUser1AndUser2(acc1, acc2);
        if (conversation == null) {
            conversation = conversationRepository.findByUser1AndUser2(acc2, acc1);
        }
        return conversation;
    }

    public void saveConversationFirebase(Conversation conversation) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("conversations");
        DatabaseReference conversationRef = ref.child(conversation.getId().toString());
        conversationRef.child("user1").setValueAsync(conversation.getUser1().getEmail());
        conversationRef.child("user2").setValueAsync(conversation.getUser2().getEmail());
    }

}
