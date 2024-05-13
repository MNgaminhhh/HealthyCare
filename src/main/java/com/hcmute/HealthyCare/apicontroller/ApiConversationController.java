package com.hcmute.HealthyCare.apicontroller;

import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Conversation;
import com.hcmute.HealthyCare.service.ConversationService;
import com.hcmute.HealthyCare.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiConversationController {

    @Autowired
    private ConversationService conversationService;
    @Autowired
    private UserService userService;
    @GetMapping("/conversation")
    public ResponseEntity<List<Conversation>> findAll() {
        return new ResponseEntity<>(conversationService.findAll(), HttpStatus.OK);
    }
    @GetMapping("/conversation/find")
    public ResponseEntity<Conversation> getConversationOf(@RequestParam("email1") String email1, @RequestParam("email2") String email2) {
        Account acc1 = userService.loadAccount(email1);
        Account acc2 = userService.loadAccount(email2);
        Conversation newConConversation = conversationService.findidconversation(acc1, acc2);
        if (newConConversation!=null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(newConConversation);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @PostMapping("/conversation")
    public ResponseEntity<Conversation> createConversation(@RequestParam("email1") String email1, @RequestParam("email2") String email2, @RequestBody Conversation conversation) {
        Account acc1 = userService.loadAccount(email1);
        Account acc2 = userService.loadAccount(email2);
        Conversation newConConversation = conversationService.addConversation(acc1, acc2, conversation);
        if (newConConversation!=null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(newConConversation);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}