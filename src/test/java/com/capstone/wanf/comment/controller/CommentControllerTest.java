package com.capstone.wanf.comment.controller;

import com.capstone.wanf.ControllerTest;
import com.capstone.wanf.post.domain.entity.Category;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static com.capstone.wanf.fixture.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CommentControllerTest extends ControllerTest {
    @Test
    void 댓글을_생성한다(){
        //given
        final String accessToken = getAccessToken();

        final String adminAccessToken = getAdminAccessToken();

        강의_등록(adminAccessToken, 강의1);

        ExtractableResponse<Response> 게시글_생성1 = 게시글_생성(accessToken, Category.friend, 게시글_요청1);
        //when
        ExtractableResponse<Response> 댓글_생성 = 댓글_생성(accessToken, 게시글_생성1.jsonPath().getLong("id"), 댓글_요청1);
        //then
        assertAll(
                () -> assertThat(댓글_생성.statusCode()).isEqualTo(200),
                () -> assertThat(댓글_생성.jsonPath().getLong("id")).isNotNull()
        );
    }

    @Test
    void 댓글을_수정한다(){
        //given
        final String accessToken = getAccessToken();

        final String adminAccessToken = getAdminAccessToken();

        강의_등록(adminAccessToken, 강의1);

        ExtractableResponse<Response> 게시글_생성2 = 게시글_생성(accessToken, Category.friend, 게시글_요청1);

        ExtractableResponse<Response> 댓글_생성 = 댓글_생성(accessToken, 게시글_생성2.jsonPath().getLong("id"), 댓글_요청2);
        //when
        ExtractableResponse<Response> 댓글_수정 = 댓글_수정(accessToken, 게시글_생성2.jsonPath().getLong("id"), 댓글_생성.jsonPath().getLong("id"), 댓글_요청2);
        //then
        assertAll(
                () -> assertThat(댓글_수정.statusCode()).isEqualTo(200),
                () -> assertThat(댓글_수정.jsonPath().getString("content")).isEqualTo(댓글_요청2.content())
        );
    }

    @Test
    void 댓글을_삭제한다(){
        //given
        final String accessToken = getAccessToken();

        final String adminAccessToken = getAdminAccessToken();

        강의_등록(adminAccessToken, 강의1);

        ExtractableResponse<Response> 게시글_생성3 = 게시글_생성(accessToken, Category.friend, 게시글_요청2);

        ExtractableResponse<Response> 댓글_생성 = 댓글_생성(accessToken, 게시글_생성3.jsonPath().getLong("id"), 댓글_요청3);
        //when
        ExtractableResponse<Response> 댓글_삭제 = 댓글_삭제(accessToken, 게시글_생성3.jsonPath().getLong("id"), 댓글_생성.jsonPath().getLong("id"));
        //then
        assertThat(댓글_삭제.statusCode()).isEqualTo(200);
    }
}