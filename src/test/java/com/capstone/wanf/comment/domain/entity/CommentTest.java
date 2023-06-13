package com.capstone.wanf.comment.domain.entity;

import com.capstone.wanf.comment.dto.response.CommentResponse;
import com.capstone.wanf.fixture.DomainFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CommentTest {
    private static final String CONTENT = "댓글 내용";

    @Test
    void 댓글을_생성한다(){
        //when & then
        assertThatCode(() -> Comment.builder()
                .content(CONTENT)
                .build())
                .doesNotThrowAnyException();
    }

    @Test
    void 댓글을_수정한다(){
        //given
        Comment comment = Comment.builder()
                .content(CONTENT)
                .build();
        //when
        comment.update("댓글 내용 수정");
        //then
        assertThat(comment.getContent()).isEqualTo("댓글 내용 수정");
    }

    @Test
    void 댓글을_DTO에_담는다(){
        //given
        Comment comment = Comment.builder()
                .content(CONTENT)
                .profile(DomainFixture.프로필1)
                .build();
        //when
        CommentResponse commentResponse = comment.toDTO();
        //then
        assertThat(commentResponse.content()).isEqualTo(CONTENT);
    }

}