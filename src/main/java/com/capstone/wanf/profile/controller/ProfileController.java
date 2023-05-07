package com.capstone.wanf.profile.controller;

import com.capstone.wanf.auth.jwt.domain.UserDetailsImpl;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.dto.request.ProfileRequest;
import com.capstone.wanf.profile.service.ProfileService;
import com.capstone.wanf.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/profiles/{id}")
    @Operation(
            summary = "특정 프로필 조회",
            description = "해당 ID의 프로필을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<Profile> findById(@PathVariable(name = "id") Long id) {
        Profile profile = profileService.findById(id);

        return ResponseEntity.ok(profile);
    }

    @PostMapping("/profiles")
    @Operation(
            summary = "프로필 생성",
            description = "프로필을 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공")
            }
    )
    public ResponseEntity<Profile> save(@Valid @RequestBody ProfileRequest profileRequest, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        Profile profile = profileService.save(profileRequest, user);

        return ResponseEntity.ok(profile);
    }

    @PatchMapping("/profiles/{id}")
    @Operation(
            summary = "프로필 수정",
            description = "프로필을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<Profile> updateField(@PathVariable(name = "id") Long id, @Valid @RequestBody ProfileRequest profileRequest) {
        Profile profile = profileService.update(id, profileRequest);

        return ResponseEntity.ok(profile);
    }

    @GetMapping("/profiles")
    @Operation(
            summary = "성격 또는 목표 리스트 조회",
            description = "프로필에 입력할 목표, 성격을 Key : value 형태로 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<Map<String, String>> getPersonalities(@RequestParam String category) {
        if (category.equals("personality")) {
            Map<String, String> personalities = profileService.getPersonalities();

            return ResponseEntity.ok(personalities);
        } else if (category.equals("goal")) {
            Map<String, String> goals = profileService.getGoals();

            return ResponseEntity.ok(goals);
        }

        return ResponseEntity.ok(null);
    }
}
