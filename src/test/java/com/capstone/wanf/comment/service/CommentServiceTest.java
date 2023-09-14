package com.capstone.wanf.comment.service;

import com.capstone.wanf.comment.domain.entity.Comment;
import com.capstone.wanf.error.exception.RestApiException;
import com.capstone.wanf.firebase.service.FCMService;
import com.capstone.wanf.post.service.PostService;
import com.capstone.wanf.profile.service.ProfileService;
import com.capstone.wanf.user.domain.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static com.capstone.wanf.fixture.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @InjectMocks
    private CommentService commentService;

    @Mock
    private PostService postService;

    @Mock
    private ProfileService profileService;

    @Mock
    private FCMService fcmService;

    @Test
    void 댓글을_저장한다(){
        //given
        given(postService.findById(anyLong())).willReturn(게시글1);

        given(profileService.findByUser(any(User.class))).willReturn(프로필1);

        //when
        Comment 댓글 = commentService.save(1L, 댓글_요청1, 유저1);
        //then
        assertThat(댓글.getContent()).isEqualTo(댓글_요청1.content());
    }

    @Test
    void 댓글을_수정한다(){
        //given
        given(postService.findById(anyLong())).willReturn(게시글3);
        //when
        Comment 댓글 = commentService.update(1L, 1L, 댓글_요청1);
        //then
        assertThat(댓글.getContent()).isEqualTo(댓글_요청1.content());
    }

    @Test
    void 댓글을_삭제한다(){
        //given
        given(postService.findById(anyLong())).willReturn(게시글4);
        //when
        commentService.delete(1L,1L);
        //then
        assertThat(게시글4.getComments().size()).isEqualTo(0);
    }

    @Test
    void 댓글_수정시_ID에_해당하는_댓글이_없으면_예외를_던진다(){
        //given
        given(postService.findById(anyLong())).willReturn(게시글3);
        //when & then
        assertThatThrownBy(() -> commentService.update(1L, 2L,댓글_요청1))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    void 댓글_삭제시_ID에_해당하는_댓글이_없으면_삭제_로직은_실행되지_않는다(){
        //given
        given(postService.findById(anyLong())).willReturn(게시글4);
        //when
        commentService.delete(1L,2L);
        //then
        assertThat(게시글4.getComments().size()).isEqualTo(1);
    }
}