package com.capstone.wanf.user.controller;

import com.capstone.wanf.auth.jwt.dto.response.TokenResponse;
import com.capstone.wanf.auth.jwt.service.AuthService;
import com.capstone.wanf.common.annotation.CurrentUser;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.service.ProfileService;
import com.capstone.wanf.user.domain.entity.User;
import com.capstone.wanf.user.dto.request.UserRequest;
import com.capstone.wanf.user.dto.response.LoginResponse;
import com.capstone.wanf.user.dto.response.UserResponse;
import com.capstone.wanf.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증 & 인가", description = "인증 & 인가 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final AuthService authService;

    private final ProfileService profileService;

    @PostMapping("/signup/user")
    @Operation(summary = "회원가입 완료")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody UserRequest userRequest) {
        User newUser = userService.updateUserPassword(userRequest);

        TokenResponse token = authService.login(userRequest);

        return ResponseEntity.ok()
                .header("X-Refresh-Token", token.getRefreshToken())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken())
                .body(newUser.toResponse());
    }

    @PostMapping("/login")
    @Operation(summary = "로그인")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid UserRequest userRequest, @RequestHeader("FCM-TOKEN") String fcmToken) {
        userService.verifyAndUpdateFcmToken(userRequest, fcmToken);

        TokenResponse token = authService.login(userRequest);

        User user = userService.findByEmail(userRequest.email());

        Profile profile = profileService.findByUser(user);

        LoginResponse loginResponse = LoginResponse.builder()
                .profileId(profile.getId())
                .build();

        return ResponseEntity.ok()
                .header("X-Refresh-Token", token.getRefreshToken())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token.getAccessToken())
                .body(loginResponse);
    }

    @PostMapping("/validate")
    @Operation(summary = "AT 만료 여부 확인")
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String requestAccessToken) {
        if (authService.validate(requestAccessToken)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급")
    public ResponseEntity<?> reissue(@RequestHeader("X-Refresh-Token") String requestRefreshToken,
                                     @RequestHeader("Authorization") String requestAccessToken) {
        TokenResponse reissuedTokenDto = authService.reissue(requestAccessToken, requestRefreshToken);

        if (reissuedTokenDto == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .header("X-Refresh-Token", reissuedTokenDto.getRefreshToken())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + reissuedTokenDto.getAccessToken())
                .build();
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String requestAccessToken, @RequestHeader("FCM-TOKEN") String fcmToken, @CurrentUser User user){
        userService.removeFcmToken(user, fcmToken);

        authService.logout(requestAccessToken);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping("/admin")
    @Operation(summary = "관리자 권한 부여")
    public ResponseEntity<Void> admin(@CurrentUser User user) {
        userService.grantAdminRole(user);

        return ResponseEntity
                .ok()
                .build();
    }
}
