package com.capstone.wanf.club.controller;

import com.capstone.wanf.club.domain.entity.Authority;
import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.domain.entity.ClubAuth;
import com.capstone.wanf.club.dto.request.ClubPwdRequest;
import com.capstone.wanf.club.dto.request.ClubRequest;
import com.capstone.wanf.club.dto.response.ClubDetailResponse;
import com.capstone.wanf.club.dto.response.ClubResponse;
import com.capstone.wanf.club.service.ClubAuthService;
import com.capstone.wanf.club.service.ClubService;
import com.capstone.wanf.common.annotation.CurrentUser;
import com.capstone.wanf.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<ClubResponse>> findAll(@CurrentUser User user) {
        List<ClubAuth> clubAuthList = clubAuthService.findByUserId(user.getId());

        return ResponseEntity.ok(clubAuthList.stream()
                .map(clubAuth -> clubAuth.getClub().toDTO())
                .collect(Collectors.toList()));
    }

    @PostMapping("/clubs")
    @Operation(
            summary = "모임 생성",
            description = "모임을 생성하고, 모임장의 권한을 부여합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공")
            }
    )
    public ResponseEntity<ClubDetailResponse> save(@CurrentUser User user, @RequestBody ClubRequest clubRequest) {
        Club club = clubService.save(clubRequest);

        clubAuthService.grantAuthorityToClub(user, club, Authority.CLUB_LEADER);

        return ResponseEntity.ok(club.toDetailDTO());
    }

    @PostMapping("/clubs/join")
    @Operation(
            summary = "모임 가입",
            description = "모임의 비밀번호를 입력하여 접근 권한을 얻습니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "401", ref = "401"),
                    @ApiResponse(responseCode = "403", ref = "403")
            }
    )
    public ResponseEntity<Authority> checkClubAccess(@CurrentUser User user,
                                                     @RequestBody ClubPwdRequest clubPwdRequest) {
        Club club = clubService.findById(clubPwdRequest.clubId());

        Authority userAuth = clubAuthService.findByUserIdAndClubId(user.getId(), club.getId());

        if (clubService.checkClubAccess(club, clubPwdRequest, userAuth)) {
            clubAuthService.grantAuthorityToClub(user, club, Authority.CLUB_MEMBER);
        }

        return ResponseEntity.ok(Authority.CLUB_MEMBER);
    }

    @GetMapping("/clubs/{clubId}")
    @Operation(
            summary = "모임 접근 권한 확인",
            description = "로그인 유저가 해당 모임의 접근 권한을 갖고 있는지 확인합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "유저의 권한을 응답받습니다."),
                    @ApiResponse(responseCode = "403", ref = "403")
            }
    )
    public ResponseEntity<Authority> getAuthority(@PathVariable(name = "clubId") Long clubId,
                                                  @CurrentUser User user) {
        Club club = clubService.findById(clubId);

        Authority userAuth = clubAuthService.getAuthority(user.getId(), club.getId());

        return ResponseEntity.ok(userAuth);
    }

    @GetMapping("/clubs/{clubId}/password")
    @Operation(
            summary = "모임 비밀번호 조회",
            description = "모임의 비밀번호를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<ClubPwdRequest> getClubPassword(@PathVariable(name = "clubId") Long clubId) {
        Club club = clubService.findById(clubId);

        ClubPwdRequest clubPwdRequest = ClubPwdRequest.builder()
                .clubId(clubId)
                .password(club.getPassword())
                .build();

        return ResponseEntity.ok(clubPwdRequest);
    }
}