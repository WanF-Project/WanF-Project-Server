package com.capstone.wanf.post.controller;

import com.capstone.wanf.ControllerTest;
import com.capstone.wanf.post.domain.entity.Category;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static com.capstone.wanf.fixture.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PostControllerTest extends ControllerTest {
    @Test
    void 게시글을_생성한다(){
        //given
        final String accessToken = getAccessToken();

        final String adminAccessToken = getAdminAccessToken();

        강의_등록(adminAccessToken, 강의1);
        //when
        ExtractableResponse<Response> 게시글_생성 = 게시글_생성(accessToken, Category.friend, 게시글_요청1);
        //then
        assertAll(
                () -> assertThat(게시글_생성.statusCode()).isEqualTo(200),
                () -> assertThat(게시글_생성.jsonPath().getLong("id")).isNotNull()
        );
    }

    @Test
    void 게시글을_수정한다(){
        //given
        final String accessToken = getAccessToken();

        final String adminAccessToken = getAdminAccessToken();

        강의_등록(adminAccessToken, 강의1);

        ExtractableResponse<Response> 게시글_생성 = 게시글_생성(accessToken, Category.friend, 게시글_요청1);
        //when
        ExtractableResponse<Response> 게시글_수정 = 게시글_수정(accessToken, 게시글_생성.jsonPath().getLong("id"), 게시글_요청2);
        //then
        assertAll(
                () -> assertThat(게시글_수정.statusCode()).isEqualTo(200),
                () -> assertThat(게시글_수정.jsonPath().getString("title")).isEqualTo(게시글_요청2.title()),
                () -> assertThat(게시글_수정.jsonPath().getString("content")).isEqualTo(게시글_요청2.content())
        );
    }

    @Test
    void 게시글을_삭제한다(){
        //given
        final String accessToken = getAccessToken();

        final String adminAccessToken = getAdminAccessToken();

        강의_등록(adminAccessToken, 강의1);

        ExtractableResponse<Response> 게시글_생성 = 게시글_생성(accessToken, Category.friend, 게시글_요청1);
        //when
        ExtractableResponse<Response> 게시글_삭제 = 게시글_삭제(accessToken, 게시글_생성.jsonPath().getLong("id"));
        //then
        assertThat(게시글_삭제.statusCode()).isEqualTo(200);
    }

    @Test
    void 게시글_조회시_게시글이_존재하지_않으면_404를_반환한다(){
        //given
        final String accessToken = getAccessToken();
        //when
        ExtractableResponse<Response> 게시글_조회 = 게시글_조회(accessToken, 1L);
        //then
        assertThat(게시글_조회.statusCode()).isEqualTo(404);
    }

    @Test
    void 게시글을_조회한다(){
        //given
        final String accessToken = getAccessToken();

        final String adminAccessToken = getAdminAccessToken();

        강의_등록(adminAccessToken, 강의1);

        ExtractableResponse<Response> 게시글_생성 = 게시글_생성(accessToken, Category.friend, 게시글_요청1);
        //when
        ExtractableResponse<Response> 게시글_조회 = 게시글_조회(accessToken, 게시글_생성.jsonPath().getLong("id"));
        //then
        assertAll(
                () -> assertThat(게시글_조회.statusCode()).isEqualTo(200),
                () -> assertThat(게시글_조회.jsonPath().getString("title")).isEqualTo(게시글_요청1.title()),
                () -> assertThat(게시글_조회.jsonPath().getString("content")).isEqualTo(게시글_요청1.content())
        );
    }

    @Test
    void 게시글_목록을_조회한다(){
        //given
        final String accessToken = getAccessToken();

        final String adminAccessToken = getAdminAccessToken();

        강의_등록(adminAccessToken, 강의1);

        게시글_생성(accessToken, Category.friend, 게시글_요청1);

        게시글_생성(accessToken, Category.friend, 게시글_요청2);
        //when
        ExtractableResponse<Response> 페이징_없는_게시글_모두_조회 = 페이징_없는_게시글_모두_조회(accessToken,Category.friend);
        //then
        assertAll(
                () -> assertThat(페이징_없는_게시글_모두_조회.statusCode()).isEqualTo(200),
                () -> assertThat(페이징_없는_게시글_모두_조회.jsonPath().getList("content").size()).isEqualTo(2)
        );
    }

    @Test
    void 페이징을_적용한_게시글_목록을_조회한다(){
        //given
        final String accessToken = getAccessToken();

        final String adminAccessToken = getAdminAccessToken();

        강의_등록(adminAccessToken, 강의1);

        게시글_생성(accessToken, Category.friend, 게시글_요청1);

        게시글_생성(accessToken, Category.friend, 게시글_요청2);

        Pageable pageable = PageRequest.of(0, 5);
        //when
        ExtractableResponse<Response> 페이징_적용한_게시글_모두_조회 = 페이징_적용한_게시글_모두_조회(accessToken, Category.friend, pageable);
        //then
        assertAll(
                () -> assertThat(페이징_적용한_게시글_모두_조회.statusCode()).isEqualTo(200),
                () -> assertThat(페이징_적용한_게시글_모두_조회.jsonPath().getList("content").size()).isEqualTo(2)
        );
    }

    @Test
    void 검색어를_통해_게시물을_조회한다(){
        //given
        final String accessToken = getAccessToken();

        final String adminAccessToken = getAdminAccessToken();

        강의_등록(adminAccessToken, 강의1);

        게시글_생성(accessToken, Category.friend, 게시글_요청1);

        게시글_생성(accessToken, Category.friend, 게시글_요청2);
        //when
        ExtractableResponse<Response> 검색어를_통해_게시물을_조회 = 검색어를_통해_게시물을_조회(accessToken, Category.friend, "강의1", PageRequest.of(0, 5));
        //then
        assertAll(
                () -> assertThat(검색어를_통해_게시물을_조회.statusCode()).isEqualTo(200),
                () -> assertThat(검색어를_통해_게시물을_조회.jsonPath().getList("content").size()).isEqualTo(2)
        );
    }
}