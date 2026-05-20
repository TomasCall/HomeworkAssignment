# Bonus Questions

## Docker

Docker is a tool which can package softwares into containers. Docker helps us to deliver the same software to multiple environments and still getting the same result. 

There are 3 parts of Docker which we have to talk about:
* Docker file
* Image
* Container

### Docker file
Is basically a text file where we define how to build our image.

```Dockerfile
# Playwright image
FROM mcr.microsoft.com/playwright:v1.54.0-noble

# Framework repo
WORKDIR /app

# Copy package files
COPY package*.json ./

# Dependency installation
RUN npm install

# Copy the rest of the project
COPY . .

# Run tests
CMD npx playwright test
```

### Container

Docker containers are very similar to a virtual machine, but instead of virtualizing the hardware and the Operating System it only virtualizes the Operating System.

### Image

A blueprint which contains the application code, configurations and dependencies.

### Setting up automated testing environment using Docker

I would use the following workflow for this task:
1. Merge request is created
2. CI starts
3. Build application from pulled image (In this case after every merge to the master a new docker image is created so we just pull it and run it)
4. Docker image is pulling and running for the automation framework (Here we pre-build the images just like in the previous step)
5. Test execution
6. Report generation


---

## JUNIT + Selenium

To run this project you will need Java and Maven installed.

### DeleteTaskTests Class
```java
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

```

As you can see I've followed the same approach here as I did with the playwright/typescript framework.
In the setUp we generate the task via the API. And also we navigate to the page where we can delete the task.
The test itself is structured just like the playwright tests. In the tearDown we close the browser and we send a delete request to the api just to be sure it is deleted.

### UiElement Class
```java
package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openqa.selenium.By;

@AllArgsConstructor
@Getter
public class UiElement {
    private By locator;
    private String name;
}
```

### BaseTest Class

```java
package utils;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Getter
public class BaseTest {
    private WebDriver webDriver;
    int defaultWaitTime = 5;
    private WebDriverWait webDriverWait;

    public void navigateToUrl(String url) {
        webDriver = new ChromeDriver();
        webDriverWait =  new WebDriverWait(webDriver, Duration.ofSeconds(defaultWaitTime));
        webDriver.get(url);
    }

    public void closeBrowser() {
        webDriver.close();
    }
}
```
This is the class where we have the browser opening and closing methods.

### TaskPageClass
```java
package pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.UiElement;

import java.time.Duration;

@Getter
public class TaskPage {
    private static final Logger log = LoggerFactory.getLogger(TaskPage.class);
    private WebDriver webDriver;
    int defaultWaitTime = 5;
    public UiElement taskTitle = new UiElement(By.xpath("//span[@data-testid='taskTitle']"), "taskPage.taskTitle");
    public UiElement deleteButton = new UiElement(By.xpath("//button[@data-testid='deleteButton']"), "taskPage.deleteButton");
    public UiElement deleteDialog = new UiElement(By.xpath("//div[@data-testid='deleteDialog']"), "taskPage.deleteDialog");
    public UiElement yesButton = new UiElement(By.xpath("//button[@data-testid='yesButton']"), "taskPage.deleteDialog.yesButton");
    public UiElement noButton = new UiElement(By.xpath("//button[@data-testid='noButton']"), "taskPage.deleteDialog.noButton");
    public UiElement successToastMessage = new UiElement(By.xpath("//div[@data-testid='successToastMessage']"), "taskPage.successToastMessage");


    public TaskPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void waitForTaskPageToLoad() {
        log.info("[ACTION] Wait for task page to load");
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(defaultWaitTime));
        wait.until(ExpectedConditions.visibilityOfElementLocated(taskTitle.getLocator()));
        log.info("[ACTION] Task page is fully loaded");
    }
}
```
This is where we have the page specific properties and locators. Locators are stored in a custom object which have 2 properties. Locator and a name which represents not only the name of the element but also the where it is located (taskTitle is located on the taskPage so the name will be taskPage.taskTitle) 

### Assert class
```java
package utils;

import lombok.AllArgsConstructor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class Assert {
    private WebDriverWait webDriverWait;
    private static final Logger log = LoggerFactory.getLogger(Assert.class);
    public void shouldBeVisible(UiElement uiElement) {
        log.info("[CHECK] Checking visibility of: {}", uiElement.getName());
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(uiElement.getLocator()));
        log.info("[CHECK] {} is visible", uiElement.getName());
    }

    public void shouldHaveText(UiElement uiElement, String expectedText) {
        log.info("[CHECK] {} should have text: {}", uiElement.getName(), expectedText);
        WebElement element = webDriverWait.until(
                ExpectedConditions.visibilityOfElementLocated(uiElement.getLocator())
        );

        String actualText = element.getText();
        if (!actualText.contains(expectedText)) {
            throw new AssertionError("Expected text: " + expectedText + " but found: " + actualText);
        }
        log.info("[CHECK] {} element has text: {}", uiElement.getName(), expectedText);
    }
}

```

### Action class
```java
package utils;

import lombok.AllArgsConstructor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class Action {
    private WebDriverWait webDriverWait;

    private static final Logger log = LoggerFactory.getLogger(Action.class);

    public void click(UiElement uiElement) {
        log.info("[ACTION] Clicking on element: {}", uiElement.getName());
        webDriverWait.until(ExpectedConditions.elementToBeClickable(uiElement.getLocator())).click();
        log.info("[ACTION] Successfully clicked on: {}", uiElement.getName());
    }
}

```

### Task class

```java
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
```
This class is under the api package and this is where we have the api methods which allow us to generate and delete the test data.

---

## CI integration

I would integrate my tests into the ci with the following yml file:

```yaml
image: mcr.microsoft.com/playwright:v1.54.0-noble

stages:
  - install
  - test

install_dependencies:
  stage: install
  script:
    - npm ci --cache .npm --prefer-offline

tests:
  stage: test
  script:
    - npx playwright test
  dependencies:
    - install_dependencies
      
  artifacts:
    when: on_failure
    paths:
      - playwright-report/
      - test-results/

  rules:
    - if: $CI_PIPELINE_SOURCE == "merge_request_event"
```

I used gitlab.yml since that is what I am most familiar with. I used the recommended playwright image. We have 2 stages 1 where we install the dependencies and one where we run the tests. This runs every time when a new merge request is created in the TA repo. If the pipeline fails we export our artifacts because they are not needed if all the tests are passing. Also in gitlab we can set how long we want to keep our artifacts. 