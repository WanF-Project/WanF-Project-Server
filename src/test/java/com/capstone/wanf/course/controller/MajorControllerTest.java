package com.capstone.wanf.course.controller;

import com.capstone.wanf.ControllerTest;
import com.capstone.wanf.course.dto.request.MajorRequest;
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
                () -> assertThat(전공_모두_조회.jsonPath().getList("content").size()).isEqualTo(1)
        );
    }

    @Test
    void 전공을_저장한다(){
        //given
        final String accessToken = getAdminAccessToken();

        MajorRequest 전공_요청 = new MajorRequest("컴퓨터공학과");
        //when
        ExtractableResponse<Response> 전공_저장 = 전공_등록(accessToken, 전공_요청);
        //then
        assertAll(
                () -> assertThat(전공_저장.statusCode()).isEqualTo(200),
                () -> assertThat(전공_저장.jsonPath().getString("name")).isEqualTo(전공_요청.getName())
        );
    }

    @Test
    void ID에_해당하는_전공을_삭제한다(){
        //given
        final String accessToken = getAdminAccessToken();

        MajorRequest 전공_요청 = new MajorRequest("컴퓨터공학과");

        ExtractableResponse<Response> 전공_저장 = 전공_등록(accessToken, 전공_요청);
        //when
        ExtractableResponse<Response> 전공_삭제 = 전공_삭제(accessToken, 전공_저장.jsonPath().getLong("id"));
        //then
        assertThat(전공_삭제.statusCode()).isEqualTo(204);
    }
}
