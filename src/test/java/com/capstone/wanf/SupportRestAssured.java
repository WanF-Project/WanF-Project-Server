package com.capstone.wanf;

import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class SupportRestAssured {
    public static ExtractableResponse<Response> get(String url) {
        return get(url, null);
    }

    public static ExtractableResponse<Response> get(String url, Map<String, String> headers) {
        return thenExtract(givenWithHeaders(headers).when().get(url));
    }

    public static ExtractableResponse<Response> post(String url, Object body) {
        return post(url, null, body);
    }

    public static ExtractableResponse<Response> post(String url, Map<String, String> headers) {
        return post(url, headers, null);
    }

    public static ExtractableResponse<Response> post(String url, Map<String, String> headers, Object body) {
        final RequestSpecification given = givenWithHeaders(headers);

        if (body != null) {
            given.body(body);
        }

        return thenExtract(given.contentType(ContentType.JSON).when().post(url));
    }

    public static ExtractableResponse<Response> patch(String url, Map<String, String> headers, Object body) {
        final RequestSpecification given = givenWithHeaders(headers);

        if (body != null) {
            given.body(body);
        }

        return thenExtract(given.contentType(ContentType.JSON).when().patch(url));
    }

    public static ExtractableResponse<Response> delete(String url, Map<String, String> headers) {
        final RequestSpecification given = givenWithHeaders(headers);

        return thenExtract(given.contentType(ContentType.JSON).when().delete(url));
    }

    private static ExtractableResponse<Response> thenExtract(Response response) {
        return response.then().log().all().extract();
    }

    private static RequestSpecification givenWithHeaders(Map<String, String> headers) {
        final RequestSpecification given = given();

        if (headers != null) {
            given.headers(headers);
        }

        return given;
    }
}