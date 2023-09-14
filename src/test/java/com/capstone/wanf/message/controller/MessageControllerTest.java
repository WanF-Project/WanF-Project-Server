package com.capstone.wanf.message.controller;

import com.capstone.wanf.ControllerTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static com.capstone.wanf.fixture.DomainFixture.쪽지_요청1;
import static com.capstone.wanf.fixture.DomainFixture.쪽지_요청2;
import static org.assertj.core.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MessageControllerTest extends ControllerTest {
    @Test
    @Order(1)
    void 쪽지를_수신한다(){
        // given
        final String accessToken = getAccessToken();

        final String adminAccessToken = getAdminAccessToken();

        쪽지_송신(accessToken, 쪽지_요청2);
        // when
        ExtractableResponse<Response> 쪽지_수신 = 쪽지_수신(adminAccessToken, 1L);
        // then
        assertThat(쪽지_수신.statusCode()).isEqualTo(200);
    }

    @Test
    @Order(2)
    void 쪽지를_송신한다(){
        // given
        final String accessToken = getAccessToken();

        getAdminAccessToken();
        // when
        ExtractableResponse<Response> 쪽지_송신 = 쪽지_송신(accessToken, 쪽지_요청2);
        // then
        assertThat(쪽지_송신.statusCode()).isEqualTo(200);
    }

    @Test
    @Order(3)
    void 자신에게_쪽지를_보낼_수_없다(){
        // given
        final String accessToken = getAccessToken();

        getAdminAccessToken();
        // when
        ExtractableResponse<Response> 쪽지_송신 = 쪽지_송신(accessToken, 쪽지_요청1);
        // then
        assertThat(쪽지_송신.statusCode()).isEqualTo(400);
    }


    @Test
    @Order(4)
    void 쪽지를_주고받은_사람들을_조회한다(){
        // given
        final String accessToken = getAccessToken();

        final String adminAccessToken = getAdminAccessToken();

        쪽지_송신(accessToken, 쪽지_요청1);
        // when
        ExtractableResponse<Response> 쪽지_주고받은_사람들_조회 = 쪽지_주고받은_사람들_조회(adminAccessToken);
        // then
        assertThat(쪽지_주고받은_사람들_조회.statusCode()).isEqualTo(200);
    }
}
