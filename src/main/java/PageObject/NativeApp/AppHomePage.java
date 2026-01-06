package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AppHomePage {
    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;

    public AppHomePage(AppiumDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        abs = new MobileAbstractComponents(driver);
    }

    @FindBy(xpath = "//android.widget.TextView[@text='Open a Live Trading Accounts']")
    WebElement applicationButtonAos;

    @FindBy(xpath = "(//XCUIElementTypeOther[@name='Open a Live Trading Accounts'])[2]")
    WebElement applicationButtonIos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.View/android.view.View[1]/android.view.ViewGroup")
    WebElement homeButtonAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.View/android.view.View[1]/android.view.ViewGroup")
    WebElement homeButtonIos;

    @FindBy(xpath = "//android.widget.TextView[@text=\"Sign Up / Login\"]")
    WebElement signUpButtonAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.View/android.view.View[4]/android.view.ViewGroup")
    WebElement meButtonAos;

    public boolean buttonValidation() {
        if (driver instanceof AndroidDriver) {
            abs.waitUtilElementFind(applicationButtonAos);
            return applicationButtonAos.isDisplayed();
        } else {
            abs.waitUtilElementFind(applicationButtonIos);
            return applicationButtonIos.isDisplayed();
        }
    }

    public boolean buttonValidation(String buttonName) {
        if (driver instanceof AndroidDriver) {
            return switch (buttonName) {
                case "home" -> homeButtonAos.isDisplayed();
                case "Open a Live Trading Accounts" -> applicationButtonAos.isDisplayed();
                case "Sign Up / Login" -> signUpButtonAos.isDisplayed();
                default -> false;
            };
        } else if (driver instanceof IOSDriver) {
            return switch (buttonName) {
                case "home" -> homeButtonIos.isDisplayed();
                case "Open a Live Trading Accounts" -> applicationButtonIos.isDisplayed();
                default -> false;
            };
        } else {
            return false;
        }
    }

    public boolean bottomButtonIsTapped() {
        if (driver instanceof AndroidDriver) {
            return homeButtonAos.isEnabled();
        } else {
            applicationButtonIos.click();
            return true;
        }
    }

    public void navigateToSignupPage() throws InterruptedException {
        if (driver instanceof AndroidDriver) {
            signUpButtonAos.click();
        } else {
            applicationButtonIos.click();
        }
        Thread.sleep(3000);
    }

    public void tapFooterButton(String buttonName) {
        if (driver instanceof AndroidDriver) {
            switch (buttonName) {
                case "Me" -> meButtonAos.click();
                case "Home" -> applicationButtonAos.click();
                case "Markets" -> applicationButtonAos.click();
                case "Portfolio" -> applicationButtonAos.click();
                default -> System.out.println("Button not found");
            }
        }
    }

    public String getButtonText(){
        abs.waitUtilElementFind(signUpButtonAos);
        return signUpButtonAos.getText();
    }
}
