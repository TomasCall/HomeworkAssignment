import test, { Page } from "@playwright/test";
import { BasePage } from "./basePage";
import { WebElement } from "../utils/webElement";
import { TestLogger } from "../helpers/testLogger";
import { Assertion } from "../utils/assertion";

export class LoginPage extends BasePage {
    testLogger: TestLogger;
    check: Assertion;

    readonly emailInputField = new WebElement('emailInputField', this.getElement("input[name='email']"));
    readonly passwordInputField = new WebElement('passwordInputField', this.getElement('input[name="passw"]'));
    readonly logInButton = new WebElement('logInButton', this.getElement('button[title="Log In"]'));
    readonly loadingIndicator = new WebElement('logInButton', this.getElement('[class="loading"]]'));

    constructor(page: Page) {
        super(page);
        this.testLogger = new TestLogger('ShoppingPage');
        this.check = new Assertion();
    }

    waitLoginPageToLoad = async(timeout: number): Promise<void> => {
        const stepDescription = `Waiting for login page to fully load - waitForShoppingPageToLoad(${timeout})`;
        await test.step(stepDescription, async () => {
            this.testLogger.action(stepDescription);
            if(await this.check.isVisible(this.loadingIndicator)) {
                await this.check.shouldNotBeVisible(this.loadingIndicator);
            }
            this.testLogger.action('Login page is fully loaded');
        });
    }
}