package com.capstone.wanf.post.controller;

import com.capstone.wanf.common.annotation.CurrentUser;
import com.capstone.wanf.common.annotation.CustomPageableAsQueryParam;
import com.capstone.wanf.post.domain.entity.Category;
import com.capstone.wanf.post.domain.entity.Post;
import com.capstone.wanf.post.dto.request.PostRequest;
import com.capstone.wanf.post.dto.response.PostPaginationResponse;
import com.capstone.wanf.post.dto.response.PostResponse;
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
    public ResponseEntity<Slice<PostPaginationResponse>> findAll(@RequestParam("category") Category category, @PageableDefault(size = 7) Pageable Pageable) {
        Slice<PostPaginationResponse> posts = postService.findAll(category, Pageable);

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts")
    @Operation(
            summary = "모든 게시글 조회 (페이징 없음)",
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
    public ResponseEntity<List<PostPaginationResponse>> findAll(@RequestParam("category") Category category) {
        List<PostPaginationResponse> posts = postService.findAll(category);

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
    public ResponseEntity<PostResponse> save(@RequestParam("category") Category category
            , @RequestBody PostRequest postRequest
            , @CurrentUser User user) {
        Post post = postService.save(category, postRequest, user);

        return ResponseEntity.ok(post.toPostResponse());
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
    public ResponseEntity<PostResponse> findById(@PathVariable(name = "id") Long id) {
        Post post = postService.findById(id);

        return ResponseEntity.ok(post.toPostResponse());
    }

    @GetMapping("/posts/search")
    @Operation(
            summary = "검색어를 통해 게시글 조회",
            description = "검색어에 해당하는 게시글을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<Slice<PostPaginationResponse>> findAllByQuery(@RequestParam("category") Category category
            , @RequestParam("query") String query
            , @PageableDefault(size = 7) Pageable Pageable) {
        Slice<PostPaginationResponse> posts = postService.searchAllByQuery(category, query, Pageable);

        return ResponseEntity.ok(posts);
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
    public ResponseEntity<PostResponse> update(@PathVariable(name = "id") Long id, @RequestBody PostRequest postRequest) {
        Post post = postService.update(id, postRequest);

        return ResponseEntity.ok(post.toPostResponse());
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
