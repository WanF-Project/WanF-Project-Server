package com.capstone.wanf.club.controller;

import com.capstone.wanf.club.domain.entity.ClubPost;
import com.capstone.wanf.club.dto.request.ClubPostRequest;
import com.capstone.wanf.club.dto.response.ClubPostResponse;
import com.capstone.wanf.club.service.ClubPostService;
import com.capstone.wanf.common.annotation.CurrentUser;
import com.capstone.wanf.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "모임 게시물", description = "모임 게시물 API")
@RestController
@RequestMapping("/api/v1/clubs/{clubId}")
@RequiredArgsConstructor
public class ClubPostController {
    private final ClubPostService clubPostService;

    @GetMapping("/clubposts")
    @Operation(summary = "모임 게시물 모두 조회")
    public ResponseEntity<List<ClubPostResponse>> findAll(@PathVariable(name = "clubId") Long clubId,
                                                          @CurrentUser User user) {
        List<ClubPostResponse> clubPostResponses = clubPostService.findAll(clubId, user);

        return ResponseEntity.ok(clubPostResponses);
    }

    @PostMapping("/clubposts")
    @Operation(summary = "모임 게시물 생성")
    public ResponseEntity<ClubPostResponse> save(@PathVariable(name = "clubId") Long clubId,
                                                 @CurrentUser User user,
                                                 @Valid @RequestBody ClubPostRequest clubPostRequest) {
        ClubPost clubPost = clubPostService.save(user, clubId, clubPostRequest);

        return ResponseEntity.ok(clubPost.toResponse());
    }

    @GetMapping("/clubposts/{clubPostId}")
    @Operation(summary = "ID에 해당하는 모임 게시글 조회")
    public ResponseEntity<ClubPostResponse> findById(@PathVariable(name = "clubId") Long clubId,
                                                     @PathVariable(name = "clubPostId") Long clubPostId) {
        ClubPost clubPost = clubPostService.findById(clubId, clubPostId);

        return ResponseEntity.ok(clubPost.toResponse());
    }

    @DeleteMapping("/clubposts/{clubPostId}")
    @Operation(summary = "모임 게시글 삭제")
    public ResponseEntity<Void> delete(@PathVariable(name = "clubId") Long clubId,
                                       @PathVariable(name = "clubPostId") Long clubPostId,
                                       @CurrentUser User user) {
        clubPostService.delete(user, clubId, clubPostId);


        return ResponseEntity.noContent().build();
    }
}