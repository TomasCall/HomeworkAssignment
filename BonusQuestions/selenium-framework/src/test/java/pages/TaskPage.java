package pages;

import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.UiElement;

import java.time.Duration;

@Getter
public class TaskPage {
    private static final Logger log = LoggerFactory.getLogger(TaskPage.class);
    private WebDriver webDriver;
    int defaultWaitTime = 5;
    public UiElement taskTitle = new UiElement(By.xpath("//span[@data-testid='taskTitle']"), "taskPage.taskTitle");
    public UiElement deleteButton = new UiElement(By.xpath("//button[@data-testid='deleteButton']"), "taskPage.deleteButton");
    public UiElement deleteDialog = new UiElement(By.xpath("//div[@data-testid='deleteDialog']"), "taskPage.deleteDialog");
    public UiElement yesButton = new UiElement(By.xpath("//button[@data-testid='yesButton']"), "taskPage.deleteDialog.yesButton");
    public UiElement noButton = new UiElement(By.xpath("//button[@data-testid='noButton']"), "taskPage.deleteDialog.noButton");
    public UiElement successToastMessage = new UiElement(By.xpath("//div[@data-testid='successToastMessage']"), "taskPage.successToastMessage");


    public TaskPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void waitForTaskPageToLoad() {
        log.info("[ACTION] Wait for task page to load");
        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(defaultWaitTime));
        wait.until(ExpectedConditions.visibilityOfElementLocated(taskTitle.getLocator()));
        log.info("[ACTION] Task page is fully loaded");
    }
}
