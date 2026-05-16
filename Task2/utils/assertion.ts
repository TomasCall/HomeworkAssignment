import test, { expect, Locator, Page } from "@playwright/test";
import { WebElement } from "./webElement";

export class Assertion {
    shouldBeVisible = async (webElement: WebElement): Promise<void> => {
        const stepDescription = `Checking visibility of '${webElement.name} - shouldBeVisible('${webElement.name}')'`;
        await test.step(stepDescription, async ()=>{
            try{
                await expect(webElement.locator).toBeVisible();
            } catch (error) {
                throw error;
            }
        });
    }
}