package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class AppFooter {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;

    public AppFooter(AppiumDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.abs = new MobileAbstractComponents(driver);
    }

    public void tapFooterButton(String buttonName) {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        for (By locator : footerLocators(buttonName)) {
            try {
                abs.tapBottomMost(locator, 8);
                return;
            } catch (TimeoutException ignored) {
            }
        }
        tapFooterByIndex(buttonName);
    }

    private List<By> footerLocators(String buttonName) {
        return List.of(
                By.xpath("//android.widget.TextView[@text='" + buttonName + "']"),
                By.xpath("//*[@text='" + buttonName + "']"),
                By.xpath("//android.widget.TextView[@text='" + buttonName + "']/parent::android.view.ViewGroup"),
                positionalFooterLocator(buttonName)
        );
    }

    private By positionalFooterLocator(String buttonName) {
        int index = switch (buttonName) {
            case "Home" -> 1;
            case "Markets" -> 2;
            case "Portfolio" -> 3;
            case "Me" -> 4;
            default -> 0;
        };
        return By.xpath(
                "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]"
                        + "//android.view.ViewGroup[2]/android.view.View/android.view.View[" + index + "]/android.view.ViewGroup"
        );
    }

    private void tapFooterByIndex(String buttonName) {
        int index = switch (buttonName) {
            case "Home" -> 1;
            case "Markets" -> 2;
            case "Portfolio" -> 3;
            case "Me" -> 4;
            default -> 0;
        };
        if (index == 0) {
            throw new TimeoutException("Footer button was not visible: " + buttonName);
        }
        Dimension window = driver.manage().window().getSize();
        int x = window.getWidth() * (index * 2 - 1) / 8;
        int y = (int) (window.getHeight() * 0.96);
        abs.tapAt(x, y);
    }
}
