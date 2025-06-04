package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WelcomePage {
    AppiumDriver driver;
    public MobileAbstractComponents abs;
    public AppLoginPage loginPage;
    public WelcomePage(AppiumDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        abs = new MobileAbstractComponents(driver);
    }

    @FindBy(id = "com.android.permissioncontroller:id/permission_allow_button")
    WebElement allowNotification;

    @FindBy(xpath = "//android.widget.TextView[@text=\"Agree\"]")
    WebElement agreeButtonAndroid;

    @iOSXCUITFindBy(accessibility="Agree")
    WebElement agreeButtonIos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id='android:id/content']/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[3]")
    WebElement image;

    @FindBy(xpath = "//android.widget.TextView[@text=\"Login\"]")
    WebElement loginButton;

    private void isIOS() {

        System.out.println(driver.getCapabilities().getPlatformName());

    }

    public void notificationPermission()
    {

        allowNotification.click();
    }

    public void agreeTerms()
    {
        if (driver instanceof AndroidDriver) {
            agreeButtonAndroid.click();
        }
        else {
            agreeButtonIos.click();
        }
    }

    public WebElement getImage()
    {
        return driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id='android:id/content']/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[3]"));
    }

    public void swipeScreen() throws InterruptedException {
        int swipeCount = 3;
        abs.swipeAction(getImage(),"left",swipeCount);
    }

    public void getStartedAction()
    {
        loginButton.click();
    }

    public AppLoginPage launchToLogin() throws InterruptedException {
        notificationPermission();
        agreeTerms();
        swipeScreen();
        getStartedAction();
        return new AppLoginPage(driver);
    }

    public void landToMe() throws InterruptedException {
        String deepLinkURL = "eunify.eiehk.uat://app/tabDirectory?screen=Tab_Me";

        //driver.executeScript("mobile: terminateApp", ImmutableMap.of("bundleId", "com.efsg.eiehktrading.ios.sit"));
//
//        List args = new ArrayList();
//        args.add("-U");
//        args.add(deepLinkURL);
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("bundleId", "com.efsg.eiehktrading.ios.sit");
//        params.put("arguments", args);
//
//        driver.executeScript("mobile: launchApp", params);

        Thread.sleep(5000);

    }
}
