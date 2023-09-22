package com.capstone.wanf.club.controller;

import com.capstone.wanf.club.domain.entity.Authority;
import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.dto.request.ClubPwdRequest;
import com.capstone.wanf.club.dto.request.ClubRequest;
import com.capstone.wanf.club.dto.response.ClubDetailResponse;
import com.capstone.wanf.club.dto.response.ClubResponse;
import com.capstone.wanf.club.service.ClubAuthService;
import com.capstone.wanf.club.service.ClubService;
import com.capstone.wanf.common.annotation.CurrentUser;
import com.capstone.wanf.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "모임", description = "모임 API")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ClubController {
    private final ClubService clubService;

    private final ClubAuthService clubAuthService;

    @GetMapping("/clubs")
    @Operation(summary = "모든 모임 조회")
    public ResponseEntity<List<ClubResponse>> findAll(@CurrentUser User user) {
        List<ClubResponse> clubList = clubAuthService.findByUserId(user.getId());

        return ResponseEntity.ok(clubList);
    }

    @PostMapping("/clubs")
    @Operation(summary = "모임 생성")
    public ResponseEntity<ClubDetailResponse> save(@CurrentUser User user,
                                                   @Valid @RequestBody ClubRequest clubRequest) {
        Club club = clubService.save(clubRequest, user);

        return ResponseEntity.ok(club.toDetailResponse());
    }

    @PostMapping("/clubs/join")
    @Operation(summary = "모임 가입")
    public ResponseEntity<Authority> checkClubAccess(@CurrentUser User user,
                                                     @Valid @RequestBody ClubPwdRequest clubPwdRequest) {
        clubService.joinClub(user, clubPwdRequest);

        return ResponseEntity.ok(Authority.CLUB_MEMBER);
    }

    @GetMapping("/clubs/{clubId}")
    @Operation(summary = "모임 접근 권한 확인")
    public ResponseEntity<Authority> getAuthority(@PathVariable(name = "clubId") Long clubId,
                                                  @CurrentUser User user) {
        Authority authority = clubService.checkAuthority(clubId, user);

        return ResponseEntity.ok(authority);
    }

    @GetMapping("/clubs/{clubId}/password")
    @Operation(summary = "모임 비밀번호 조회")
    public ResponseEntity<ClubPwdRequest> getClubPassword(@PathVariable(name = "clubId") Long clubId) {
        ClubPwdRequest clubPwdRequest = clubService.getPassword(clubId);

        return ResponseEntity.ok(clubPwdRequest);
    }
}