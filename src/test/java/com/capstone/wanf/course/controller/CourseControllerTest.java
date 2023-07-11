package com.capstone.wanf.course.controller;

import com.capstone.wanf.ControllerTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;

import static com.capstone.wanf.fixture.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CourseControllerTest extends ControllerTest {
    @Test
    void ID에_맞는_수업이_없으면_404를_반환한다() {
        //given
        final String accessToken = getAccessToken();

        //given & when
        ExtractableResponse<Response> 수업_조회 = 수업_조회(accessToken, 1L);

        //then
        assertThat(수업_조회.statusCode()).isEqualTo(404);
    }

    @Test
    void ID에_맞는_수업을_조회한다() {
        //given
        final String accessToken = getAdminAccessToken();

        ExtractableResponse<Response> 수업_등록 = 수업_등록(accessToken, 수업_요청1);

        //when
        ExtractableResponse<Response> 수업_조회 = 수업_조회(accessToken, 수업_등록.jsonPath().getLong("id"));

        //then
        assertAll(
                () -> assertThat(수업_조회.statusCode()).isEqualTo(200),
                () -> assertThat(수업_조회.jsonPath().getLong("id")).isEqualTo(수업_등록.jsonPath().getLong("id"))
        );
    }

    @Test
    void 수업을_삭제한다() {
        //given
        final String accessToken = getAdminAccessToken();

        ExtractableResponse<Response> 수업_등록 = 수업_등록(accessToken, 수업_요청1);

        //when
        ExtractableResponse<Response> 수업_삭제 = 수업_삭제(accessToken, 수업_등록.jsonPath().getLong("id"));

        //then
        assertThat(수업_삭제.statusCode()).isEqualTo(204);
    }

    @Test
    void 수업_리스트를_조회시_수업이_없으면_빈리스트를_반환한다() {
        //given
        final String accessToken = getAccessToken();

        //when
        ExtractableResponse<Response> 수업_모두_조회 = 수업_모두_조회(accessToken);

        //then
        assertAll(
                () -> assertThat(수업_모두_조회.statusCode()).isEqualTo(200),
                () -> assertThat(수업_모두_조회.jsonPath().getList("content").size()).isEqualTo(0)
        );
    }

    @Test
    void 일반_사용자는_수업을_저장할_수_없다() {
        //given
        final String accessToken = getAccessToken();

        //when
        ExtractableResponse<Response> 수업_등록 = 수업_등록(accessToken, 수업1);

        //then
        assertThat(수업_등록.statusCode()).isEqualTo(403);
    }

    @Test
    void 일반_사용자는_수업을_삭제할_수_없다(){
        //given
        final String accessToken = getAccessToken();

        final String adminAccessToken = getAdminAccessToken();

        ExtractableResponse<Response> 수업_등록 = 수업_등록(adminAccessToken, 수업1);

        long id = 수업_등록.jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> 수업_삭제 = 수업_삭제(accessToken, id);

        //then
        assertThat(수업_삭제.statusCode()).isEqualTo(403);
    }

    @Test
    void 검색어에_해당하는_강의를_조회한다(){
        //given
        final String accessToken = getAccessToken();

        final String adminAccessToken = getAdminAccessToken();

        ExtractableResponse<Response> 수업_등록1 = 수업_등록(adminAccessToken, 수업1);

        ExtractableResponse<Response> 수업_등록2 = 수업_등록(adminAccessToken, 수업2);

        //when
        ExtractableResponse<Response> 검색어에_해당하는_강의_조회 = 검색어에_해당하는_강의_조회(accessToken, "수업1");

        //then
        assertAll(
                () -> assertThat(검색어에_해당하는_강의_조회.statusCode()).isEqualTo(200),
                () -> assertThat(검색어에_해당하는_강의_조회.jsonPath().getList("content").size()).isEqualTo(1)
        );
    }
}