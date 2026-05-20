# Task3 Answers

## Documentation for the API

A task has 4 property:
* id (unique)
* summary 
* description
* priority
  * Priority can only have 3 types of values:
    * HIGH
    * MEDIUM
    * LOW

In my scenario I assumed the API would work like this:

## POST /tasks

Endpoint would expect the following json structure as the requestBody with 200 OK:

```json
{
  "summary": "",
  "description": "",
  "priority": ""
}
```
This would be the response for a successful POST request:
```json
{
  "id": 0
  "summary": "",
  "description": "",
  "priority": ""
}
```

For invalid priority the endpoint would return the following json with 400 BAD_REQUEST:

```json
{
  "message": "Incorrect priority"
}
```

## PUT /tasks{id}

Endpoint would expect the following json structure as the requestBody with 200 OK:

```json
{
  "summary": "",
  "description": "",
  "priority": ""
}
```

For invalid priority the endpoint would return the following json with 400 BAD_REQUEST:

```json
{
  "message": "Incorrect priority"
}
```

For not existing id the endpoint would return the following json with 404 NOT_FOUND:

```json
{
  "message": "Not found"
}
```

## DELETE /tasks{id}

Endpoint would expect the following json structure as the requestBody with 200 OK:

```json
{
  "summary": "",
  "description": "",
  "priority": ""
}
```

For not existing id the endpoint would return the following json with 404 NOT_FOUND:

```json
{
  "message": "Not found"
}
```

## GET /tasks{id}

Endpoint would expect the following json structure as the requestBody with 200 OK:

```json
{
  "summary": "",
  "description": "",
  "priority": ""
}
```

For not existing id the endpoint would return the following json with 404 NOT_FOUND:

```json
{
  "message": "Not found"
}
```

---

## Tests

Tests for the endpoints can be found under the /apitesting/src/test/java/org/apitesting/tests/task folders

The following tests were implemented for the 4 methods:
* DELETE
  * Deletion with existing id
  * Deletion with non-existing id
* GET
  * Get task with existing id
  * Get task with non-existing id
* POST
  * Task creation with correct properties
  * Task creation with incorrect priority
* PUT
  * Task update with correct properties
  * Task update with incorrect priority
  * Task update with non-existing id

### Example test file TaskPutTests

```java
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

```

---

## Hierarchy

We have 2 other classes, BaseTest and Task

### BaseTest

```java
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
```

* This is where we set the BaseUri inside the constructor
* Expected status codes so the assertion is easier to read
* Assertion methods which are easier to read inside the test files than the chained response versions

### Task

```java
package org.apitesting.utils;

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
    Map<String, Object> body = new HashMap<>();
    body.put(SUMMARY, task.getSummary());
    body.put(DESCRIPTION, task.getDescription());
    body.put(PRIORITY, task.getPriority());

    return RestAssured.given().contentType(ContentType.JSON)
            .body(body)
            .post("/tasks");
  }

  public static Response sendGetRequestToTaskEndpoint(int id) {
    return RestAssured.given().contentType(ContentType.JSON)
            .pathParam(ID, id)
            .get("/tasks/{id}");
  }

  public static Response sendDeleteRequestToTaskEndpoint(int id) {
    return RestAssured.given().contentType(ContentType.JSON)
            .pathParam(ID, id)
            .delete("/tasks/{id}");
  }

  public static Response sendPutRequestToTaskEndpoint(Task task, int id) {
    return RestAssured.given().contentType(ContentType.JSON)
            .pathParam(ID, id)
            .post("/tasks/{id}");
  }

  public static Task extractTaskFromResponse(Response response) {
    return response.as(Task.class);
  }
}
```
* This is where we store the json keys and priority values, so it is easier to change if the endpoint changes
* Also we have all the 4 types of methods which are easier to read inside the test files
* And also an extract method which returns the response as a Task

We also store the jsonscheme under the: /apitesting/src/test/resources/schemes

```json
{
  "type": "object",
  "required": ["id", "summary", "description", "priority"],
  "properties": {
    "id": {
      "type": "number"
    },
    "summary": {
      "type": "string"
    },
    "description": {
      "type": "string"
    },
    "priority": {
      "type": "string",
      "enum": ["LOW", "MEDIUM", "HIGH", "CRITICAL"]
    }
  }
}
```

---

## Project information

* The project uses Java with REST-assured
* You will need the latest Java version and also maven
* You can run the test with testng

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd" >
<suite name="APITestSuite">
    <test name="APTTest">
        <classes>
            <class name="org.apitesting.tests.task.TaskDeleteTests"/>
            <class name="org.apitesting.tests.task.TaskGetTests"/>
            <class name="org.apitesting.tests.task.TaskPostTests"/>
            <class name="org.apitesting.tests.task.TaskPutTests"/>
        </classes>
    </test>
</suite>

```