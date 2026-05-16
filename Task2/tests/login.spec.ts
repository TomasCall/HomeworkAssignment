import { test } from '@playwright/test';
import { Assertion } from '../utils/assertion';
import { LoginPage } from '../pages/loginPage';

test.describe('Login tests', ()=>{

    let ASSERTION: Assertion;
    let loginPage: LoginPage;

    test.beforeEach(async ({page})=>{
        await page.goto('https://account.aldi.us/s/login/');
        ASSERTION = new Assertion();
        loginPage = new LoginPage(page);
    });

    test('Test the login functionality', async ({ page }) => {
        await ASSERTION.shouldBeVisible(loginPage.emailInputField);
        await ASSERTION.shouldBeVisible(loginPage.passwordInputField);
        await ASSERTION.shouldBeVisible(loginPage.logInButton);
    });
});