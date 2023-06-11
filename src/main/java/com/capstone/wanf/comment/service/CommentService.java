package com.capstone.wanf.comment.service;

import com.capstone.wanf.comment.domain.entity.Comment;
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
    private final PostService postService;

    private final ProfileService profileService;

    @Transactional
    public Comment save(Long postId, CommentRequest commentRequest, User user) {
        Post post = postService.findById(postId);

        Profile profile = profileService.findByUser(user);

        Comment comment = Comment.builder()
                .content(commentRequest.content())
                .profile(profile)
                .build();

        post.getComments().add(comment);

        return comment;
    }

    @Transactional
    public Comment update(Long postId, Long commentId, CommentRequest commentRequest) {
        Post post = postService.findById(postId);

        Comment findComment = post.getComments().stream()
                .filter(comment -> comment.getId() == commentId)
                .findFirst()
                .orElseThrow(() -> new RestApiException(COMMENT_NOT_FOUND));

        findComment.update(commentRequest.content());

        return findComment;
    }

    @Transactional
    public void delete(Long postId, Long commentId) {
        postService.findById(postId).getComments()
                .removeIf(comment -> comment.getId() == commentId);
    }
}
