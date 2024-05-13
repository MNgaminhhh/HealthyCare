package com.hcmute.HealthyCare.repository;

import com.hcmute.HealthyCare.entity.Account;
import com.hcmute.HealthyCare.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Conversation findByUser1AndUser2(Account acc1, Account acc2);
}
