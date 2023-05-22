package com.capstone.wanf.course.controller;

import com.capstone.wanf.ControllerTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MajorControllerTest extends ControllerTest {
    @Test
    void 모든_전공을_조회한다(){
        //given
        final String accessToken = getAccessToken();

        //when
        ExtractableResponse<Response> 전공_모두_조회 = 전공_모두_조회(accessToken);

        //then
        assertAll(
                () -> assertThat(전공_모두_조회.statusCode()).isEqualTo(200),
                () -> assertThat(전공_모두_조회.jsonPath().getList("content").size()).isEqualTo(0)
        );
    }
}
