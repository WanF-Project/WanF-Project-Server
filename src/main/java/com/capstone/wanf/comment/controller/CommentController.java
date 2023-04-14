package com.capstone.wanf.comment.controller;

import com.capstone.wanf.comment.domain.entity.Comment;
import com.capstone.wanf.comment.dto.request.RequestComment;
import com.capstone.wanf.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("posts/{id}/comments")
    public ResponseEntity<Comment> save(@PathVariable(name = "id") Long postId, @RequestBody RequestComment requestComment) {
        Comment comment = commentService.save(postId, requestComment);

        return ResponseEntity.ok(comment);
    }

    @PatchMapping("/comments/{id}")
    public ResponseEntity<Comment> update(@PathVariable(name = "id") Long id, @RequestBody RequestComment requestComment) {
        Comment comment = commentService.update(id, requestComment);

        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        commentService.delete(id);

        return ResponseEntity.ok().build();
    }
}
