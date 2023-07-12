package com.capstone.wanf;

import com.capstone.wanf.auth.jwt.provider.JwtTokenProvider;
import com.capstone.wanf.post.domain.entity.Category;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Pageable;
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

    private static final String POST_PATH = "/posts";

    private static final String ADMIN_PATH = "/admin";

    private static final String COMMENT_PATH = "/comments";

    private static final String SEARCH_PATH = "/search";

    @Autowired
    protected JwtTokenProvider tokenProvider;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    protected ExtractableResponse<Response> 강의_모두_조회(String accessToken) {
        return get(String.format("%s%s", BASE_PATH, COURSE_PATH),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 강의_조회(String accessToken, Long id) {
        return get(String.format("%s%s/%d", BASE_PATH, COURSE_PATH, id),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 강의_등록(String accessToken, Object body) {
        return post(String.format("%s%s", BASE_PATH, COURSE_PATH),
                Map.of("Authorization", accessToken), body);
    }

    protected  ExtractableResponse<Response> 강의_삭제(String accessToken, Long id) {
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

    protected ExtractableResponse<Response> 전공_등록(String accessToken, Object body) {
        return post(String.format("%s%s", BASE_PATH, MAJOR_PATH),
                Map.of("Authorization", accessToken), body);
    }

    protected ExtractableResponse<Response> 전공_삭제(String accessToken, Long id) {
        return delete(String.format("%s%s/%d", BASE_PATH, MAJOR_PATH, id),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> MBTI_리스트를_조회한다(String accessToken) {
        return get(String.format("%s%s/%s", BASE_PATH,PROFILE_PATH, "mbti"),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 게시글_생성(String accessToken, Category category, Object body) {
        return post(String.format("%s%s?category=%s", BASE_PATH,POST_PATH,category.name()),
                Map.of("Authorization", accessToken),body);
    }

    protected ExtractableResponse<Response> 게시글_수정(String accessToken, Long id, Object body) {
        return patch(String.format("%s%s/%d", BASE_PATH, POST_PATH, id),
                Map.of("Authorization", accessToken),body);
    }

    protected ExtractableResponse<Response> 게시글_삭제(String accessToken, Long id) {
        return delete(String.format("%s%s/%d", BASE_PATH, POST_PATH, id),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 게시글_조회(String accessToken,Long id) {
        return get(String.format("%s%s/%d", BASE_PATH, POST_PATH, id),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 페이징_없는_게시글_모두_조회(String accessToken, Category category) {
        return get(String.format("%s%s?category=%s", BASE_PATH, POST_PATH, category.name()),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 페이징_적용한_게시글_모두_조회(String accessToken, Category category, Pageable pageable) {
        return get(String.format("%s%s?category=%s&page=%d&size=%d", BASE_PATH, "/posts-pageable", category.name(),pageable.getPageNumber(),pageable.getPageSize()),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 댓글_생성(String accessToken, Long postId, Object body) {
        return post(String.format("%s%s/%d%s", BASE_PATH, POST_PATH, postId, COMMENT_PATH),
                Map.of("Authorization", accessToken),body);
    }

    protected ExtractableResponse<Response> 댓글_수정(String accessToken, Long postId,Long commentId, Object body) {
        return patch(String.format("%s%s/%d%s/%d", BASE_PATH, POST_PATH, postId, COMMENT_PATH, commentId),
                Map.of("Authorization", accessToken), body);
    }

    protected ExtractableResponse<Response> 댓글_삭제(String accessToken, Long postId, Long commentId) {
        return delete(String.format("%s%s/%d%s/%d", BASE_PATH, POST_PATH, postId, COMMENT_PATH, commentId),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 검색어를_통해_게시물을_조회(String accessToken, Category category, String query, Pageable pageable) {
        return get(String.format("%s%s%s?category=%s&query=%s&page=%d&size=%d", BASE_PATH, POST_PATH, SEARCH_PATH, category.name(), query, pageable.getPageNumber(), pageable.getPageSize()),
                Map.of("Authorization", accessToken));
    }

    protected ExtractableResponse<Response> 검색어에_해당하는_강의_조회(String accessToken, String query) {
        return get(String.format("%s%s%s?s&query=%s", BASE_PATH, COURSE_PATH, SEARCH_PATH, query),
                Map.of("Authorization", accessToken));
    }

    protected String getAccessToken() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("email", "user@gmail.com");

        post(String.format("%s%s%s/%s", BASE_PATH, AUTH_PATH, SIGN_UP_PATH,"verification-code"), jsonObject);

        jsonObject.put("userPassword", "test");

        post(String.format("%s%s%s/%s", BASE_PATH, AUTH_PATH, SIGN_UP_PATH, "user"), jsonObject);

        ExtractableResponse<Response> response = post(String.format("%s%s%s", BASE_PATH, AUTH_PATH, LOGIN_PATH), jsonObject);

        return response.header("Authorization");
    }

    protected String getAdminAccessToken() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("email", "admin@gmail.com");

        post(String.format("%s%s%s/%s", BASE_PATH, AUTH_PATH, SIGN_UP_PATH, "verification-code"), jsonObject);

        jsonObject.put("userPassword", "test");

        post(String.format("%s%s%s/%s", BASE_PATH, AUTH_PATH, SIGN_UP_PATH, "user"), jsonObject);

        ExtractableResponse<Response> userResponse = post(String.format("%s%s%s", BASE_PATH, AUTH_PATH, LOGIN_PATH), jsonObject);

        String accessToken = userResponse.header("Authorization");

        get(String.format("%s%s%s", BASE_PATH, AUTH_PATH, ADMIN_PATH), Map.of("Authorization", accessToken));

        return accessToken;
    }
}
