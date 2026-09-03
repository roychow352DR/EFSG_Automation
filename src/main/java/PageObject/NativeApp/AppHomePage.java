package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

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

    @FindBy(xpath = "//android.widget.TextView[@text=\"Markets\"]")
    WebElement marketsButtonAos;

    public boolean buttonValidation() {
        if (driver instanceof AndroidDriver) {
            abs.waitUntilElementFind(applicationButtonAos);
            return applicationButtonAos.isDisplayed();
        } else {
            abs.waitUntilElementFind(applicationButtonIos);
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
            abs.waitUntilElementVisible(By.xpath(
                    "//*[@text='Home' or @text='Sign Up / Login' or @text='Markets']"));
            return true;
        }
        applicationButtonIos.click();
        return true;
    }

    public void navigateToSignupPage() throws InterruptedException {
        if (!(driver instanceof AndroidDriver)) {
            applicationButtonIos.click();
            return;
        }
        if (isSignupOrLoginOpen()) {
            return;
        }
        TimeoutException lastError = null;
        for (By locator : List.of(
                By.xpath("//android.widget.TextView[@text='Sign Up / Login']"),
                By.xpath("//*[@text='Sign Up / Login']"),
                By.xpath("//android.widget.TextView[@text='Sign Up / Login']/parent::android.view.ViewGroup")
        )) {
            try {
                abs.tapBottomMost(locator, 10);
                waitForSignupOrLogin();
                return;
            } catch (TimeoutException e) {
                lastError = e;
            }
        }
        try {
            new AppFooter(driver).tapFooterButton("Me");
            abs.tapBottomMost(By.xpath("//*[@text='Sign Up / Login']"), 8);
            waitForSignupOrLogin();
            return;
        } catch (TimeoutException e) {
            lastError = e;
        }
        try {
            logoutLeftoverSessionFromMe();
            abs.tapBottomMost(By.xpath("//*[@text='Sign Up / Login']"), 10);
            waitForSignupOrLogin();
        } catch (TimeoutException e) {
            throw lastError != null ? lastError : e;
        }
    }

    private void logoutLeftoverSessionFromMe() {
        if (!driver.findElements(By.xpath("//*[@text='Sign Up / Login']")).isEmpty()) {
            return;
        }
        By logout = By.xpath("//*[@text='Logout' or @text='Log Out' or @text='Log out']");
        try {
            abs.tapVisible(logout, 5);
        } catch (TimeoutException e) {
            abs.swipeUp(driver);
            try {
                abs.tapVisible(logout, 5);
            } catch (TimeoutException ignored) {
                return;
            }
        }
        try {
            abs.tapVisible(By.xpath("//*[@text='Yes' or @text='Confirm']"), 8);
        } catch (TimeoutException ignored) {
        }
    }

    private void waitForSignupOrLogin() {
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(d -> isSignupOrLoginOpen());
    }

    private boolean isSignupOrLoginOpen() {
        if (!driver.findElements(By.xpath("//*[@text='Signup']")).isEmpty()
                || !driver.findElements(By.xpath("//*[@text='Have an account? Log In']")).isEmpty()) {
            return true;
        }
        return driver.findElements(By.className("android.widget.EditText")).size() >= 2
                && !driver.findElements(By.xpath("//*[@text='Login' or @text='Log In']")).isEmpty();
    }

    public void tapFooterButton(String buttonName) {
        if (driver instanceof AndroidDriver) {
            switch (buttonName) {
                case "Me" -> meButtonAos.click();
                case "Home" -> applicationButtonAos.click();
                case "Markets" -> marketsButtonAos.click();
                case "Portfolio" -> applicationButtonAos.click();
                default -> System.out.println("Button not found");
            }
        }
    }

    public String getButtonText(){
        abs.waitUntilElementFind(signUpButtonAos);
        return signUpButtonAos.getText();
    }
}
