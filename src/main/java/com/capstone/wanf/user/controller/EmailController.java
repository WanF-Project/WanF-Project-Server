package com.capstone.wanf.user.controller;

import com.capstone.wanf.user.dto.request.CodeRequest;
import com.capstone.wanf.user.dto.request.EmailRequest;
import com.capstone.wanf.user.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class EmailController {
    EmailService emailService;

    @PostMapping("/signup/verification-code")
    public ResponseEntity<Void> sendVerificationCode(@Valid @RequestBody EmailRequest emailRequest) {
        // 인증번호 생성 및 전송
        String verificationCode = emailService.generateVerificationCode();
        emailService.sendVerificationCode(emailRequest, verificationCode);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup/verification")
    public ResponseEntity<Void> verify(@RequestBody CodeRequest codeRequest) {
        emailService.verify(codeRequest);        // 인증번호 검증
        return ResponseEntity.ok().build();
    }
}
