import { Locator, Page } from "@playwright/test";
import { BasePage } from "../pages/basePage";

export class WebElement{
    constructor(
        public name: string,
        public locator: Locator
    ) {
    }
}