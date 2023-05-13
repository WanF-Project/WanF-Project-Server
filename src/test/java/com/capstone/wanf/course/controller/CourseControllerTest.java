package com.capstone.wanf.course.controller;

import com.capstone.wanf.ControllerTest;
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
        // TODO: 2023/05/13 수업 생성 로직 추가
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