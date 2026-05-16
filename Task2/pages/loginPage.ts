import { Page } from "@playwright/test";
import { BasePage } from "./basePage";
import { WebElement } from "../utils/webElement";

export class LoginPage extends BasePage {
    readonly emailInputField = new WebElement('emailInputField', this.getElement("input[name='email']"));
    readonly passwordInputField = new WebElement('passwordInputField', this.getElement('input[name="passw"]'));
    readonly logInButton = new WebElement('logInButton', this.getElement('button[title="Log In"]'));

    constructor(page: Page) {
        super(page)
    }
}