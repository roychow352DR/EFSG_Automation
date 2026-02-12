package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class AppTradeView {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    public static String stopLossPrice;
    public static String takeProfitPrice;
    public static String selectedDirection;
    public static String lotSize;
    public static String executedPrice;

    public AppTradeView(AppiumDriver driver) {
        this.driver = driver;
        abs = new MobileAbstractComponents(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//android.widget.TextView[@text=\"BUY\"]/parent::android.view.ViewGroup")
    WebElement buyButtonAos;

    @FindBy(xpath = "//android.widget.TextView[@text=\"SELL\"]/parent::android.view.ViewGroup")
    WebElement sellButtonAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.widget.TextView[5]")
    WebElement stopLossPriceAos;

    @FindBy(xpath = "//android.widget.Switch")
    WebElement stopLossSwitchAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[10]/android.widget.EditText")
    WebElement stopLossTextFieldAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[14]/android.widget.EditText")
    WebElement takeProfitTextFieldAos;

    @FindBy(className = "android.widget.TextView")
    WebElement confirmationTextAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[12]/android.view.ViewGroup")
    WebElement closeButtonAos;

    @FindBy(className = "android.widget.EditText")
    WebElement lotSizeTextFieldAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.widget.TextView")
    List<WebElement> rowsOnPositionTab;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup")
    WebElement detailsButton;

    public void selectDirection(String direction) {
        selectedDirection = direction;
        if (driver instanceof AndroidDriver) {
            switch (direction) {
                case "BUY" -> buyButtonAos.click();
                case "SELL" -> sellButtonAos.click();
            }
        }
    }

    public String getStopLossValue() {
        if (driver instanceof AndroidDriver) {
            return stopLossPriceAos.getText();
        }
        return null;
    }

    public void switchProfitStopLoss() throws InterruptedException {
        if (driver instanceof AndroidDriver) {
            Thread.sleep(2000);
            stopLossSwitchAos.click();
        }
    }

    public String getStopLossPrice(String direction) {
        String price = "";
        WebElement text;
        selectedDirection = direction;
        switch (selectedDirection) {
            case "BUY" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Stop Loss (≤\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≤")[1].trim().split("\\)")[0]) - 100);
            }
            case "SELL" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Stop Loss (≥\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≥")[1].trim().split("\\)")[0]) + 100);
            }
        }
        stopLossPrice = price;
        return price;
    }

    public String getTakeProfitPrice(String direction) {
        String price = "";
        WebElement text;
        selectedDirection = direction;
        switch (selectedDirection) {
            case "BUY" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Take Profit (≥\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≥")[1].trim().split("\\)")[0]) + 100);
            }
            case "SELL" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Take Profit (≤\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≤")[1].trim().split("\\)")[0]) - 100);
            }
        }
        takeProfitPrice = price;
        return price;
    }

    public void fillInTextField(String textFieldName, String direction) {
        if (driver instanceof AndroidDriver) {
            switch (textFieldName) {
                case "Stop Loss" -> stopLossTextFieldAos.sendKeys(getStopLossPrice(direction));
                case "Take Profit" -> takeProfitTextFieldAos.sendKeys(getTakeProfitPrice(direction));
                case "Lot Size" -> lotSizeTextFieldAos.sendKeys("0.45");
            }
        }
    }

    public void tapsButton(String buttonName) {
        if (driver instanceof AndroidDriver) {
            driver.findElement(By.xpath("(//android.widget.TextView[@text=\"" + buttonName + "\"])[2]/parent::android.view.ViewGroup")).click();
        }
    }

    public String getConfirmationValue(String value) {
        List<WebElement> text = driver.findElements(By.className("android.widget.TextView"));
        for (int i = 0; i < text.size(); i++) {
            if (text.get(i).getText().equalsIgnoreCase(value)) {
                if (value.equalsIgnoreCase("Volume")) {
                    return text.get(i + 1).getText().split("Lots")[0].trim();
                }
                return text.get(i + 1).getText();
            }
        }
        return null;
    }

    public String getValidationValue(String label){
        return switch (label) {
            case "Stop Loss Price" -> String.format("%.2f",Double.parseDouble(stopLossPrice));
            case "Take Profit Price" -> String.format("%.2f",Double.parseDouble(takeProfitPrice));
            case "Direction" -> selectedDirection;
            case "Volume" -> lotSize;
            default -> null;
        };
    }

    public void closeConfirmationPopUp(){
        if (driver instanceof AndroidDriver) {
            closeButtonAos.click();
        }
    }

    public String getLotSize(){
        return driver.findElement(By.className("android.widget.EditText")).getText();
    }

    public void getDirection(){
        List<WebElement> direction = driver.findElements(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup" +
                "/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/" +
                "android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup"));
        for (WebElement element : direction) {
            if (element.isSelected()) {
            }
        }
    }

    public void fillValueIntoTextField(String textFieldName,String value) throws InterruptedException {
        if (driver instanceof AndroidDriver) {
            switch (textFieldName) {
                case "Lot Size" -> {
                    driver.findElement(By.xpath("//android.widget.TextView[@text=\""+value+"\"]/parent::android.view.ViewGroup")).click();
                    Thread.sleep(1000);
                    lotSize = getLotSize();
                }
            }
        }
    }

    public boolean getPositionDetail(String positionDetail){
        for (WebElement element : rowsOnPositionTab) {
            if (element.getText().equalsIgnoreCase(positionDetail)){
                return true;
            }
        }

        return false;
    }

    public void getExecutedPrice(){
        executedPrice = getConfirmationValue("Price");
    }

    public void tabCtaButton(String buttonName) {
        if (driver instanceof AndroidDriver) {
            switch (buttonName) {
                case "Deposit detail" -> detailsButton.click();
            }
        }
    }


}
