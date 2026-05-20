package utils;

import lombok.AllArgsConstructor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@AllArgsConstructor
public class Action {
    private WebDriverWait webDriverWait;

    private static final Logger log = LoggerFactory.getLogger(Action.class);

    public void click(UiElement uiElement) {
        log.info("[ACTION] Clicking on element: {}", uiElement.getName());
        webDriverWait.until(ExpectedConditions.elementToBeClickable(uiElement.getLocator())).click();
        log.info("[ACTION] Successfully clicked on: {}", uiElement.getName());
    }
}
