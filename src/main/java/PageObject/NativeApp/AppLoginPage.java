package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AppLoginPage {
    AppiumDriver driver;
    BiometricsPage biometricsPage;
    MobileAbstractComponents abs;

    public AppLoginPage(AppiumDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver,this);
        abs = new MobileAbstractComponents(driver);
    }

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[5]")
    WebElement loginButtonAndroid;

    @FindBy(xpath = "(//XCUIElementTypeOther[@name=\"Login\"])[10]")
    WebElement loginButtonIos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.widget.EditText")
    WebElement emailFieldAndroid;

    @FindBy(xpath = "//XCUIElementTypeTextField")
    WebElement emailFieldIos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.EditText")
    WebElement passwordFieldAndroid;

    @FindBy(xpath = "//XCUIElementTypeSecureTextField")
    WebElement passwordFieldIos;

    @FindBy(xpath = "(//android.widget.TextView[@text=\"Login\"])[2]")
    WebElement loginTitleAndroid;

    @FindBy(xpath = "//XCUIElementTypeStaticText[@name='Login']")
    WebElement loginTitleIos;

    public BiometricsPage clickLogin() {
        if (driver instanceof AndroidDriver) {
            loginButtonAndroid.click();
        }
        else {
            loginButtonIos.click();
        }
        biometricsPage = new BiometricsPage(driver);
        return biometricsPage;
    }

    public void fillCredential(String email,String password) throws InterruptedException {
        if (driver instanceof AndroidDriver) {
            emailFieldAndroid.sendKeys(email);
            passwordFieldAndroid.sendKeys(password);
        }
        else {
            // avoid typo
            for (char c : email.toCharArray()) {
                emailFieldIos.sendKeys(String.valueOf(c));
               //Thread.sleep(1);
            }
            passwordFieldIos.sendKeys(password);
            passwordFieldIos.sendKeys(Keys.RETURN);
        }
    }

    public boolean loginPageValidation()
    {
        if (driver instanceof AndroidDriver) {
            abs.waitUtilElementFind(loginTitleAndroid);
            return loginTitleAndroid.isDisplayed();
        }
        else {
            abs.waitUtilElementFind(loginTitleIos);
            return loginTitleIos.isDisplayed();
        }
    }


}
