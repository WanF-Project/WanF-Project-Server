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

import static io.restassured.RestAssured.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate.sql")
public class ControllerTest {
    private static final String BASE_PATH = "/api/v1";

    private static final String COURSE_PATH = "/courses";

    private static final String MAJOR_PATH = "/majors";

    @Autowired
    protected JwtTokenProvider tokenProvider;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected ExtractableResponse<Response> 수업_모두_조회(String accessToken) {
        return SupportRestAssured.get(String.format("%s%s", BASE_PATH, COURSE_PATH),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 수업_조회(String accessToken, Long id) {
        return SupportRestAssured.get(String.format("%s%s/%d", BASE_PATH, COURSE_PATH, id),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 수업_등록(String accessToken, Object body) {
        return SupportRestAssured.post(String.format("%s%s", BASE_PATH, COURSE_PATH),
                Map.of("Authorization", accessToken), body);
    }

    protected  ExtractableResponse<Response> 수업_삭제(String accessToken, Long id) {
        return SupportRestAssured.delete(String.format("%s%s/%d", BASE_PATH, COURSE_PATH, id),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 전공_모두_조회(String accessToken) {
        return SupportRestAssured.get(String.format("%s%s", BASE_PATH, MAJOR_PATH),
                Map.of("Authorization", accessToken));
    }

    protected String getAccessToken() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("email", "qwer@gmail.com");

        given().header("Content-Type", "application/json")
                .body(jsonObject.toJSONString()).log().all().when().post("http://localhost:" + port + "/api/v1/auth/signup/verification-code").then().extract().response();

        jsonObject.put("userPassword", "test");

        given().contentType(ContentType.JSON)
                .body(jsonObject).log().all().when().post("http://localhost:" + port + "/api/v1/auth/signup/user").then().extract().response();

        Response response = given().contentType(ContentType.JSON)
                .body(jsonObject).log().all().when().post("http://localhost:" + port + "/api/v1/auth/login").then().extract().response();

        return response.getHeader("Authorization");
    }
}
