package utils;

import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Getter
public class BaseTest {
    private WebDriver webDriver;
    int defaultWaitTime = 5;
    private WebDriverWait webDriverWait;

    public void navigateToUrl(String url) {
        webDriver = new ChromeDriver();
        webDriverWait =  new WebDriverWait(webDriver, Duration.ofSeconds(defaultWaitTime));
        webDriver.get(url);
    }

    public void closeBrowser() {
        webDriver.close();
    }
}
