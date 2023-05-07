package com.capstone.wanf.post.controller;

import com.capstone.wanf.auth.jwt.domain.UserDetailsImpl;
import com.capstone.wanf.common.annotation.CustomPageableAsQueryParam;
import com.capstone.wanf.post.domain.entity.Category;
import com.capstone.wanf.post.domain.entity.Post;
import com.capstone.wanf.post.dto.request.RequestPost;
import com.capstone.wanf.post.service.PostService;
import com.capstone.wanf.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/posts-pageable")
    @Operation(
            summary = "모든 게시글 조회",
            description = "모든 게시글을 조회합니다.",
            parameters = {
                    @Parameter(name = "category", description = "카테고리",
                            schema = @Schema(type = "string", allowableValues = {"friend", "course"}))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    @CustomPageableAsQueryParam
    public ResponseEntity<Slice<Post>> findAll(@RequestParam("category") Category category, @PageableDefault(size = 7) Pageable Pageable) {
        Slice<Post> posts = postService.findAll(category, Pageable);

        return ResponseEntity.ok(posts);

    }

    @GetMapping("/posts")
    @Operation(
            summary = "모든 게시글 조회",
            description = "모든 게시글을 조회합니다.",
            parameters = {
                    @Parameter(name = "category", description = "카테고리",
                            schema = @Schema(type = "string", allowableValues = {"friend", "course"}))
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<List<Post>> findAll(@RequestParam("category") Category category) {
        List<Post> posts = postService.findAll(category);

        return ResponseEntity.ok(posts);

    }

    @PostMapping("/posts")
    @Operation(
            summary = "게시글 생성",
            description = "게시글을 생성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공")
            }
    )
    public ResponseEntity<Post> save(@RequestParam("category") Category category, @RequestBody RequestPost requestPost, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();

        Post post = postService.save(category, requestPost, user);

        return ResponseEntity.ok(post);
    }

    @GetMapping("/posts/{id}")
    @Operation(
            summary = "특정 게시글 조회",
            description = "해당 ID의 게시글을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<Post> findById(@PathVariable(name = "id") Long id) {
        Post post = postService.findById(id);

        return ResponseEntity.ok(post);
    }

    @PatchMapping("/posts/{id}")
    @Operation(
            summary = "게시글 수정",
            description = "게시글을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<Post> update(@PathVariable(name = "id") Long id, @RequestBody RequestPost requestPost) {
        Post post = postService.update(id, requestPost);

        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/posts/{id}")
    @Operation(
            summary = "게시글 삭제",
            description = "게시글을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        postService.delete(id);

        return ResponseEntity.ok().build();
    }
}
