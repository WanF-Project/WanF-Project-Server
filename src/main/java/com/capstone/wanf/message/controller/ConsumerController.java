package com.capstone.wanf.message.controller;

import com.capstone.wanf.common.annotation.CurrentUser;
import com.capstone.wanf.message.dto.response.ReceiverMessageResponse;
import com.capstone.wanf.message.service.ConsumerService;
import com.capstone.wanf.profile.dto.response.ProfileResponse;
import com.capstone.wanf.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ConsumerController {
    private final ConsumerService consumerService;

    @GetMapping("/messages/{id}")
    public ResponseEntity<ReceiverMessageResponse> getMessages(@CurrentUser User user, @PathVariable(name = "id") Long senderId) {
        ReceiverMessageResponse message = consumerService.getMessage(user, senderId);

        return ResponseEntity.ok(message);
    }

    @GetMapping("/messages/senders")
    public ResponseEntity<List<ProfileResponse>> getSenders(@CurrentUser User user) {
        List<ProfileResponse> message = consumerService.getSenders(user);

        return ResponseEntity.ok(message);
    }
}
