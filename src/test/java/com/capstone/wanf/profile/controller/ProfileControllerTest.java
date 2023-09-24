package com.capstone.wanf.profile.controller;

import com.capstone.wanf.ControllerTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static com.capstone.wanf.fixture.DomainFixture.프로필_이미지_수정2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ProfileControllerTest extends ControllerTest {
    @Test
    void ID에_맞는_프로필이_없으면_404를_반환한다() {
        //given
        final String accessToken = getAccessToken();

        //given & when
        ExtractableResponse<Response> 프로필_조회 = 프로필_조회(accessToken, 10L);

        //then
        assertThat(프로필_조회.statusCode()).isEqualTo(404);
    }

    @Test
    void ID에_맞는_프로필을_조회한다() {
        //given
        final String accessToken = getAccessToken();

        //when
        ExtractableResponse<Response> 프로필_조회 = 프로필_조회(accessToken, 1L);

        //then
        assertAll(
                () -> assertThat(프로필_조회.statusCode()).isEqualTo(200),
                () -> assertThat(프로필_조회.jsonPath().getLong("id")).isEqualTo(1L)
        );
    }

    @Test
    void 랜덤_프로필_목록을_조회한다() {
        //given
        final String accessToken = getAccessToken();

        final String adminToken = getAdminAccessToken();

        Pageable pageable = PageRequest.of(0, 5);

        //when
        ExtractableResponse<Response> 랜덤_프로필_목록_조회 = 랜덤_프로필_목록_조회(accessToken, pageable);

        //then
        assertAll(
                () -> assertThat(랜덤_프로필_목록_조회.statusCode()).isEqualTo(200),
                () -> assertThat(랜덤_프로필_목록_조회.jsonPath().getList("content").size()).isEqualTo(1)
        );
    }

    @Test
    void 나의_프로필을_조회한다() {
        //given
        final String accessToken = getAccessToken();

        //given & when
        ExtractableResponse<Response> 프로필_조회 = 프로필_조회(accessToken);

        //then
        assertThat(프로필_조회.statusCode()).isEqualTo(200);
    }

    @Test
    void 프로필의_필드를_수정한다() {
        //given
        final String accessToken = getAccessToken();

        //when
        ExtractableResponse<Response> 프로필_수정 = 프로필_수정(accessToken, 프로필_이미지_수정2);

        //then
        assertThat(프로필_수정.statusCode()).isEqualTo(200);
    }

    @Test
    void 프로필_성격_리스트를_조회한다() {
        //given
        final String accessToken = getAccessToken();

        //when
        ExtractableResponse<Response> 프로필_성격_리스트_조회 = 프로필_성격_리스트_조회(accessToken);

        //then
        assertThat(프로필_성격_리스트_조회.statusCode()).isEqualTo(200);
    }

    @Test
    void 프로필_목표_리스트를_조회한다() {
        //given
        final String accessToken = getAccessToken();

        //when
        ExtractableResponse<Response> 프로필_목표_리스트_조회 = 프로필_목표_리스트_조회(accessToken);

        //then
        assertThat(프로필_목표_리스트_조회.statusCode()).isEqualTo(200);
    }

    @Test
    void MBTI_리스트를_조회한다() {
        //given
        final String accessToken = getAccessToken();

        //when
        ExtractableResponse<Response> MBTI_리스트_조회 = MBTI_리스트를_조회한다(accessToken);

        //then
        assertThat(MBTI_리스트_조회.statusCode()).isEqualTo(200);
    }

}