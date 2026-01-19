package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Arrays;

public class AppTradeView {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;

    public AppTradeView(AppiumDriver driver) {
        this.driver = driver;
        abs = new MobileAbstractComponents(driver);
        PageFactory.initElements(driver,this);
    }

    @FindBy(xpath = "//android.widget.TextView[@text=\"BUY\"]/parent::android.view.ViewGroup")
    WebElement buyButtonAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.widget.TextView[5]")
    WebElement stopLossPriceAos;

    @FindBy(xpath = "//android.widget.Switch")
    WebElement stopLossSwitchAos;

    public void selectDirection(String direction) {
        if (driver instanceof AndroidDriver) {
            switch (direction) {
                case "BUY" -> buyButtonAos.click();
            }
        }
    }

    public String getStopLossValue() {
        if (driver instanceof AndroidDriver) {
            return stopLossPriceAos.getText();
        }
        return null;
    }

    public void switchProfitStopLoss(){
        if (driver instanceof AndroidDriver) {
            stopLossSwitchAos.click();
        }
    }

    public String getStopLossPrice(){
        String price;
        WebElement text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Stop Loss (≤\")]"));
        price = text.getText().split("≤")[1].trim().split("\\)")[0];
        return price;
    }

    public String getTakeProfitPrice(){
        String price;
        WebElement text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Take Profit (≥\")]"));
        price = text.getText().split("≤")[1].trim().split("\\)")[0];
        return price;
    }


}
