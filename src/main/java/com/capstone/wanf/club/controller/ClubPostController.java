package com.capstone.wanf.club.controller;

import com.capstone.wanf.auth.jwt.domain.UserDetailsImpl;
import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.domain.entity.ClubPost;
import com.capstone.wanf.club.dto.request.ClubPostRequest;
import com.capstone.wanf.club.service.ClubAuthService;
import com.capstone.wanf.club.service.ClubPostService;
import com.capstone.wanf.club.service.ClubService;
import com.capstone.wanf.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/clubs")
@RequiredArgsConstructor
@RestController
public class ClubPostController {
    private final ClubAuthService clubAuthService;

    private final ClubPostService clubPostService;

    private final ClubService clubService;

    @GetMapping("/{clubId}/clubposts")
    @Operation(
            summary = "모임 게시물 조회",
            description = "내가 참여하고 있는 모임의 게시물을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "403", ref = "403"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<List<ClubPost>> findAll(@PathVariable(name = "clubId") Long clubId,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        clubAuthService.getAuthority(userDetails.getUser().getId(), clubId);

        List<ClubPost> clubPosts = clubPostService.findAllByClubId(clubId);

        return ResponseEntity.ok(clubPosts);
    }

    @PostMapping("/{clubId}/clubposts")
    @Operation(
            summary = "모임 게시물 생성",
            description = "모임에 게시물을 작성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "403", ref = "403"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<ClubPost> save(@PathVariable(name = "clubId") Long clubId,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails,
                                         ClubPostRequest clubPostRequest
    ) {
        User user = userDetails.getUser();

        clubAuthService.getAuthority(user.getId(), clubId);

        Club club = clubService.findById(clubId);

        ClubPost post = clubPostService.save(user, club, clubPostRequest);

        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/clubposts/{clubPostId}")
    @Operation(
            summary = "모임 게시글 삭제",
            description = "모임 게시글을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "403", ref = "403"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<Void> delete(@PathVariable(name = "clubPostId") Long clubPostId,
                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User loginUser = userDetails.getUser();

        clubPostService.delete(loginUser, clubPostId);

        return ResponseEntity.ok().build();
    }
}