package org.apitesting.tests.task;

import io.restassured.response.Response;
import org.apitesting.utils.BaseTest;
import org.apitesting.utils.Task;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.apitesting.utils.Task.*;
import static org.apitesting.utils.Task.MESSAGE;

public class TaskDeleteTests extends BaseTest {
    Task task;

    @BeforeMethod
    public void setUp() {
        Response response = sendPostRequestToTaskEndpoint(new Task("New Task Summary 1", "New Task Description 1", HIGH));
        shouldEqualStatusCode(response, HTTP_STATUS_CREATED);
        task = extractTaskFromResponse(response);
    }

    @AfterMethod
    public void cleanUp() {
        Response response = sendDeleteRequestToTaskEndpoint(task.getId());
    }

    @Test
    public void deleteTaskWithExistingId() {
        Response response = sendDeleteRequestToTaskEndpoint(task.getId());

        shouldEqualStatusCode(response, HTTP_STATUS_NO_CONTENT);
        shouldMatchJsonScheme(response, "schemes/taskDeleteScheme.json");
    }

    @Test
    public void deleteTaskWithNonExistingId() {
        int nonExistingTaskId = task.getId() + 1;
        Response response = sendDeleteRequestToTaskEndpoint(nonExistingTaskId);

        shouldEqualStatusCode(response, HTTP_STATUS_NOT_FOUND);
        fieldShouldEqualTo(response, MESSAGE, "Not found");
    }
}
