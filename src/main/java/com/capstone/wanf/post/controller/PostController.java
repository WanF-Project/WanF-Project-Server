package com.capstone.wanf.post.controller;

import com.capstone.wanf.post.domain.entity.Category;
import com.capstone.wanf.post.domain.entity.Post;
import com.capstone.wanf.post.dto.request.RequestPost;
import com.capstone.wanf.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<Slice<Post>> findAll(@RequestParam("category") Category category, @PageableDefault(size = 7 ) Pageable Pageable) {
        Slice<Post> posts = postService.findAll(category, Pageable);

        return ResponseEntity.ok(posts);

    }

    @PostMapping("/posts")
    public ResponseEntity<Post> save(@RequestParam("category") Category category , @RequestBody RequestPost requestPost) {
        Post post = postService.save(category, requestPost);

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
