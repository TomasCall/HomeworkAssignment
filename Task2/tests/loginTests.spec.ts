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
        await ACTION.goToPage(config.environement.loginPageConfig.url);
        await ACTION.waitForLoginPageToLoad(config.environement.loginPageConfig.defaultWatingTime);
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