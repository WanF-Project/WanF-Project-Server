package com.capstone.wanf.comment.controller;

import com.capstone.wanf.comment.domain.entity.Comment;
import com.capstone.wanf.comment.dto.request.CommentRequest;
import com.capstone.wanf.comment.dto.response.CommentResponse;
import com.capstone.wanf.comment.service.CommentService;
import com.capstone.wanf.common.annotation.CurrentUser;
import com.capstone.wanf.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("posts/{id}/comments")
    @Operation(
            summary = "댓글 작성",
            description = "해당 게시글에 댓글을 작성합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공")
            }
    )
    public ResponseEntity<CommentResponse> save(@PathVariable(name = "id") Long postId, @RequestBody CommentRequest commentRequest, @CurrentUser User user) {
        Comment comment = commentService.save(postId, commentRequest, user);

        return ResponseEntity.ok(comment.toDTO());
    }

    @PatchMapping("posts/{postId}/comments/{commentId}")
    @Operation(
            summary = "댓글 수정",
            description = "해당 댓글을 수정합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공"),
                    @ApiResponse(responseCode = "404", ref = "404")
            }
    )
    public ResponseEntity<CommentResponse> update(@PathVariable(name = "postId") Long postId,
                                                  @PathVariable(name = "commentId") Long commentId,
                                                  @RequestBody CommentRequest commentRequest) {
        Comment comment = commentService.update(postId, commentId, commentRequest);

        return ResponseEntity.ok(comment.toDTO());
    }

    @DeleteMapping("posts/{postId}/comments/{commentId}")
    @Operation(
            summary = "댓글 삭제",
            description = "해당 댓글을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "요청 성공")
            }
    )
    public ResponseEntity<Void> delete(@PathVariable(name = "postId") Long postId,
                                       @PathVariable(name = "commentId") Long commentId) {
        commentService.delete(postId, commentId);

        return ResponseEntity.ok().build();
    }
}
