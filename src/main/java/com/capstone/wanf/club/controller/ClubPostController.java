package com.capstone.wanf.club.controller;

import com.capstone.wanf.club.domain.entity.Club;
import com.capstone.wanf.club.domain.entity.ClubPost;
import com.capstone.wanf.club.dto.request.ClubPostRequest;
import com.capstone.wanf.club.dto.response.ClubPostResponse;
import com.capstone.wanf.club.service.ClubAuthService;
import com.capstone.wanf.club.service.ClubPostService;
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

@RequestMapping("/api/v1/clubs/{clubId}")
@RequiredArgsConstructor
@RestController
public class ClubPostController {
    private final ClubAuthService clubAuthService;

    private final ClubPostService clubPostService;

    private final ClubService clubService;

    @GetMapping("/clubposts")
    @Operation(
            summary = "모임 게시물 조회",
            description = "내가 참여하고 있는 모임의 게시물을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "403", ref = "403"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<List<ClubPostResponse>> findAll(@PathVariable(name = "clubId") Long clubId,
                                                          @CurrentUser User user
    ) {
        clubAuthService.getAuthority(user.getId(), clubId);

        List<ClubPostResponse> clubPosts = clubPostService.findAllByClubId(clubId).stream()
                .map(ClubPost::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(clubPosts);
    }

    @PostMapping("/clubposts")
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
                                         @CurrentUser User user,
                                         ClubPostRequest clubPostRequest
    ) {
        clubAuthService.getAuthority(user.getId(), clubId);

        Club club = clubService.findById(clubId);

        ClubPost post = clubPostService.save(user, club, clubPostRequest);

        return ResponseEntity.ok(post);
    }

    @GetMapping("/clubposts/{clubPostId}")
    @Operation(
            summary = "모임 게시글 조회",
            description = "특정 모임 게시글을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "403", ref = "403"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<ClubPostResponse> findById(@PathVariable(name = "clubId") Long clubId,
                                                     @PathVariable(name = "clubPostId") Long clubPostId) {
        ClubPost clubPost = clubPostService.findById(clubId, clubPostId);

        return ResponseEntity.ok(clubPost.toDTO());
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
    public ResponseEntity<Void> delete(@PathVariable(name = "clubId") Long clubId,
                                       @PathVariable(name = "clubPostId") Long clubPostId,
                                       @CurrentUser User user) {
        clubPostService.delete(user, clubId, clubPostId);

        return ResponseEntity.ok().build();
    }
}