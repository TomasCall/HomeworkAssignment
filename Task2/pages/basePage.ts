import { Locator, Page } from "@playwright/test";

export class BasePage {
    private page: Page;

    constructor(page: Page) {
        this.page = page;
    }

    getElement(selector: string): Locator {
        return this.page.locator(selector);
    }
}