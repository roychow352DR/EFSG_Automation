package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;

public class AppSettingPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    public static boolean isTradeConfirmNeeded = true;

    public AppSettingPage(AppiumDriver driver) {
        this.driver = driver;
        abs = new MobileAbstractComponents(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[9]/android.view.ViewGroup/android.widget.Switch")
    WebElement tradeConfirmationToggleAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/" +
            "android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/" +
            "android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup")
    WebElement backButtonAos;

    public void tradeSettingsToggleOff() {
        if (driver instanceof AndroidDriver) {
            abs.waitUntilElementClickable(tradeConfirmationToggleAos);
            if (getToggleStatus()) {
                tradeConfirmationToggleAos.click();
                tabButtonByText("Confirm");
            }
            else {
                System.out.println("Trade Confirmation has already been disabled");
                isTradeConfirmNeeded = false;
            }
        }
    }

    public void tradeSettingsToggleOn() {
        if (driver instanceof AndroidDriver) {
            abs.waitUntilElementClickable(tradeConfirmationToggleAos);
            if (!getToggleStatus()) {
                tradeConfirmationToggleAos.click();
                isTradeConfirmNeeded = true;
            }
            else {
                System.out.println("Trade Confirmation has already been enabled");
            }
        }
    }

    public void tabButtonByText(String label) {
        if (driver instanceof AndroidDriver) {
            WebElement button = driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + label + "\"]/parent::android.view.ViewGroup"));
            abs.waitUntilElementClickable(button);
            button.click();
            isTradeConfirmNeeded = false;
        }
    }

    public void tabBack() {
        if (driver instanceof AndroidDriver) {
            abs.waitUntilElementClickable(backButtonAos);
            backButtonAos.click();
        }
    }

    public boolean getToggleStatus() {
        //return Boolean.parseBoolean(((RemoteWebElement) stopLossSwitchAos).getDomAttribute("checked"));
        return Boolean.parseBoolean(tradeConfirmationToggleAos.getDomAttribute("checked"));
    }

}
