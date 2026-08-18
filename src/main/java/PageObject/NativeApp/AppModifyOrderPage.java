package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.time.Duration;
import java.util.List;

public class AppModifyOrderPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    public static String editPrice;
    public static boolean SCROLLED = false;

    public AppModifyOrderPage(AppiumDriver driver) {
        this.driver = driver;
        this.abs = new MobileAbstractComponents(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @FindBy(className = "android.widget.EditText")
    List<WebElement> editTextFieldAos;

    @FindBy(className = "android.widget.TextView")
    List<WebElement> textMessagesAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout" +
            "/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup" +
            "/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]")
    WebElement backBtnAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[15]")
    WebElement closeBtnAos;

    @FindBy(xpath = "(//android.widget.TextView[@text=\"Modify Order\"])[1]")
    WebElement headerAos;


    public String getEditPrice(String direction, String decimal, String priceType, int value) {
        String price = "";
        if (driver instanceof AndroidDriver) {
            if (priceType.equalsIgnoreCase("Stop Limit")) {
                switch (direction) {
                    case "BUY" ->
                            price = Float.toString(Float.parseFloat(editTextFieldAos.getFirst().getText()) + value);
                    case "SELL" ->
                            price = Float.toString(Float.parseFloat(editTextFieldAos.getFirst().getText()) - value);
                }
            } else if (priceType.equalsIgnoreCase("Stop Loss")) {
                switch (direction) {
                    case "BUY" -> price = Float.toString(Float.parseFloat(editTextFieldAos.get(1).getText()) - value);
                    case "SELL" -> price = Float.toString(Float.parseFloat(editTextFieldAos.get(1).getText()) + value);
                }
            } else {
                switch (direction) {
                    case "BUY" ->
                            price = Float.toString(Float.parseFloat(editTextFieldAos.getLast().getText()) + value);
                    case "SELL" ->
                            price = Float.toString(Float.parseFloat(editTextFieldAos.getLast().getText()) - value);
                }
            }
        }
        editPrice = abs.normalizePriceToDecimals(price, decimal);
        return price;
    }

    public void editTextField(String priceType, String direction, String decimal, int value) {
        if (driver instanceof AndroidDriver) {
            switch (priceType) {
                case "Stop Loss" -> {
                    String editStopLossPrice = getEditPrice(direction, decimal, priceType, value);
                    editTextFieldAos.get(1).clear();
                    abs.typeWithAndroidKeys((AndroidDriver) driver, editTextFieldAos.get(1), editStopLossPrice);
                }
                case "Stop Limit" -> {
                    String editStopLimitPrice = getEditPrice(direction, decimal, priceType, value);
                    editTextFieldAos.getFirst().clear();
                    abs.typeWithAndroidKeys((AndroidDriver) driver, editTextFieldAos.getFirst(), editStopLimitPrice);
                }
                case "Take Profit" -> {
                    // WebElement takeProfitField = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,'Take Profit')]"));
                    //  abs.swipeUntilElementVisible(driver,takeProfitField,5);
                    String editTakeProfitPrice = getEditPrice(direction, decimal, priceType, value);
                    editTextFieldAos.getLast().clear();
                    abs.typeWithAndroidKeys((AndroidDriver) driver, editTextFieldAos.getLast(), editTakeProfitPrice);
                }
            }
        }
    }

    public void tapsButton(String buttonName) {
        if (driver instanceof AndroidDriver) {
            driver.findElement(By.xpath("(//android.widget.TextView[@text=\"" + buttonName + "\"])[2]/parent::android.view.ViewGroup")).click();
        }
    }

    public void scrollDown() {
        SCROLLED = true;
        abs.swipeUpUntilEnd(driver);
    }

    public boolean getTextMessage(String messageContent) {
        if (driver instanceof AndroidDriver) {
            abs.waitUntilElementFind(textMessagesAos.getFirst());
            for (WebElement ele : textMessagesAos) {
                if (ele.getText().equalsIgnoreCase(messageContent)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void tapBack() {
        if (driver instanceof AndroidDriver) {
            backBtnAos.click();
        }
    }

    public void tapButtonOnDialogue(String btnName) {
        if (driver instanceof AndroidDriver) {
            switch (btnName) {
                case "x" -> {
                    abs.waitUntilElementClickable(closeBtnAos);
                    closeBtnAos.click();
                }
            }
        }
    }
    public boolean getHeader(){
        if (driver instanceof AndroidDriver) {
            abs.waitUntilElementFind(headerAos);
            return headerAos.isDisplayed();
        }
        return false;
    }

}
