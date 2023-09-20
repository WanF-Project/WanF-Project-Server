package com.capstone.wanf.user.controller;

import com.capstone.wanf.user.dto.request.CodeRequest;
import com.capstone.wanf.user.dto.request.EmailRequest;
import com.capstone.wanf.user.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "이메일 인증", description = "이메일 인증 API")
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/signup/verification-code")
    @Operation(summary = "인증번호 생성 & 전송")
    public ResponseEntity<Boolean> sendVerificationCode(@Valid @RequestBody EmailRequest emailRequest) {
        String verificationCode = emailService.generateVerificationCode();

        boolean isSuccess = emailService.sendVerificationCode(emailRequest, verificationCode);

        return ResponseEntity.ok(isSuccess);
    }

    @PostMapping("/signup/verification")
    @Operation(summary = "인증번호가 유효한지 검증")
    public ResponseEntity<Boolean> verify(@RequestBody CodeRequest codeRequest) {
        boolean isSuccess = emailService.verify(codeRequest);

        return ResponseEntity.ok(isSuccess);
    }
}