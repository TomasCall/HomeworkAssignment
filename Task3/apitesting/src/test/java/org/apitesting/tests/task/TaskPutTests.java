package org.apitesting.tests.task;

import io.restassured.response.Response;
import org.apitesting.utils.BaseTest;
import org.apitesting.utils.Task;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.apitesting.utils.Task.*;
import static org.apitesting.utils.Task.sendDeleteRequestToTaskEndpoint;

public class TaskPutTests  extends BaseTest {
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

    @DataProvider(name = "taskUpdateData")
    public Object[][] taskUpdateData() {
        return new Object[][]{
                {new Task("New Task Summary Updated", "New Task Description 1", HIGH)},
                {new Task("New Task Summary", "New Task Description Updated", HIGH)},
                {new Task("New Task Summary", "New Task Description", MEDIUM)},
                {new Task("New Task Summary Updated", "New Task Description Updated", LOW)}
        };
    }

    @Test(dataProvider = "taskUpdateData")
    public void testUpdateTask(Task task) {
        this.task = task;
        Response response = sendPutRequestToTaskEndpoint(task, this.task.getId());

        shouldEqualStatusCode(response, HTTP_STATUS_OK);
        shouldMatchJsonScheme(response, "schemes/taskPutScheme.json");
        fieldShouldEqualTo(response, ID, this.task.getId());
        fieldShouldEqualTo(response, SUMMARY, task.getSummary());
        fieldShouldEqualTo(response, DESCRIPTION, task.getDescription());
        fieldShouldEqualTo(response, PRIORITY, task.getPriority());
    }

    @DataProvider(name = "taskUpdateDataWithIncorrectPriority")
    public Object[][] taskUpdateDataWithIncorrectPriority() {
        return new Object[][]{
                {new Task("New Task Summary", "New Task Description", "high")},
                {new Task("New Task Summary", "New Task Description", "High")},
                {new Task("New Task Summary", "New Task Description", "Lorem")}
        };
    }

    @Test(dataProvider = "taskUpdateDataWithIncorrectPriority")
    public void checkTaskUpdatingWithIncorrectPriority(Task task) {
        Response response = sendPutRequestToTaskEndpoint(task, this.task.getId());

        shouldEqualStatusCode(response, HTTP_STATUS_BAD_REQUEST);
        fieldShouldEqualTo(response, MESSAGE, "Incorrect priority");
    }

    @Test
    public void checkTaskUpdatingWithINonExistingId() {
        int nonExistingId = task.getId() + 1;
        Response response = sendPutRequestToTaskEndpoint(task, nonExistingId);

        shouldEqualStatusCode(response, HTTP_STATUS_NOT_FOUND);
        fieldShouldEqualTo(response, MESSAGE, "Not found");
    }
}
