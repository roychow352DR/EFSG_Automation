package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class AppMarketsPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;

    public static String tradeSymbol;

    public AppMarketsPage(AppiumDriver driver) {
        this.driver = driver;
        this.abs = new MobileAbstractComponents(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout//android.view.ViewGroup[1]//android.widget.FrameLayout//android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup")
    WebElement applicationButtonAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]//android.view.ViewGroup[2]/android.view.View/android.view.View[2]/android.view.ViewGroup")
    WebElement marketFooterBtnAos;

    @FindBy(xpath = "//android.widget.TextView[@text=\"XAUUSD\"]/parent::android.view.ViewGroup")
    WebElement marketXAUUSDAos;



    public void tapButtonOnMarketsPage(String buttonName) {
        if (driver instanceof AndroidDriver) {
            switch (buttonName) {
                case "Open a Live Trading Accounts" -> {
                    abs.waitUntilElementFind(applicationButtonAos);
                    applicationButtonAos.click();
                }
                default -> System.out.println("Button not found");
            }
        }
    }

    public void tapSymbol(String symbol) {
        tradeSymbol = symbol;
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        TimeoutException lastError = null;
        for (By locator : symbolLocators(symbol)) {
            try {
                abs.tapVisible(locator, 15);
                return;
            } catch (TimeoutException e) {
                lastError = e;
            }
        }
        throw lastError != null
                ? lastError
                : new TimeoutException("Symbol was not visible: " + symbol);
    }

    private List<By> symbolLocators(String symbol) {
        return List.of(
                By.xpath("//android.widget.TextView[@text=\"" + symbol + "\"]"),
                By.xpath("//*[@text=\"" + symbol + "\"]"),
                By.xpath("//android.widget.TextView[@text=\"" + symbol + "\"]/parent::android.view.ViewGroup")
        );
    }




}
