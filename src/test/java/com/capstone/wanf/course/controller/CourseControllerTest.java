package com.capstone.wanf.course.controller;

import com.capstone.wanf.ControllerTest;
import com.capstone.wanf.course.domain.entity.Course;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class CourseControllerTest extends ControllerTest {
    @Test
    void ID에_맞는_수업이_없으면_404를_반환한다(){
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
        final String accessToken = getAccessToken();

        Course course = Course.builder()
                .courseId("과목코드")
                .name("수업명")
                .courseTime("수업시간")
                .professor("교수")
                .category("카테고리")
                .courseId("과목코드")
                .build();

        ExtractableResponse<Response> 수업_등록 = 수업_등록(accessToken, course);

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
        final String accessToken = getAccessToken();

        Course course = Course.builder()
                .courseId("과목코드")
                .name("수업명")
                .courseTime("수업시간")
                .professor("교수")
                .category("카테고리")
                .courseId("과목코드")
                .build();

        ExtractableResponse<Response> 수업_등록 = 수업_등록(accessToken, course);

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
}