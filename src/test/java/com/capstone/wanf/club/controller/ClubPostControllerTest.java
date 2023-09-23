package com.capstone.wanf.club.controller;

import com.capstone.wanf.ControllerTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static com.capstone.wanf.fixture.DomainFixture.모임_게시글_요청1;
import static com.capstone.wanf.fixture.DomainFixture.모임_요청1;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ClubPostControllerTest extends ControllerTest {
    @Test
    void 모임_게시글을_생성한다() {
        //given
        String accessToken = getAccessToken();

        모임_생성(accessToken, 모임_요청1);
        //when
        ExtractableResponse<Response> 모임_게시글_생성 = 모임_게시글_생성(accessToken, 모임_게시글_요청1, 1L);
        //then
        assertAll(
                () -> assertThat(모임_게시글_생성.statusCode()).isEqualTo(200),
                () -> assertThat(모임_게시글_생성.body().jsonPath().getLong("id")).isNotNull()
        );
    }

    @Test
    void 모임_게시글을_조회한다() {
        //given
        String accessToken = getAccessToken();

        모임_생성(accessToken, 모임_요청1);

        모임_게시글_생성(accessToken, 모임_게시글_요청1, 1L);
        //when
        ExtractableResponse<Response> 모임_게시글_조회 = 모임_게시글_조회(accessToken,1L, 1L);
        //then
        assertAll(
                () -> assertThat(모임_게시글_조회.statusCode()).isEqualTo(200),
                () -> assertThat(모임_게시글_조회.body().jsonPath().getLong("id")).isNotNull()
        );
    }

    @Test
    void 모임_게시글을_모두_조회한다() {
        //given
        String accessToken = getAccessToken();

        모임_생성(accessToken, 모임_요청1);

        모임_게시글_생성(accessToken, 모임_게시글_요청1, 1L);
        //when
        ExtractableResponse<Response> 모임_게시글_모두_조회 = 모임_게시글_모두_조회(accessToken, 1L);
        //then
        assertAll(
                () -> assertThat(모임_게시글_모두_조회.statusCode()).isEqualTo(200),
                () -> assertThat(모임_게시글_모두_조회.body().jsonPath().getList("id").size()).isEqualTo(1)
        );
    }

    @Test
    void 모임_게시글을_삭제한다() {
        //given
        String accessToken = getAccessToken();

        모임_생성(accessToken, 모임_요청1);

        모임_게시글_생성(accessToken, 모임_게시글_요청1, 1L);
        //when
        ExtractableResponse<Response> 모임_게시글_삭제 = 모임_게시글_삭제(accessToken, 1L, 1L);
        //then
        assertThat(모임_게시글_삭제.statusCode()).isEqualTo(204);
    }

}