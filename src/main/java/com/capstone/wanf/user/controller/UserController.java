package com.capstone.wanf.user.controller;

import com.capstone.wanf.user.dto.request.CodeRequest;
import com.capstone.wanf.user.dto.request.EmailRequest;
import com.capstone.wanf.user.dto.request.UserRequest;
import com.capstone.wanf.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/signup/send-verification-code")
    public ResponseEntity<Void> sendVerificationCode(@RequestBody EmailRequest emailRequest) {
        // 인증번호 생성 및 전송
        String verificationCode = userService.generateVerificationCode();
        userService.sendVerificationCode(emailRequest, verificationCode);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup/verify")
    public ResponseEntity<Void> verify(@RequestBody CodeRequest codeRequest) {
        userService.verify(codeRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup/final")
    public ResponseEntity<Void> signUp(@RequestBody UserRequest userRequest) {
        userService.update(userRequest);
        return ResponseEntity.ok().build();
    }
}
