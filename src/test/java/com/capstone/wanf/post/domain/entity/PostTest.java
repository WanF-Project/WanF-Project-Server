package com.capstone.wanf.post.domain.entity;

import com.capstone.wanf.post.dto.request.PostRequest;
import com.capstone.wanf.post.dto.response.PostResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.capstone.wanf.fixture.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

class PostTest {
    private static final String TITLE = "title";

    private static final String CONTENT = "content";

    private static final Category CATEGORY = Category.friend;

    @Test
    void 게시글을_생성할_수_있다(){
        //when & then
        assertThatCode(() -> Post.builder()
                .title(TITLE)
                .content(CONTENT)
                .category(CATEGORY)
                .build())
                .doesNotThrowAnyException();
    }

    @Nested
    class 게시글을_수정한다{
        @Test
        void 제목과_내용만을_수정한다(){
            //given
            Post post = Post.builder()
                    .title(TITLE)
                    .content(CONTENT)
                    .category(CATEGORY)
                    .build();

            PostRequest postRequest = PostRequest.builder()
                    .title("new title")
                    .content("new content")
                    .build();
            //when
            post.update(postRequest);
            //then
            assertAll(
                    () -> assertEquals(post.getTitle(), postRequest.title()),
                    () -> assertEquals(post.getContent(), postRequest.content())
            );
        }

        @Test
        void 수업을_수정한다() {
            //given
            Post post = Post.builder()
                    .title(TITLE)
                    .content(CONTENT)
                    .category(CATEGORY)
                    .course(강의1)
                    .build();
            //when
            post.update(강의2);
            //then
            assertThat(post.getCourse()).isEqualTo(강의2);
        }
    }

    @Test
    void 게시글에_저장된_정보를_조회할_수_있다(){
        //given
        Post post = Post.builder()
                .title(TITLE)
                .content(CONTENT)
                .category(CATEGORY)
                .build();
        //when & then
        assertAll(
                () -> assertEquals(post.getTitle(), TITLE),
                () -> assertEquals(post.getContent(), CONTENT),
                () -> assertEquals(post.getCategory(), CATEGORY)
        );
    }

    @Nested
    class 게시글에_저장된_정보를_DTO에_담을_수_있다{
        @Test
        void 댓글이_달리지_않은_게시글을_DTO에_담는다(){
            //given
            Post post = Post.builder()
                    .title(TITLE)
                    .content(CONTENT)
                    .category(CATEGORY)
                    .course(강의1)
                    .profile(프로필1)
                    .build();
            //when
            PostResponse dto = post.toPostResponse();
            //then
            assertAll(
                    () -> assertEquals(dto.title(), TITLE),
                    () -> assertEquals(dto.content(), CONTENT),
                    () -> assertEquals(dto.category(), Map.of(CATEGORY.name(), CATEGORY.getName())),
                    () -> assertEquals(dto.course(), 강의1),
                    () -> assertEquals(dto.profile(), 프로필1.toDTO()),
                    () -> assertEquals(dto.comments(), null)
            );
        }

        @Test
        void 댓글이_달린_게시글을_DTO에_담는다(){
            //given
            Post post = Post.builder()
                    .title(TITLE)
                    .content(CONTENT)
                    .category(CATEGORY)
                    .course(강의1)
                    .profile(프로필1)
                    .comments(List.of(댓글1))
                    .build();
            //when
            PostResponse dto = post.toPostResponse();
            //then
            assertAll(
                    () -> assertEquals(dto.title(), TITLE),
                    () -> assertEquals(dto.content(), CONTENT),
                    () -> assertEquals(dto.category(), Map.of(CATEGORY.name(), CATEGORY.getName())),
                    () -> assertEquals(dto.course(), 강의1),
                    () -> assertEquals(dto.profile(), 프로필1.toDTO()),
                    () -> assertEquals(dto.comments(), List.of(댓글1.toDTO()))
            );
        }
    }
}