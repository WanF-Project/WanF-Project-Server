package com.capstone.wanf.club.controller;

import com.capstone.wanf.ControllerTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static com.capstone.wanf.fixture.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ClubControllerTest extends ControllerTest {
    @Test
    void 모임을_생성한다() {
        //given
        String accessToken = getAccessToken();
        //when
        ExtractableResponse<Response> 모임_생성 = 모임_생성(accessToken, 모임_요청1);
        //then
        assertAll(
                () -> assertThat(모임_생성.statusCode()).isEqualTo(200),
                () -> assertThat(모임_생성.body().jsonPath().getLong("id")).isNotNull()
        );
    }

    @Test
    void 모임을_모두_조회한다(){
        //given
        String accessToken = getAccessToken();

        모임_생성(accessToken, 모임_요청1);
        //when
        ExtractableResponse<Response> 모임_모두_조회 = 모임_모두_조회(accessToken);
        //then
        assertAll(
                () -> assertThat(모임_모두_조회.statusCode()).isEqualTo(200),
                () -> assertThat(모임_모두_조회.body().jsonPath().getList("id").size()).isEqualTo(1)
        );
    }

    @Test
    void 모임에_가입한다(){
        //given
        String accessToken = getAccessToken();

        String adminAccessToken = getAdminAccessToken();

        모임_생성(accessToken, 모임_요청2);
        //when
        ExtractableResponse<Response> 모임_가입 = 모임_가입(adminAccessToken, 모임_비밀번호_요청1);
        //then
        assertThat(모임_가입.statusCode()).isEqualTo(200);
    }

    @Test
    void 모임_권한을_확인한다(){
        //given
        String accessToken = getAccessToken();

        String adminAccessToken = getAdminAccessToken();

        모임_생성(accessToken, 모임_요청2);

        모임_가입(adminAccessToken, 모임_비밀번호_요청1);
        //when
        ExtractableResponse<Response> 모임_권한_확인 = 모임_권한_확인(adminAccessToken, 1L);
        //then
        assertThat(모임_권한_확인.statusCode()).isEqualTo(200);
    }

    @Test
    void 모임_비밀번호를_조회한다(){
        //given
        String accessToken = getAccessToken();

        모임_생성(accessToken, 모임_요청2);
        //when
        ExtractableResponse<Response> 모임_비밀번호_조회 = 모임_비밀번호_확인(accessToken, 1L);
        //then
        assertThat(모임_비밀번호_조회.statusCode()).isEqualTo(200);
    }
}