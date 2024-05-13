package com.hcmute.HealthyCare.repository;

import com.hcmute.HealthyCare.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
