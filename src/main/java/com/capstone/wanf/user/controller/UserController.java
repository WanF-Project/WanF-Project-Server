package com.capstone.wanf.user.controller;

import com.capstone.wanf.jwt.dto.response.TokenResponse;
import com.capstone.wanf.jwt.service.AuthService;
import com.capstone.wanf.user.dto.request.UserRequest;
import com.capstone.wanf.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    private final AuthService authService;

    @PostMapping("/signup/user")
    public ResponseEntity<Void> signUp(@Valid @RequestBody UserRequest userRequest) {
        userService.updateUserPassword(userRequest);        // 회원가입 완료

        return ResponseEntity.ok().build();
    }

    // 로그인 -> 토큰 발급
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserRequest loginDto) {
        // User 등록 및 Refresh Token 저장
        TokenResponse tokenDto = authService.login(loginDto);

        return ResponseEntity.ok()
                .header("X-Refresh-Token", tokenDto.getRefreshToken())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .build();
    }

    // AT가 만료되었는지의 여부만 판별
    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String requestAccessToken) {
        if (!authService.validate(requestAccessToken)) {
            return ResponseEntity.status(HttpStatus.OK).build(); // 재발급 필요X
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 재발급 필요
        }
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestHeader("X-Refresh-Token") String requestRefreshToken,
                                     @RequestHeader("Authorization") String requestAccessToken) {
        TokenResponse reissuedTokenDto = authService.reissue(requestAccessToken, requestRefreshToken);

        if (reissuedTokenDto != null) { // 토큰 재발급 성공
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("X-Refresh-Token", reissuedTokenDto.getRefreshToken())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + reissuedTokenDto.getAccessToken())
                    .build();
        } else { // Refresh Token 탈취 가능성
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String requestAccessToken) {
        authService.logout(requestAccessToken);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
