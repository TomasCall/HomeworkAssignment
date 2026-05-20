package api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public class Task {
    public static final String ID = "id";
    public static final String SUMMARY = "summary";
    public static final String DESCRIPTION = "description";
    public static final String PRIORITY = "priority";
    public static final String MESSAGE = "message";

    private String summary;
    private String description;
    private String priority;
    private int id;

    public Task(String summary, String description, String priority) {
        this.summary = summary;
        this.description = description;
        this.priority = priority;
    }

    public final int HTTP_STATUS_CREATED = 201;
    public Response sendPostRequestToTaskEndpoint() {
        Map<String, Object> body = new HashMap<>();
        body.put(SUMMARY, getSummary());
        body.put(DESCRIPTION, getDescription());
        body.put(PRIORITY, getPriority());

        Response response = RestAssured.given().contentType(ContentType.JSON)
                .body(body)
                .post("/tasks");
        response.then().statusCode(HTTP_STATUS_CREATED);
        return response;
    }

    public void sendDeleteRequestToTaskEndpoint() {
        RestAssured.given().contentType(ContentType.JSON)
                .pathParam(ID, getId())
                .delete("/tasks/{id}");
    }

    public Task extractTaskFromResponse(Response response) {
        return response.as(Task.class);
    }
}
