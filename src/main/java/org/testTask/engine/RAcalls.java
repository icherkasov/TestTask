package org.testTask.engine;


import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testTask.ENUM.ENDPOINT;
import org.testng.internal.collections.Pair;

import java.util.Map;

import static io.restassured.RestAssured.given;


public class RAcalls {

    public static Response sendGet(ENDPOINT endpoint, Pair<String, Object> pathParam, Map<String, Object> queryParams) {
        return given()
                .contentType(ContentType.JSON)
                .log().all()
                .pathParam(pathParam.first(), pathParam.second())
                .queryParams(queryParams)
                .when()
                .get(endpoint.getEndpoint());
    }

    public static Response sendGet(ENDPOINT endpoint) {
        return given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .get(endpoint.getEndpoint());
    }

    public static Response sendGet(ENDPOINT endpoint, Pair<String, Object> pathParam) {
        return given()
                .contentType(ContentType.JSON)
                .log().all()
                .pathParam(pathParam.first(), pathParam.second())
                .when()
                .get(endpoint.getEndpoint());
    }

    public static Response sendGet(ENDPOINT endpoint, Map<String, Object> queryParams) {
        return given()
                .contentType(ContentType.JSON)
                .log().all()
                .queryParams(queryParams)
                .when()
                .get(endpoint.getEndpoint());
    }

    public static Response sendPost(ENDPOINT endpoint, Object body) {
        return given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .body(body)
                .post(endpoint.getEndpoint());

    }

    public static Response sendPatch(ENDPOINT endpoint, Pair<String, Object> pathParam1, Pair<String, Object> pathParam2, Object body) {
        return given()
                .contentType(ContentType.JSON)
                .log().all()
                .pathParam(pathParam1.first(), pathParam1.second())
                .pathParam(pathParam2.first(), pathParam2.second())
                .when()
                .body(body)
                .patch(endpoint.getEndpoint());
    }

    public static Response sendDelete(ENDPOINT endpoint, Pair<String, Object> pathParam, Object body) {
        return given()
                .contentType(ContentType.JSON)
                .log().all()
                .pathParam(pathParam.first(), pathParam.second())
                .when()
                .body(body)
                .delete(endpoint.getEndpoint());
    }
}
