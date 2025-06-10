package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

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

    @FindBy(xpath = "//XCUIElementTypeOther[@name=\"Agree\"]")
    WebElement agreeButtonIos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id='android:id/content']/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[3]")
    WebElement androidImage;

    @FindBy(xpath = "//android.widget.TextView[@text=\"Login\"]")
    WebElement loginButtonAndroid;

    @FindBy(xpath = "//XCUIElementTypeOther[@name=\"Login\"]")
    WebElement loginButtonIos;

    @FindBy(xpath = "//XCUIElementTypeImage")
    WebElement iosImage;

    private Platform isIOS() {

        return driver.getCapabilities().getPlatformName();

    }

    public void notificationPermission()
    {
      //  abs.waitUtilElementFind(allowNotification);
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
       if (driver instanceof AndroidDriver)
       {
           return driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id='android:id/content']/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[3]"));
       }
       else
       {
           return driver.findElement(By.xpath("//XCUIElementTypeOther[@name=\"Toolbar\"]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[1]/XCUIElementTypeOther/XCUIElementTypeOther[4]"));
       }
    }

    public void swipeScreen() throws InterruptedException {
        int swipeCount = 3;
        abs.waitUtilElementFind(getImage());
        abs.swipeAction(getImage(),"left",swipeCount);
    }

    public void getStartedAction()
    {
        if (driver instanceof AndroidDriver) {
            loginButtonAndroid.click();
        }
        else {
            loginButtonIos.click();
        }
    }

    public AppLoginPage launchToLogin() throws InterruptedException {
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

 //       Thread.sleep(5000);

    }
}
