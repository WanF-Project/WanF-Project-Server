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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "모임 게시물", description = "모임 게시물 API")
@RestController
@RequestMapping("/api/v1/clubs/{clubId}")
@RequiredArgsConstructor
public class ClubPostController {
    private final ClubAuthService clubAuthService;

    private final ClubPostService clubPostService;

    private final ClubService clubService;

    @GetMapping("/clubposts")
    @Operation(summary = "모임 게시물 조회")
    public ResponseEntity<List<ClubPostResponse>> findAll(@PathVariable(name = "clubId") Long clubId,
                                                          @CurrentUser User user
    ) {
        clubAuthService.getAuthority(user.getId(), clubId);

        List<ClubPostResponse> clubPosts = clubPostService.findAllByClubId(clubId).stream()
                .map(ClubPost::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(clubPosts);
    }

    @PostMapping("/clubposts")
    @Operation(summary = "모임 게시물 생성")
    public ResponseEntity<ClubPostResponse> save(@PathVariable(name = "clubId") Long clubId,
                                                 @CurrentUser User user,
                                                 @Valid @RequestBody ClubPostRequest clubPostRequest) {
        clubAuthService.getAuthority(user.getId(), clubId);

        Club club = clubService.findById(clubId);

        ClubPost post = clubPostService.save(user, club, clubPostRequest);

        return ResponseEntity.ok(post.toResponse());
    }

    @GetMapping("/clubposts/{clubPostId}")
    @Operation(summary = "모임 게시글 조회")
    public ResponseEntity<ClubPostResponse> findById(@PathVariable(name = "clubId") Long clubId,
                                                     @PathVariable(name = "clubPostId") Long clubPostId) {
        Club club = clubService.findById(clubId);

        ClubPost clubPost = clubPostService.findById(club, clubPostId);

        return ResponseEntity.ok(clubPost.toResponse());
    }

    @DeleteMapping("/clubposts/{clubPostId}")
    @Operation(summary = "모임 게시글 삭제")
    public ResponseEntity<Void> delete(@PathVariable(name = "clubId") Long clubId,
                                       @PathVariable(name = "clubPostId") Long clubPostId,
                                       @CurrentUser User user) {
        Club club = clubService.findById(clubId);

        clubPostService.delete(user, club, clubPostId);

        return ResponseEntity.noContent().build();
    }
}