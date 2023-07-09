package com.capstone.wanf.user.controller;

import com.capstone.wanf.user.dto.request.CodeRequest;
import com.capstone.wanf.user.dto.request.EmailRequest;
import com.capstone.wanf.user.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/signup/verification-code")
    @Operation(
            summary = "인증번호 생성 & 전송",
            description = "인증번호를 생성하여, 입력한 이메일로 인증번호를 전송합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "409", ref = "409")
            }
    )
    public ResponseEntity<Boolean> sendVerificationCode(@Valid @RequestBody EmailRequest emailRequest) {
        String verificationCode = emailService.generateVerificationCode();      // 인증번호 생성

        boolean isSuccess = emailService.sendVerificationCode(emailRequest, verificationCode);      // 인증번호 전송

        return ResponseEntity.ok(isSuccess);
    }

    @PostMapping("/signup/verification")
    @Operation(
            summary = "인증번호 검증",
            description = "입력된 인증번호가 유효한지 검증합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "400", ref = "400"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<Boolean> verify(@RequestBody CodeRequest codeRequest) {
        boolean isSuccess = emailService.verify(codeRequest);        // 인증번호 검증

        return ResponseEntity.ok(isSuccess);
    }
}