package org.apitesting.tests.task;

import io.restassured.response.Response;
import org.apitesting.utils.BaseTest;
import org.apitesting.utils.Task;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.apitesting.utils.Task.*;

public class TaskGetTests  extends BaseTest {
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
        shouldEqualStatusCode(response, HTTP_STATUS_NO_CONTENT);
    }

    @Test
    public void getTaskWithExistingId() {
        Response response = sendGetRequestToTaskEndpoint(task.getId());

        shouldEqualStatusCode(response, HTTP_STATUS_OK);
        shouldMatchJsonScheme(response, "schemes/taskGetScheme.json");
        fieldShouldEqualTo(response, ID, task.getId());
        fieldShouldEqualTo(response, SUMMARY, task.getSummary());
        fieldShouldEqualTo(response, DESCRIPTION, task.getDescription());
        fieldShouldEqualTo(response, PRIORITY, task.getPriority());
    }

    @Test
    public void getTaskWithNonExistingId() {
        int nonExistingTaskId = task.getId() + 1;
        Response response = sendGetRequestToTaskEndpoint(nonExistingTaskId);

        shouldEqualStatusCode(response, HTTP_STATUS_NOT_FOUND);
        fieldShouldEqualTo(response, MESSAGE, "Not found");
    }
}
