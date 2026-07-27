package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AppFooter {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;

    public AppFooter(AppiumDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.abs = new MobileAbstractComponents(driver);
    }

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]//android.view.ViewGroup[2]/android.view.View/android.view.View[2]/android.view.ViewGroup")
    WebElement marketFooterBtnAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]//android.view.ViewGroup[2]/android.view.View/android.view.View[3]/android.view.ViewGroup")
    WebElement portfolioFooterBtnAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]//android.view.ViewGroup[2]/android.view.View/android.view.View[4]/android.view.ViewGroup")
    WebElement meFooterBtnAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]//android.view.ViewGroup[2]/android.view.View/android.view.View[1]/android.view.ViewGroup")
    WebElement homeFooterBtnAos;

    public void tapFooterButton(String buttonName) {
        if (driver instanceof AndroidDriver) {
            switch (buttonName) {
                case "Me" -> {
                    abs.waitUtilElementClickable(meFooterBtnAos);
                    meFooterBtnAos.click();
                }
                case "Home" -> {
                    abs.waitUtilElementClickable(homeFooterBtnAos);
                    homeFooterBtnAos.click();
                }
                case "Markets" -> {
                    abs.waitUtilElementClickable(marketFooterBtnAos);
                    marketFooterBtnAos.click();
                }
                case "Portfolio" -> {
                    abs.waitUtilElementClickable(portfolioFooterBtnAos);
                    portfolioFooterBtnAos.click();
                }
                default -> System.out.println("Button not found");
            }
        }
    }
}
