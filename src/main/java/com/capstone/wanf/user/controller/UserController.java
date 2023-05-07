package com.capstone.wanf.user.controller;

import com.capstone.wanf.auth.jwt.dto.response.TokenResponse;
import com.capstone.wanf.auth.jwt.service.AuthService;
import com.capstone.wanf.user.dto.request.UserRequest;
import com.capstone.wanf.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "회원가입 완료",
            description = "회원가입 마지막 절차로 비밀번호를 입력하고 회원가입이 완료됩니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<Void> signUp(@Valid @RequestBody UserRequest userRequest) {
        userService.updateUserPassword(userRequest);        // 회원가입 완료

        return ResponseEntity.ok().build();
    }

    // 로그인 -> 토큰 발급
    @PostMapping("/login")
    @Operation(
            summary = "로그인",
            description = "로그인이 성공하면 Access Token과 Refresh Token을 헤더에 담아서 전달합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "500", ref = "500")
            }
    )
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
    @Operation(
            summary = "AT 만료 여부 확인",
            description = "Access Token의 만료 여부만 확인하는 api입니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "401", ref = "401")
            }
    )
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String requestAccessToken) {
        if (!authService.validate(requestAccessToken)) {
            return ResponseEntity.status(HttpStatus.OK).build(); // 재발급 필요X
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 재발급 필요
        }
    }

    // 토큰 재발급
    @PostMapping("/reissue")
    @Operation(
            summary = "토큰 재발급",
            description = "Access Token과 Refresh Token을 검증하고, 재발급받아서 헤더에 담아 전송합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "401", ref = "401")
            }
    )
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
    @Operation(
            summary = "로그아웃",
            description = "Redis에 저장된 Refresh Token이 삭제되고, 로그아웃한 Access Token을 저장합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공")
            }
    )
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String requestAccessToken) {
        authService.logout(requestAccessToken);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }
}
