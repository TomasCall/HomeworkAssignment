import test, { Page } from "@playwright/test";
import { BasePage } from "./basePage";
import { WebElement } from "../utils/webElement";
import { TestLogger } from "../helpers/testLogger";
import { Assertion } from "../utils/assertion";

export class ShoppingPage extends BasePage{
    readonly loadingIndicator = new WebElement('emailInputField', this.getElement("[class='loading']"));
    readonly accountButton = new WebElement('accountButton', this.getElement('button[aria-label ="Account Menu"]'));
    readonly cartButton = new WebElement('accountButton', this.getElement('button[data-testid ="floating-cart-button"]'));

    testLogger: TestLogger;
    check: Assertion;

    constructor(page: Page) {
        super(page);
        this.testLogger = new TestLogger('ShoppingPage');
        this.check = new Assertion();
    }

    waitForShoppingPageToLoad = async(timeout: number): Promise<void> => {
        const stepDescription = `Waiting for shopping page to fully load - waitForShoppingPageToLoad(${timeout})`;
        await test.step(stepDescription, async () => {
            this.testLogger.action(stepDescription);
            if(await this.check.isVisible(this.loadingIndicator)) {
                await this.check.shouldNotBeVisible(this.loadingIndicator);
            }
            this.testLogger.action('Shopping page is fully loaded');
        });
    }
}