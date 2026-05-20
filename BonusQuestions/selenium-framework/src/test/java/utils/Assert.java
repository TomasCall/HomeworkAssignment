package utils;

import lombok.AllArgsConstructor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class Assert {
    private WebDriverWait webDriverWait;
    private static final Logger log = LoggerFactory.getLogger(Assert.class);
    public void shouldBeVisible(UiElement uiElement) {
        log.info("[CHECK] Checking visibility of: {}", uiElement.getName());
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(uiElement.getLocator()));
        log.info("[CHECK] {} is visible", uiElement.getName());
    }

    public void shouldHaveText(UiElement uiElement, String expectedText) {
        log.info("[CHECK] {} should have text: {}", uiElement.getName(), expectedText);
        WebElement element = webDriverWait.until(
                ExpectedConditions.visibilityOfElementLocated(uiElement.getLocator())
        );

        String actualText = element.getText();
        if (!actualText.contains(expectedText)) {
            throw new AssertionError("Expected text: " + expectedText + " but found: " + actualText);
        }
        log.info("[CHECK] {} element has text: {}", uiElement.getName(), expectedText);
    }
}
