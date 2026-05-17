# Task2 Answers

---

## Why Playwright?

Playwright is the most modern any widely used Test Automation Framework. This framework has a lot of built in feature which other automation tools don't have like the playwright tracer and also the storage state where we can save a state of a page. This is useful if we don't want to log in every time we execute a new test.

## Why Typescript?

Since Angular is a Typescript based framework then it is also beneficial if the Test Automation framework is Typescript based because in this case the Automation team is able to get help from the Frontend Team if needed. Another reason would be that on some projects the Automation framework is inside the same repo as the FE.

---

## How to set up the Framework?

1. Install the latest node version from [here](https://nodejs.org/en/download)
2. Install dependencies with the following command: `npm install`
3. Install playwright with the following command `npx playwright install`

---

## How to use the framework?

### Structure

#### Test files

You can find the test files inside the tests folder.

```typescript
import { test } from '@playwright/test';
import { Assertion } from '../utils/assertion';
import { LoginPage } from '../pages/loginPage';
import { TestLogger } from '../helpers/testLogger';
import { Action } from '../utils/action';
import { ShoppingPage } from '../pages/shoppingPage';
import { configManager } from '../config/config';

test.describe('Login tests', ()=>{
    const config = configManager.getConfig();

    let ACTION: Action;
    let CHECK: Assertion;
    let LOGGER: TestLogger; 
    let loginPage: LoginPage;
    let shoppingPage: ShoppingPage;

    test.beforeEach(async ({page})=>{
        ACTION = new Action(page);
        CHECK = new Assertion();
        LOGGER = new TestLogger('Login tests');
        loginPage = new LoginPage(page);
        shoppingPage = new ShoppingPage(page);

        LOGGER.startHookMessage('BEFORE_EACH');
        ACTION.goToPage(config.environement.loginPageConfig.url);
        ACTION.waitForLoginPageToLoad(config.environement.loginPageConfig.defaultWatingTime);
        LOGGER.endHookMessage('BEFORE_EACH');
    });

    test('Test the login functionality with valid user data', async () => {
        await CHECK.shouldBeVisible(loginPage.emailInputField);
        await CHECK.shouldBeVisible(loginPage.passwordInputField);
        await CHECK.shouldBeVisible(loginPage.logInButton);

        await ACTION.sendKeys(loginPage.emailInputField, config.credentials.email);
        await ACTION.sendKeys(loginPage.passwordInputField, config.credentials.password);
        await ACTION.clickElement(loginPage.logInButton);
        await ACTION.waitForShoppingPageToLoad(config.environement.shoppingPageConfig.defaultWatingTime);
        await CHECK.shouldBeVisible(shoppingPage.accountButton);
        await CHECK.shouldBeVisible(shoppingPage.cartButton);
        await CHECK.shouldContainText(shoppingPage.accountButton, config.credentials.firstName);
    });

    test('Test the login functionality with incorrect passwrod', async () => {
        await CHECK.shouldBeVisible(loginPage.emailInputField);
        await CHECK.shouldBeVisible(loginPage.passwordInputField);
        await CHECK.shouldBeVisible(loginPage.logInButton);

        await ACTION.sendKeys(loginPage.emailInputField, config.credentials.email);
        await ACTION.sendKeys(loginPage.passwordInputField, config.credentials.incorrectPassword);
        await ACTION.clickElement(loginPage.logInButton);
        await ACTION.waitForLoginPageToLoad(config.environement.loginPageConfig.defaultWatingTime);
        await CHECK.shouldNotBeVisible(shoppingPage.accountButton);
        await CHECK.shouldNotBeVisible(shoppingPage.cartButton);
    });
});
```