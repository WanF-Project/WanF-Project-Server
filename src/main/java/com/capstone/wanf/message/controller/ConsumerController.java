package com.capstone.wanf.message.controller;

import com.capstone.wanf.common.annotation.CurrentUser;
import com.capstone.wanf.message.dto.response.ReceiverMessageResponse;
import com.capstone.wanf.message.service.ConsumerService;
import com.capstone.wanf.profile.dto.response.ProfileResponse;
import com.capstone.wanf.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @GetMapping("/messages/senders/{id}")
    @Operation(
            summary = "송신자와 주고 받은 쪽지를 조회",
            description = "송신자와 주고 받은 쪽지들을 조회합니다. 최신 순으로 정렬되어 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<ReceiverMessageResponse> getMessages(@CurrentUser User user, @PathVariable(name = "id") Long senderId) {
        ReceiverMessageResponse message = consumerService.getMessage(user, senderId);

        return ResponseEntity.ok(message);
    }

    @GetMapping("/messages/senders")
    @Operation(
            summary = "쪽지를 주고받은 사람들을 조회",
            description = "쪽지를 주고 받은 사람들을 조회합니다. 최신 순으로 정렬되어 있습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<List<ProfileResponse>> getSenders(@CurrentUser User user) {
        List<ProfileResponse> message = consumerService.getSenders(user);

        return ResponseEntity.ok(message);
    }
}
