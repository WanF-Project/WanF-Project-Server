package com.capstone.wanf.comment.domain.repo;

import com.capstone.wanf.comment.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
