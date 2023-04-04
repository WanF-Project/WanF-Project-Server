package com.capstone.wanf.profile.controller;

import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.dto.request.ProfileRequest;
import com.capstone.wanf.profile.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/profiles/{id}")
    public ResponseEntity<Profile> findById(@PathVariable(name = "id") Long id) {
        Profile profile = profileService.findById(id);

        return ResponseEntity.ok(profile);
    }

    @PostMapping("/profiles")
    public ResponseEntity<Profile> save(@Valid @RequestBody ProfileRequest profileRequest) {
        Profile profile = profileService.save(profileRequest);

        return ResponseEntity.ok(profile);
    }

    @PatchMapping("/profiles/{id}")
    public ResponseEntity<Profile> updateField(@PathVariable(name = "id") Long id, @Valid @RequestBody ProfileRequest profileRequest) {
        Profile profile = profileService.update(id, profileRequest);

        return ResponseEntity.ok(profile);
    }
}
