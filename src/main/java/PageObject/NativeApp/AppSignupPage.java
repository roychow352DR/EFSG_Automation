package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AppSignupPage {
    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;

    public AppSignupPage(AppiumDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver,this);
        this.abs = new MobileAbstractComponents(driver);
    }

    @FindBy(xpath = "//android.widget.TextView[@text=\"Have an account? Log In\"]")
    WebElement navigateToLoginAos;


    public void navigateToLoginPage() {
        if (driver instanceof AndroidDriver) {
            navigateToLoginAos.click();
        }
    }
}
