package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
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
    WebElement agreeButton;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id='android:id/content']/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[3]")
    WebElement image;

    @FindBy(xpath = "//android.widget.TextView[@text=\"Login\"]")
    WebElement loginButton;


    public void notificationPermission()
    {
        allowNotification.click();
    }

    public void agreeTerms()
    {
        agreeButton.click();
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
}
