import test, { Page } from "@playwright/test";
import { TestLogger } from "../helpers/testLogger";
import { WebElement } from "./webElement";
import { ShoppingPage } from "../pages/shoppingPage";
import { LoginPage } from "../pages/loginPage";

export class Action {
    testLogger: TestLogger;
    page: Page;

    constructor(page: Page) {
        this.testLogger = new TestLogger('Action')
        this.page = page; 
    }

    goToPage = async (url: string): Promise<void> => {
        const stepDescription = `Going to page: '${url} - goToPage('${url}')'`;
        await test.step(stepDescription, async ()=>{
            this.testLogger.check(stepDescription);
            this.page.goto(url);
        });
    }

    clickElement = async (webElement: WebElement): Promise<void> => {
        const stepDescription = `Clicking on '${webElement.name} - clickElement('${webElement.name}')'`;
        await test.step(stepDescription, async ()=>{
            this.testLogger.check(stepDescription);
            await webElement.locator.click();
            this.testLogger.action(`Successfully clicked '${webElement.name}'`);
        });
    }
    
    sendKeys = async (webElement: WebElement, text: string): Promise<void> => {
        const stepDescription = `Filling in: '${webElement.name} - sendKeys('${webElement.name}')'`;
        await test.step(stepDescription, async ()=>{
            this.testLogger.action(stepDescription);
            await webElement.locator.fill(text);
            this.testLogger.action(`Successfully filled in '${webElement.name}'`);
        });
    }

    waitForShoppingPageToLoad = async (timeout: number): Promise<void> => {
        const shoppingPage = new ShoppingPage(this.page);
        await shoppingPage.waitForShoppingPageToLoad(timeout);
    }

    waitForLoginPageToLoad = async (timeout: number): Promise<void> => {
        const shoppingPage = new LoginPage(this.page);
        await shoppingPage.waitForLoginPageToLoad(timeout);
    }
}