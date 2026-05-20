package tests;

import api.Task;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.TaskPage;
import utils.Action;
import utils.Assert;
import utils.BaseTest;

public class DeleteTaskTests extends BaseTest {
    Task task;
    TaskPage taskPage;
    Action ACTION;
    Assert CHECK;

    @BeforeEach
    public void setUp() {
        task = new Task("New Task Summary 1", "New Task Description 1", "HIGH");
        Response response = task.sendPostRequestToTaskEndpoint();
        Task newTask = task.extractTaskFromResponse(response);
        navigateToUrl("localhost:8080/task/" + newTask.getId());
        ACTION = new Action(getWebDriverWait());
        taskPage = new TaskPage(getWebDriver());
        taskPage.waitForTaskPageToLoad();
    }

    @Test
    public void testDeleteTask() {
        CHECK.shouldHaveText(taskPage.getTaskTitle(), task.getSummary());
        CHECK.shouldBeVisible(taskPage.getDeleteButton());

        ACTION.click(taskPage.getDeleteButton());
        CHECK.shouldBeVisible(taskPage.getDeleteDialog());
        CHECK.shouldBeVisible(taskPage.getYesButton());
        CHECK.shouldBeVisible(taskPage.getNoButton());

        ACTION.click(taskPage.getYesButton());
        CHECK.shouldBeVisible(taskPage.getSuccessToastMessage());
    }

    @AfterEach
    public void tearDown() {
        closeBrowser();
        task.sendDeleteRequestToTaskEndpoint();
    }
}
