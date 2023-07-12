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
    void ID에_맞는_강의가_없으면_404를_반환한다() {
        //given
        final String accessToken = getAccessToken();

        //given & when
        ExtractableResponse<Response> 강의_조회 = 강의_조회(accessToken, 1L);

        //then
        assertThat(강의_조회.statusCode()).isEqualTo(404);
    }

    @Test
    void ID에_맞는_강의를_조회한다() {
        //given
        final String accessToken = getAdminAccessToken();

        ExtractableResponse<Response> 강의_등록 = 강의_등록(accessToken, 강의_요청1);

        //when
        ExtractableResponse<Response> 강의_조회 = 강의_조회(accessToken, 강의_등록.jsonPath().getLong("id"));

        //then
        assertAll(
                () -> assertThat(강의_조회.statusCode()).isEqualTo(200),
                () -> assertThat(강의_조회.jsonPath().getLong("id")).isEqualTo(강의_등록.jsonPath().getLong("id"))
        );
    }

    @Test
    void 강의를_삭제한다() {
        //given
        final String accessToken = getAdminAccessToken();

        ExtractableResponse<Response> 강의_등록 = 강의_등록(accessToken, 강의_요청1);

        //when
        ExtractableResponse<Response> 강의_삭제 = 강의_삭제(accessToken, 강의_등록.jsonPath().getLong("id"));

        //then
        assertThat(강의_삭제.statusCode()).isEqualTo(204);
    }

    @Test
    void 강의_리스트를_조회시_강의가_없으면_빈리스트를_반환한다() {
        //given
        final String accessToken = getAccessToken();

        //when
        ExtractableResponse<Response> 강의_모두_조회 = 강의_모두_조회(accessToken);

        //then
        assertAll(
                () -> assertThat(강의_모두_조회.statusCode()).isEqualTo(200),
                () -> assertThat(강의_모두_조회.jsonPath().getList("content").size()).isEqualTo(0)
        );
    }

    @Test
    void 일반_사용자는_강의를_저장할_수_없다() {
        //given
        final String accessToken = getAccessToken();

        //when
        ExtractableResponse<Response> 강의_등록 = 강의_등록(accessToken, 강의1);

        //then
        assertThat(강의_등록.statusCode()).isEqualTo(403);
    }

    @Test
    void 일반_사용자는_수업을_삭제할_수_없다(){
        //given
        final String accessToken = getAccessToken();

        final String adminAccessToken = getAdminAccessToken();

        ExtractableResponse<Response> 강의_등록 = 강의_등록(adminAccessToken, 강의1);

        long id = 강의_등록.jsonPath().getLong("id");

        //when
        ExtractableResponse<Response> 강의_삭제 = 강의_삭제(accessToken, id);

        //then
        assertThat(강의_삭제.statusCode()).isEqualTo(403);
    }

    @Test
    void 검색어에_해당하는_강의를_조회한다(){
        //given
        final String accessToken = getAccessToken();

        final String adminAccessToken = getAdminAccessToken();

        강의_등록(adminAccessToken, 강의1);

        강의_등록(adminAccessToken, 강의2);

        //when
        ExtractableResponse<Response> 검색어에_해당하는_강의_조회 = 검색어에_해당하는_강의_조회(accessToken, "강의1");

        //then
        assertAll(
                () -> assertThat(검색어에_해당하는_강의_조회.statusCode()).isEqualTo(200),
                () -> assertThat(검색어에_해당하는_강의_조회.jsonPath().getList("content").size()).isEqualTo(1)
        );
    }
}