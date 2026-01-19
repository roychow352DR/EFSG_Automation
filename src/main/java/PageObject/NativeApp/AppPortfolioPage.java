package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AppPortfolioPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;

    public AppPortfolioPage(AppiumDriver driver){
        this.driver = driver;
        this.abs = new MobileAbstractComponents(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout//android.view.ViewGroup[3]//android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]")
    WebElement applicationButtonAos;

    public void tapButtonOnPortfolioPage(String buttonName) {
        if (buttonName.equals("Open a Live Trading Accounts")) {
            abs.waitUtilElementFind(applicationButtonAos);
            applicationButtonAos.click();
        }
    }
}
