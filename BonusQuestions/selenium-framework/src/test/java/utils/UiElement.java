package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openqa.selenium.By;

@AllArgsConstructor
@Getter
public class UiElement {
    private By locator;
    private String name;
}
