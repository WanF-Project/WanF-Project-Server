package com.capstone.wanf.user.controller;

import com.capstone.wanf.ControllerTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * 토큰_유효성_검사는 토큰이 만료되었는지 안되었는지를 기준으로 검사하기 때문에 테스트가 불가하여 제외하였다.
 */

class UserControllerTest extends ControllerTest {
    @Test
    void 로그아웃_요청(){
        //given
        String accessToken = getAccessToken();
        //when
        ExtractableResponse<Response> 로그아웃_요청 = 로그아웃_요청(accessToken);
        //then
        assertThat(로그아웃_요청.statusCode()).isEqualTo(200);
    }

    @Test
    void AT_토큰_재발급_요청시_재발급이_되지_않으면_401_반환된다(){
        //given
        String accessToken = getAccessToken();
        //when
        ExtractableResponse<Response> 토큰_재발급_요청_실패 = 토큰_재발급_요청_실패(accessToken);
        //then
        assertThat(토큰_재발급_요청_실패.statusCode()).isEqualTo(401);
    }

    @Test
    void AT_재발급_요청(){
        //given
        List<String> accessTokenAndRefreshToken = getAccessTokenAndRefreshToken();
        //when
        ExtractableResponse<Response> 토큰_재발급_요청 = 토큰_재발급_요청(accessTokenAndRefreshToken);
        //then
        assertThat(토큰_재발급_요청.statusCode()).isEqualTo(200);
    }

    @Test
    void 토큰_유효성_검사_요청(){
        //given
        String accessToken = getAccessToken();
        //when
        ExtractableResponse<Response> 토큰_유효성_검사_요청 = 토큰_유효성_검사_요청(accessToken);
        //then
        assertThat(토큰_유효성_검사_요청.statusCode()).isEqualTo(200);
    }
}