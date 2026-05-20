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



---

### CI integration

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