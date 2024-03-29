package com.capstone.wanf.message.controller;

import com.capstone.wanf.common.annotation.CurrentUser;
import com.capstone.wanf.message.dto.request.MessageRequest;
import com.capstone.wanf.message.service.MessageProducerService;
import com.capstone.wanf.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "쪽지 송신", description = "쪽지 송신 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class MessageProduceController {
    private final MessageProducerService messageProducerService;

    @PostMapping("/messages")
    @Operation(
            summary = "쪽지를 송신",
            description = "쪽지를 송신합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<String> publish(@RequestBody MessageRequest messageRequest, @CurrentUser User sender){
        messageProducerService.sendMessage(messageRequest, sender);

        return ResponseEntity.ok("success");
    }
}
