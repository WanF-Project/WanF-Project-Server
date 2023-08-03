package com.capstone.wanf.message.controller;

import com.capstone.wanf.common.annotation.CurrentUser;
import com.capstone.wanf.message.domain.entity.MessageUtils;
import com.capstone.wanf.message.dto.request.MessageRequest;
import com.capstone.wanf.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProduceController {

    @Autowired
    private KafkaTemplate<String, MessageUtils> kafkaTemplate;

    @PostMapping("/message")
    public ResponseEntity<String> publish(@RequestBody MessageRequest messageRequest, @CurrentUser User sender){
        kafkaTemplate.send("message", messageRequest.receiverProfileId().toString(), MessageUtils.builder()
                .receiverId(messageRequest.receiverProfileId())
                .content(messageRequest.content())
                .sender(sender)
                .build());
        // TODO: 2023/07/18 callback 함수로 성공시 FCM을 날리자 실패하면 예외
        return ResponseEntity.ok("success");
    }
}
