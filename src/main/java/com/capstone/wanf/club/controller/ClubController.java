package com.capstone.wanf.club.controller;

import com.capstone.wanf.auth.jwt.domain.UserDetailsImpl;
import com.capstone.wanf.club.domain.entity.Authority;
import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.domain.entity.ClubAuth;
import com.capstone.wanf.club.dto.request.ClubPwdRequest;
import com.capstone.wanf.club.dto.request.ClubRequest;
import com.capstone.wanf.club.dto.response.ClubResponse;
import com.capstone.wanf.club.service.ClubAuthService;
import com.capstone.wanf.club.service.ClubService;
import com.capstone.wanf.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
@RestController
public class ClubController {
    private final ClubService clubService;

    private final ClubAuthService clubAuthService;

    // 기획 변경으로 모든 모임 -> 로그인 유저가 참여하는 모임 조회로 변경
    @GetMapping("/clubs")
    @Operation(
            summary = "모든 모임 조회",
            description = "내가 참여하고 있는 모든 모임을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
            }
    )
    public ResponseEntity<List<ClubResponse>> findAll(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getId();

        List<ClubAuth> clubAuthList = clubAuthService.findByUserId(userId);

        List<ClubResponse> clubList = new ArrayList<>();

        for (ClubAuth clubAuth : clubAuthList) {
            Club club = clubAuth.getClub();

            clubList.add(club.toDTO());
        }

        return ResponseEntity.ok(clubList);
    }

    @PostMapping("/clubs")
    @Operation(
            summary = "모임 생성",
            description = "모임을 생성하고, 모임장의 권한을 부여합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공")
            }
    )
    public ResponseEntity<Club> save(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ClubRequest clubRequest) {
        User user = userDetails.getUser();

        Club club = clubService.save(clubRequest);

        clubAuthService.grantAuthorityToClub(user, club, Authority.CLUB_LEADER);

        return ResponseEntity.ok(club);
    }

    @PostMapping("/clubs/{id}")
    @Operation(
            summary = "모임 가입",
            description = "모임을 생성하고, 모임장의 권한을 부여합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "401", ref = "401"),
                    @ApiResponse(responseCode = "403", ref = "403")
            }
    )
    public ResponseEntity<Void> checkClubAccess(@PathVariable(name = "id") Long id,
                                                @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @RequestBody ClubPwdRequest clubPwdRequest) {
        User user = userDetails.getUser();

        Club club = clubService.findById(id);

        Authority userAuth = clubAuthService.findByUserIdAndClubId(user.getId(), club.getId());

        if (clubService.checkClubAccess(id, clubPwdRequest, userAuth)) {
            clubAuthService.grantAuthorityToClub(user, club, Authority.CLUB_MEMBER);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/clubs/{id}")
    @Operation(
            summary = "모임 접근 권한 확인",
            description = "로그인 유저가 해당 모임의 접근 권한을 갖고 있는지 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "유저의 권한을 응답받습니다."),
                    @ApiResponse(responseCode = "403", ref = "403")
            }
    )
    public ResponseEntity<Authority> getAuthority(@PathVariable(name = "id") Long id,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        Club club = clubService.findById(id);

        Authority userAuth = clubAuthService.getAuthority(user.getId(), club.getId());

        return ResponseEntity.ok(userAuth);
    }
}