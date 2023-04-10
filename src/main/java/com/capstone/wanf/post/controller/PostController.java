package com.capstone.wanf.post.controller;

import com.capstone.wanf.post.domain.entity.Post;
import com.capstone.wanf.post.dto.request.RequestPost;
import com.capstone.wanf.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<Post> save(@RequestBody RequestPost requestPost) {
        Post post = postService.save(requestPost);

        return ResponseEntity.ok(post);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> findById(@PathVariable(name = "id") Long id) {
        Post post = postService.findById(id);

        return ResponseEntity.ok(post);
    }

    @PatchMapping("/posts/{id}")
    public ResponseEntity<Post> update(@PathVariable(name = "id") Long id, @RequestBody RequestPost requestPost) {
        Post post = postService.update(id, requestPost);

        return ResponseEntity.ok(post);
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        postService.delete(id);

        return ResponseEntity.ok().build();
    }
}
