package com.capstone.wanf.user.controller;

import com.capstone.wanf.user.dto.request.UserRequest;
import com.capstone.wanf.user.service.UserService;
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
public class UserController {
    private final UserService userService;

    @PostMapping("/signup/user")
    public ResponseEntity<Void> signUp(@Valid @RequestBody UserRequest userRequest) {
        userService.updateUserPassword(userRequest);        // 회원가입 완료

        return ResponseEntity.ok().build();
    }
}
