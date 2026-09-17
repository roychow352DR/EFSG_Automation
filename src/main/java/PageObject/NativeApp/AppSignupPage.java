package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AppSignupPage {
    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;

    public AppSignupPage(AppiumDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.abs = new MobileAbstractComponents(driver);
    }

    @FindBy(xpath = "//android.widget.TextView[@text=\"Have an account? Log In\"]")
    WebElement navigateToLoginAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]//android.view.ViewGroup[3]//android.view.ViewGroup[2]//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.widget.EditText")
    WebElement usernameFieldAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]//android.view.ViewGroup[3]//android.view.ViewGroup[2]//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.EditText")
    WebElement passwordFieldAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]//android.view.ViewGroup[3]//android.view.ViewGroup[2]//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[8]/android.widget.EditText")
    WebElement confirmPasswordFieldAos;

    @FindBy(xpath = "//android.widget.TextView[@text=\"Signup\"]")
    WebElement signupPageTitleAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]//android.view.ViewGroup[3]//android.view.ViewGroup[2]//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[9]/android.view.ViewGroup")
    WebElement countryCodeDropdownAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]//android.view.ViewGroup[3]//android.view.ViewGroup[2]//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[10]/android.widget.EditText")
    WebElement phoneNumberFieldAos;

    //android.widget.FrameLayout[@resource-id="android:id/content"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.widget.ScrollView/android.view.ViewGroup/android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[9]/android.widget.EditText

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.widget.ScrollView/android.view.ViewGroup/android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[9]/android.widget.EditText")
    WebElement emailFieldAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]//android.view.ViewGroup[3]//android.view.ViewGroup[2]//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[11]/android.view.ViewGroup")
    WebElement termsCheckboxAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]//android.view.ViewGroup[3]//android.view.ViewGroup[2]//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[14]/android.view.ViewGroup")
    WebElement receiveInfoCheckboxAos;

    public void fillMandatoryFields() throws InterruptedException {
        enterPassword();
        enterPhoneNumber();
        enterEmail();
        acceptTermsAndConditions();
        acceptReceiveInfo();
    }

    public void navigateToLoginPage() {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        if (isLoginFormOpen()) {
            return;
        }
        TimeoutException lastError = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            for (By locator : List.of(
                    By.xpath("//android.widget.TextView[@text='Have an account? Log In']"),
                    By.xpath("//android.widget.TextView[contains(@text,'Have an account')]"),
                    By.xpath("//*[contains(@text,'Have an account') or contains(@content-desc,'Have an account')]")
            )) {
                try {
                    if (attempt == 1) {
                        abs.tapVisibleRight(locator, 8);
                    } else {
                        abs.tapBottomMost(locator, 8);
                    }
                    if (waitForLoginForm(10)) {
                        return;
                    }
                } catch (TimeoutException e) {
                    lastError = e;
                }
            }
        }
        if (lastError != null) {
            throw lastError;
        }
        throw new TimeoutException("Login page was not visible after tapping Have an account");
    }

    private boolean waitForLoginForm(int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(d -> isLoginFormOpen());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean isLoginFormOpen() {
        if (!driver.findElements(By.xpath("//*[@text='Signup' or @text='Sign up']")).isEmpty()) {
            return false;
        }
        int fields = driver.findElements(By.className("android.widget.EditText")).size();
        return fields >= 2 && !driver.findElements(By.xpath(
                "//*[@text='Login' or @text='Log In' or @content-desc='Login' or @content-desc='Log In']"
        )).isEmpty();
    }

    public void enterTextFieldValue(String value, String textField) throws InterruptedException {

        if (driver instanceof AndroidDriver) {
            switch (textField) {
                case "username" -> {
                    abs.typeWithAndroidKeys((AndroidDriver) driver, usernameFieldAos, value);
                    Thread.sleep(2000);
                    abs.tapEmptySpace(driver);
                    Thread.sleep(2000);
                }
            }
        }
    }

    public boolean getSignupPageTitle() {
        if (driver instanceof AndroidDriver) {
            return signupPageTitleAos.isDisplayed();
        }
        return false;
    }

    public void enterPassword() {
        if (driver instanceof AndroidDriver) {
            passwordFieldAos.sendKeys(abs.userInfo().get("password"));
            confirmPasswordFieldAos.sendKeys(abs.userInfo().get("password"));
        }
    }

    public void enterPhoneNumber() throws InterruptedException {
        if (driver instanceof AndroidDriver) {
//            countryCodeDropdownAos.click();
//            Thread.sleep(2000);
//            for (int i = 0 ; i<=1 ; i++) {
//                abs.swipeUp(driver);
//            }
//            WebElement countryCode = driver.findElement(By.xpath("//android.widget.TextView[@text=\"Canada (+1)\"]"));
//            countryCode.click();
            phoneNumberFieldAos.sendKeys(abs.userInfo().get("phone"));
        }
    }

    public void enterEmail() throws InterruptedException {
        Thread.sleep(2000);
        abs.swipeUp(driver);
        if (driver instanceof AndroidDriver) {
            //abs.swipeUntilElementVisible(driver,emailFieldAos,3);
            emailFieldAos.sendKeys(abs.userInfo().get("email"));
        }
    }

    public void acceptTermsAndConditions() {
        if (driver instanceof AndroidDriver) {
            termsCheckboxAos.click();
        }
    }

    public void acceptReceiveInfo() {
        if (driver instanceof AndroidDriver) {
            receiveInfoCheckboxAos.click();
        }
    }

    public WebElement getErrorMsg(String errorText) {
        return driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + errorText + "\"]"));
    }
}

