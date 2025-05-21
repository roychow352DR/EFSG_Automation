package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AppHomePage {
    AppiumDriver driver;
    MobileAbstractComponents abs;

    public AppHomePage(AppiumDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver,this);
        abs = new MobileAbstractComponents(driver);
    }

    @FindBy(xpath = "//android.widget.TextView[@text='Open a Live Trading Accounts']")
    WebElement applicationText;

    public boolean buttonValidation()
    {
        abs.waitUtilElementFind(applicationText);
        return applicationText.isDisplayed();
    }
}
