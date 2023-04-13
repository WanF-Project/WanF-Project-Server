package com.capstone.wanf.post.domain.repo;

import com.capstone.wanf.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
