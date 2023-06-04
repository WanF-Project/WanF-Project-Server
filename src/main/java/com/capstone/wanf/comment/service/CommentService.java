package com.capstone.wanf.comment.service;

import com.capstone.wanf.comment.domain.entity.Comment;
import com.capstone.wanf.comment.domain.repo.CommentRepository;
import com.capstone.wanf.comment.dto.request.CommentRequest;
import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.post.domain.entity.Post;
import com.capstone.wanf.post.service.PostService;
import com.capstone.wanf.profile.domain.entity.Profile;
import com.capstone.wanf.profile.service.ProfileService;
import com.capstone.wanf.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.capstone.wanf.error.errorcode.CustomErrorCode.COMMENT_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    private final PostService postService;

    private final ProfileService profileService;

    @Transactional
    public Comment save(Long postId, CommentRequest commentRequest, User user) {
        Post post = postService.findById(postId);

        Profile profile = profileService.findByUser(user);

        Comment comment = Comment.builder()
                .content(commentRequest.content())
                .post(post)
                .profile(profile)
                .build();

        Comment saveComment = commentRepository.save(comment);

        post.getComments().add(saveComment);

        return saveComment;
    }

    public Comment update(Long id, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RestApiException(COMMENT_NOT_FOUND));

        comment.update(commentRequest.content());

        Comment updateComment = commentRepository.save(comment);

        return updateComment;
    }

    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RestApiException(COMMENT_NOT_FOUND));

        commentRepository.delete(comment);
    }
}
