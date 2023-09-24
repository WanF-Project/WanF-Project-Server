package com.capstone.wanf.profile.controller;

import com.capstone.wanf.common.annotation.CurrentUser;
import com.capstone.wanf.common.annotation.CustomPageableAsQueryParam;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.dto.request.ProfileImageRequest;
import com.capstone.wanf.profile.dto.response.MBTIResponse;
import com.capstone.wanf.profile.dto.response.ProfileResponse;
import com.capstone.wanf.profile.service.ProfileService;
import com.capstone.wanf.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "프로필", description = "프로필 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/profiles")
    @Operation(summary = "프로필 생성")
    public ResponseEntity<ProfileResponse> create(@Valid @RequestBody ProfileImageRequest profileImageRequest, @CurrentUser User user) {
        Profile profile = profileService.save(profileImageRequest, user);

        return ResponseEntity.ok(profile.toResponse());
    }

    @GetMapping("/profiles/{id}")
    @Operation(summary = "특정 프로필 조회")
    public ResponseEntity<ProfileResponse> findById(@PathVariable(name = "id") Long id) {
        Profile profile = profileService.findById(id);

        return ResponseEntity.ok(profile.toResponse());
    }

    @GetMapping("/profiles/random")
    @Operation(summary = "랜덤 프로필 조회")
    @CustomPageableAsQueryParam
    public ResponseEntity<Slice<ProfileResponse>> findByRandom(Pageable pageable, @CurrentUser User user) {
        Slice<ProfileResponse> randomProfiles = profileService.findByRandom(user, pageable);

        return ResponseEntity.ok(randomProfiles);
    }

    @PatchMapping("/profiles")
    @Operation(summary = "프로필 수정")
    public ResponseEntity<ProfileResponse> updateField(@Valid @RequestBody ProfileImageRequest profileImageRequest, @CurrentUser User user) {
        Profile profile = profileService.update(user, profileImageRequest);

        return ResponseEntity.ok(profile.toResponse());
    }

    @GetMapping("/profiles/personalities")
    @Operation(summary = "성격 리스트 조회")
    public ResponseEntity<Map<String, String>> getPersonalities() {
        Map<String, String> personalities = profileService.getPersonalities();

        return ResponseEntity.ok(personalities);
    }

    @GetMapping("/profiles/goals")
    @Operation(summary = "목표 리스트 조회")
    public ResponseEntity<Map<String, String>> getGoals() {
        Map<String, String> goals = profileService.getGoals();

        return ResponseEntity.ok(goals);
    }

    @GetMapping("/profiles")
    @Operation(summary = "나의 프로필 조회")
    public ResponseEntity<ProfileResponse> findByUser(@CurrentUser User user) {
        Profile profile = profileService.findByUser(user);

        return ResponseEntity.ok(profile.toResponse());
    }

    @GetMapping("/profiles/mbti")
    @Operation(summary = "MBIT 리스트 조회")
    public ResponseEntity<List<MBTIResponse>> getMBTI() {
        List<MBTIResponse> mbti = profileService.getMBTI();

        return ResponseEntity.ok(mbti);
    }
}
