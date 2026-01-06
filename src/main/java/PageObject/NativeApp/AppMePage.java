package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AppMePage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;

    public AppMePage(AppiumDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        abs = new MobileAbstractComponents(driver);
    }

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]//android.widget.ScrollView//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup")
    WebElement tradeAccountLabelAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]//android.widget.ScrollView//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup")
    WebElement logoutButtonAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[1]")
    WebElement confirmButton;

    public boolean getTradeAccountLabel() {
        if (driver instanceof AndroidDriver) {
            abs.waitUtilElementFind(tradeAccountLabelAos);
            return tradeAccountLabelAos.isDisplayed();
        }
        return false;
    }

    public void tapButtonOnMe(String btnName){
        if (driver instanceof AndroidDriver) {
            switch (btnName) {
                case "Logout" -> {
                    abs.swipeUntilElementVisible(driver, logoutButtonAos, 3);
                    logoutButtonAos.click();
                }
                case "Yes" -> {
                    abs.waitUtilElementFind(confirmButton);
                    confirmButton.click();
                }
                default -> System.out.println("Button not found");
            }
        }

    }

}
