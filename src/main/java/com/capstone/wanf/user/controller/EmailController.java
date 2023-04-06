package com.capstone.wanf.user.controller;

import com.capstone.wanf.user.dto.request.CodeRequest;
import com.capstone.wanf.user.dto.request.EmailRequest;
import com.capstone.wanf.user.service.EmailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/signup/verification-code")
    public ResponseEntity<Void> sendVerificationCode(@Valid @RequestBody EmailRequest emailRequest) {
        String verificationCode = emailService.generateVerificationCode();      // 인증번호 생성

        emailService.sendVerificationCode(emailRequest, verificationCode);      // 인증번호 전송

        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup/verification")
    public ResponseEntity<Void> verify(@RequestBody CodeRequest codeRequest) {
        emailService.verify(codeRequest);        // 인증번호 검증

        return ResponseEntity.ok().build();
    }
}
