package org.apitesting.tests.task;

import io.restassured.response.Response;
import org.apitesting.utils.BaseTest;
import org.apitesting.utils.task.Task;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.apitesting.utils.task.Task.*;

public class TaskPostTests extends BaseTest {
    Task task;

    @AfterMethod
    public void cleanUp() {
        Response response = sendDeleteRequestToTaskEndpoint(task.getId());
        shouldEqualStatusCode(response, HTTP_STATUS_NO_CONTENT);
    }

    @DataProvider(name = "taskCreationData")
    public Object[][] taskCreationData() {
        return new Object[][]{
                {new Task("New Task Summary 1", "New Task Description 1", HIGH)},
                {new Task("New Task Summary 2", "New Task Description 2", MEDIUM)},
                {new Task("New Task Summary 3", "New Task Description 3", LOW)}
        };
    }

    @Test(dataProvider = "taskCreationData")
    public void createNewTask(Task task) {
        this.task = task;
        Response response = sendPostRequestToTaskEndpoint(task);

        shouldEqualStatusCode(response, HTTP_STATUS_CREATED);
        shouldMatchJsonScheme(response, "schemes/taskPostScheme.json");
        shouldNotHaveNullValue(response, ID);
        fieldShouldEqualTo(response, SUMMARY, task.getSummary());
        fieldShouldEqualTo(response, DESCRIPTION, task.getDescription());
        fieldShouldEqualTo(response, PRIORITY, task.getPriority());
    }

    @DataProvider(name = "taskCreationDataWithIncorrectPriority")
    public Object[][] taskCreationDataWithIncorrectPriority() {
        return new Object[][]{
                {new Task("New Task Summary 1", "New Task Description 1", "high")},
                {new Task("New Task Summary 2", "New Task Description 2", "High")},
                {new Task("New Task Summary 3", "New Task Description 3", "Lorem")}
        };
    }

    @Test(dataProvider = "taskCreationDataWithIncorrectPriority")
    public void checkTaskCreationWithIncorrectPriority(Task task) {
        Response response = sendPostRequestToTaskEndpoint(task);

        shouldEqualStatusCode(response, HTTP_STATUS_BAD_REQUEST);
        fieldShouldEqualTo(response, MESSAGE, "Incorrect priority");
    }
}
