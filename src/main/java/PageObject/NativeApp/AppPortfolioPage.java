package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

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

    @FindBy(className = "android.view.ViewGroup")
    List<WebElement> buttons;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[3]" +
            "/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.widget.TextView")
    WebElement titleAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup")
    List<WebElement> products;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup")
    WebElement checkedIconAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup" +
            "/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]'" +
            "/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup")
    WebElement backButtonAos;

    public void tapButtonOnPortfolioPage(String buttonName) {
        if (buttonName.equals("Open a Live Trading Accounts")) {
            abs.waitUtilElementFind(applicationButtonAos);
            applicationButtonAos.click();
        }
    }

    public void clickButton(String buttonName){
        if (driver instanceof AndroidDriver) {
            driver.findElement(By.xpath("//android.widget.TextView[@text=\""+buttonName+"\"]/parent::android.view.ViewGroup")).click();
        }
    }

    public String getTitleAos(){
        abs.waitUtilElementFind(titleAos);
        return titleAos.getText();
    }

    public String getCheckedProduct(){
        if (driver instanceof AndroidDriver) {
            return driver.findElement(By.xpath("//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup" +
                    "/parent::android.view.ViewGroup/android.widget.TextView")).getText();
        }
        return "No checked product found";
    }

    public void tapBack(){
        if (driver instanceof AndroidDriver) {
            backButtonAos.click();
        }
    }
}
