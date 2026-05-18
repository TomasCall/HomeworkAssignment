package org.apitesting.utils.task;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Task {
    // JSON KEYS
    public static final String ID = "id";
    public static final String SUMMARY = "summary";
    public static final String DESCRIPTION = "description";
    public static final String PRIORITY = "priority";
    public static final String MESSAGE = "message";
    // PRIORITIES
    public static final String HIGH = "HIGH";
    public static final String MEDIUM = "MEDIUM";
    public static final String LOW = "LOW";

    public Task(String summary, String description, String priority) {
        this.summary = summary;
        this.description = description;
        this.priority = priority;
    }

    private String summary;
    private String description;
    private String priority;
    private int id;

    public static Response sendPostRequestToTaskEndpoint(Task task) {
        return RestAssured.given().contentType(ContentType.JSON)
                .body(task)
                .post("/task");
    }

    public static Response sendGetRequestToTaskEndpoint(int id) {
        return RestAssured.given().contentType(ContentType.JSON)
                .pathParam(ID, id)
                .get("/task/{id}");
    }

    public static Response sendDeleteRequestToTaskEndpoint(int id) {
        return RestAssured.given().contentType(ContentType.JSON)
                .pathParam(ID, id)
                .delete("/task/{id}");
    }

    public static Response sendPutRequestToTaskEndpoint(Task task, int id) {
        return RestAssured.given().contentType(ContentType.JSON)
                .pathParam(ID, id)
                .post("/task/{id}");
    }

    public static Task extractTaskFromResponse(Response response) {
        return (Task) response.then().extract();
    }
}
