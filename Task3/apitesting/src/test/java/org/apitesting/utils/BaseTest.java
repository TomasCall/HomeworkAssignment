package org.apitesting.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class BaseTest {
    public BaseTest() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    public final int HTTP_STATUS_OK = 200;
    public final int HTTP_STATUS_CREATED = 201;
    public final int HTTP_STATUS_NO_CONTENT = 204;
    public final int HTTP_STATUS_BAD_REQUEST = 400;
    public final int HTTP_STATUS_NOT_FOUND = 404;

    public void shouldEqualStatusCode(Response response, int statusCode) {
        response.then().statusCode(statusCode);
    }

    public void shouldMatchJsonScheme(Response response, String path) {
        response.then().body(matchesJsonSchemaInClasspath(path));
    }

    public void fieldShouldEqualTo(Response response, String field, Object value) {
        response.then().body(field, equalTo(value));
    }

    public void shouldNotHaveNullValue(Response response, String field) {
        response.then().body(field, notNullValue());
    }
}
