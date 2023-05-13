package com.capstone.wanf;

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