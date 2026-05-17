import test, { expect, Locator, Page } from "@playwright/test";
import { WebElement } from "./webElement";
import { TestLogger } from "../helpers/testLogger";

export class Assertion {
    testLogger: TestLogger;

    constructor() {
        this.testLogger = new TestLogger('Assertion');
    }

    shouldBeVisible = async (webElement: WebElement): Promise<void> => {
        const stepDescription = `Checking visibility of '${webElement.name} - shouldBeVisible('${webElement.name}')'`;
        await test.step(stepDescription, async ()=>{
            this.testLogger.check(stepDescription);
            try{
                await expect(webElement.locator).toBeVisible();
                this.testLogger.check(`'${webElement.name}' is visible`);
            } catch (error) {
                this.testLogger.errorLog(`'${webElement.name}' is not visible`);
                throw error;
            }
        });
    }

    shouldNotBeVisible = async (webElement: WebElement, timeout: number = 5000): Promise<void> => {
        const stepDescription = `Checking invisibility of '${webElement.name} - shouldNotBeVisible('${webElement.name}')'`;
        await test.step(stepDescription, async ()=>{
            this.testLogger.check(stepDescription);
            try{
                await expect(webElement.locator).toBeHidden({timeout});
                this.testLogger.check(`'${webElement.name}' is not visible`);
            } catch (error) {
                this.testLogger.errorLog(`'${webElement.name}' is visible`);
                throw error;
            }
        });
    }

    isVisible = async (webElement: WebElement): Promise<boolean> => {
        const stepDescription = `Checking visibility of '${webElement.name} - isVisible('${webElement.name}')'`;
        return await test.step(stepDescription, async ()=>{
            this.testLogger.check(stepDescription);
            return webElement.locator.isVisible();
        });
    }

    shouldContainText = async(webElement: WebElement, text: string): Promise<void> => {
        const stepDescription = `Checking if element '${webElement.name} contains text: '${webElement.name} - shouldContainText(${webElement.name}, ${text})''`;
        await test.step(stepDescription, async ()=>{
            this.testLogger.check(stepDescription);
            try{
                await expect(webElement.locator).toHaveText(text);
                this.testLogger.check(`'${webElement.name}' is not visible`);
            } catch (error) {
                const elementText = await webElement.locator.textContent();
                this.testLogger.errorLog(`Text mismatch: expected '${text}', but found '${elementText}'`);
                throw error;
            }
        });
    }
}