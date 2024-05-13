package com.hcmute.HealthyCare.apicontroller;

import com.google.firebase.database.*;
import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Message;
import com.hcmute.HealthyCare.entity.User;
import com.hcmute.HealthyCare.entity.UserInfoDetails;
import com.hcmute.HealthyCare.service.MessageService;
import com.hcmute.HealthyCare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/messages")
public class ApiMessageController {
    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;
    private final DatabaseReference firebaseRef;

    public ApiMessageController(DatabaseReference firebaseRef) {
        this.firebaseRef = firebaseRef.child("Messages");
    }
    @PostMapping("/conversation/{conversationId}")
    public ResponseEntity<?> sendMessage(@PathVariable Long conversationId, @RequestBody Message messageDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                UserInfoDetails userInfo = (UserInfoDetails) authentication.getPrincipal();
                String userEmail = userInfo.getUsername();
                Account account = userService.loadAccount(userEmail);
                if (account != null) {
                    Message message = messageService.sendMessage(conversationId, messageDTO, account);
                    return ResponseEntity.ok(message);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to send message: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message: " + e.getMessage());
        }
    }


    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<Map<String, String>>> getConversationMessages(@PathVariable Long conversationId) {
        try {
            DatabaseReference conversationRef = firebaseRef.child(String.valueOf(conversationId));
            List<Map<String, String>> messages = new ArrayList<>();
            CompletableFuture<Void> future = new CompletableFuture<>();

            conversationRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                    String content = dataSnapshot.child("content").getValue(String.class);
                    String receiver = dataSnapshot.child("receiver").getValue(String.class);
                    boolean seen = dataSnapshot.child("seen").getValue(Boolean.class);
                    String sendOn = dataSnapshot.child("send").getValue(String.class);
                    Map<String, String> messageMap = new HashMap<>();
                    messageMap.put("content", content);
                    messageMap.put("receiver", receiver);
                    messageMap.put("seen", String.valueOf(seen));
                    messageMap.put("sendOn", sendOn);
                    messages.add(messageMap);
                    future.complete(null);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    future.completeExceptionally(databaseError.toException());
                }
            });

            future.join();
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
