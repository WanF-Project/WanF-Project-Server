package com.capstone.wanf.message.domain.repo;

import com.capstone.wanf.message.domain.entity.Message;
import com.capstone.wanf.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findMessagesByReceiverAndSender(User receiver, User sender);
}

