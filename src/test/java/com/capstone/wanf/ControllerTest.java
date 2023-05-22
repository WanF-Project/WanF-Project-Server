package com.capstone.wanf;

import com.capstone.wanf.auth.jwt.provider.JwtTokenProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.util.Map;

import static com.capstone.wanf.SupportRestAssured.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class ControllerTest {
    private static final String BASE_PATH = "/api/v1";

    private static final String COURSE_PATH = "/courses";

    private static final String MAJOR_PATH = "/majors";

    private static final String AUTH_PATH = "/auth";

    private static final String SIGN_UP_PATH = "/signup";

    private static final String LOGIN_PATH = "/login";

    private static final String PROFILE_PATH = "/profiles";



    @Autowired
    protected JwtTokenProvider tokenProvider;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected ExtractableResponse<Response> 수업_모두_조회(String accessToken) {
        return get(String.format("%s%s", BASE_PATH, COURSE_PATH),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 수업_조회(String accessToken, Long id) {
        return get(String.format("%s%s/%d", BASE_PATH, COURSE_PATH, id),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 수업_등록(String accessToken, Object body) {
        return post(String.format("%s%s", BASE_PATH, COURSE_PATH),
                Map.of("Authorization", accessToken), body);
    }

    protected  ExtractableResponse<Response> 수업_삭제(String accessToken, Long id) {
        return delete(String.format("%s%s/%d", BASE_PATH, COURSE_PATH, id),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 전공_모두_조회(String accessToken) {
        return get(String.format("%s%s", BASE_PATH, MAJOR_PATH),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 프로필_조회(String accessToken){
        return get(String.format("%s%s", BASE_PATH, PROFILE_PATH),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 프로필_조회(String accessToken, Long id) {
        return get(String.format("%s%s/%d", BASE_PATH, PROFILE_PATH, id),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 프로필_수정(String accessToken, Object body) {
        return patch(String.format("%s%s", BASE_PATH, PROFILE_PATH),
                Map.of("Authorization", accessToken), body);
    }

    protected ExtractableResponse<Response> 프로필_성격_리스트_조회(String accessToken) {
        return get(String.format("%s%s/%s", BASE_PATH, PROFILE_PATH, "personalities"),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 프로필_목표_리스트_조회(String accessToken) {
        return get(String.format("%s%s/%s", BASE_PATH, PROFILE_PATH, "goals"),
                Map.of("Authorization", accessToken));
    }

    protected String getAccessToken() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("email", "qwer@gmail.com");

        post(String.format("%s%s%s/%s", BASE_PATH, AUTH_PATH, SIGN_UP_PATH,"verification-code"), jsonObject);

        jsonObject.put("userPassword", "test");

        post(String.format("%s%s%s/%s", BASE_PATH, AUTH_PATH, SIGN_UP_PATH, "user"), jsonObject);

        ExtractableResponse<Response> response = post(String.format("%s%s%s", BASE_PATH, AUTH_PATH, LOGIN_PATH), jsonObject);

        return response.header("Authorization");
    }
}
