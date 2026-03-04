package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

public class AppTradeView {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    public static String stopLossPrice;
    public static String stopOrderPrice;
    public static String stopOrderType;
    public static String takeProfitPrice;
    public static String selectedDirection;
    public static String lotSize;
    public static String executedPrice;
    public static String validity;
    public static boolean SCROLLED = false;

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
    WebElement marketStopLossTextFieldAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[14]/android.widget.EditText")
    WebElement marketTakeProfitTextFieldAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[11]/android.widget.EditText")
    WebElement stopLimitStopLossTextFieldAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[15]/android.widget.EditText")
    WebElement stopLimitTakeProfitTextFieldAos;

    @FindBy(className = "android.widget.TextView")
    WebElement confirmationTextAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[12]/android.view.ViewGroup")
    WebElement closeButtonAos;

    @FindBy(className = "android.widget.EditText")
    List<WebElement> editTextFieldAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.widget.TextView")
    List<WebElement> rowsOnPositionTabAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.widget.TextView")
    List<WebElement> rowsOnPendingOrdersTabAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup")
    WebElement detailsButton;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup")
    WebElement orderTypeDropdownBtn;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.widget.ScrollView/android.view.ViewGroup")
    List<WebElement> orderTypeOptions;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[1]")
    WebElement crossButtonAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[14]")
    WebElement cancelOrderButtonAos;

    @FindBy(xpath = "(//android.widget.TextView[@text=\"Close Position\"])[2]/parent::android.view.ViewGroup")
    WebElement closePositionAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[11]")
    WebElement confirmClosePositionBtnAos;


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

    public String getStopOrderPrice(String direction, String stopOrderType) {
        String price = "";
        WebElement text;
        selectedDirection = direction;
        if (stopOrderType.contains("Stop")) {
            switch (selectedDirection) {
                case "BUY" -> {
                    text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Price (≥\")]"));
                    price = Float.toString(Float.parseFloat(text.getText().split("≥")[1].trim().split("\\)")[0]) + 100);
                }
                case "SELL" -> {
                    text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Price (≤\")]"));
                    price = Float.toString(Float.parseFloat(text.getText().split("≤")[1].trim().split("\\)")[0]) - 100);
                }
            }
        } else if (stopOrderType.contains("Limit")) {
            switch (selectedDirection) {
                case "BUY" -> {
                    text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Price (≤\")]"));
                    price = Float.toString(Float.parseFloat(text.getText().split("≤")[1].trim().split("\\)")[0]) - 100);
                }
                case "SELL" -> {
                    text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Price (≥\")]"));
                    price = Float.toString(Float.parseFloat(text.getText().split("≥")[1].trim().split("\\)")[0]) + 100);
                }
            }
        }
        stopOrderPrice = price;
        return price;
    }

    public void fillInTextField(String textFieldName, String direction) {
        if (driver instanceof AndroidDriver) {
            switch (textFieldName) {
                case "Stop Loss" -> {
                    if (!SCROLLED) {
                        marketStopLossTextFieldAos.sendKeys(getStopLossPrice(direction));
                    } else {
                        stopLimitStopLossTextFieldAos.sendKeys(getStopLossPrice(direction));
                    }
                }
                case "Take Profit" -> {
                    if (!SCROLLED) {
                        marketTakeProfitTextFieldAos.sendKeys(getTakeProfitPrice(direction));
                    } else {
                        stopLimitTakeProfitTextFieldAos.sendKeys(getTakeProfitPrice(direction));
                    }
                }
                case "Lot Size" -> editTextFieldAos.getFirst().sendKeys("0.45");
                case "Price" -> {
                    editTextFieldAos.get(1).clear();
                    // editTextFieldAos.get(1).sendKeys(getStopOrderPrice(direction));
                    abs.typeWithAndroidKeys((AndroidDriver) driver, editTextFieldAos.get(1), getStopOrderPrice(direction, stopOrderType));
                }
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

    public String getValidationValue(String label) {
        return switch (label) {
            case "Stop Loss Price" -> String.format("%.2f", Double.parseDouble(stopLossPrice));
            case "Take Profit Price" -> String.format("%.2f", Double.parseDouble(takeProfitPrice));
            case "Direction" -> selectedDirection;
            case "Volume" -> lotSize;
            case "Stop Order Price" -> stopOrderPrice;
            case "Validity" -> validity;
            default -> null;
        };
    }

    public void closeConfirmationPopUp() {
        if (driver instanceof AndroidDriver) {
            closeButtonAos.click();
        }
    }

    public String getLotSize() {
        // return driver.findElement(By.className("android.widget.EditText")).getText();
        return driver.findElements(By.className("android.widget.EditText")).getFirst().getText();
    }

    public void getDirection() {
        List<WebElement> direction = driver.findElements(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup" +
                "/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/" +
                "android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup"));
        for (WebElement element : direction) {
            if (element.isSelected()) {
            }
        }
    }

    public void fillValueIntoTextField(String textFieldName, String value) throws InterruptedException {
        if (driver instanceof AndroidDriver) {
            switch (textFieldName) {
                case "Lot Size" -> {
                    driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + value + "\"]/parent::android.view.ViewGroup")).click();
                    Thread.sleep(1000);
                    lotSize = getLotSize();
                }
            }
        }
    }

    public boolean getPositionDetail(String positionDetail) {
        for (WebElement element : rowsOnPositionTabAos) {
            if (element.getText().equalsIgnoreCase(positionDetail)) {
                return true;
            }
        }

        return false;
    }

    public void getExecutedPrice() {
        executedPrice = getConfirmationValue("Price");
    }

    public void tapCtaButton(String buttonName) {
        if (driver instanceof AndroidDriver) {
            switch (buttonName) {
                case "Deposit detail" -> detailsButton.click();
            }
        }
    }

    //        public void selectOrderType(String orderType) {
//        if (driver instanceof AndroidDriver) {
//            orderTypeDropdownBtn.click();
//            for (WebElement element : orderTypeOptions) {
//                if (element.getText().equalsIgnoreCase(orderType)) {
//                    element.click();
//                    break;
//                }
//            }
//        }
//    }
    public void selectOrderType(String orderType) throws InterruptedException {
        if (driver instanceof AndroidDriver) {
            orderTypeDropdownBtn.click();
            Thread.sleep(2000);
            driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + orderType + "\"]/parent::android.view.ViewGroup")).click();
        }
    }

    public void selectStopLimitOption(String option) {
        stopOrderType = option;
        if (driver instanceof AndroidDriver) {
            driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + option + "\"]/parent::android.view.ViewGroup")).click();
        }
    }

    public void scrollDown() {
        SCROLLED = true;
        abs.swipeUp(driver);
    }

    public boolean tabIsSelected(String tabName) {
        return driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + tabName + "\"]/parent::android.view.ViewGroup")).isSelected();

    }

    public boolean getPendingOrdersDetail(String pendingOrderDetail) {
        for (WebElement element : rowsOnPendingOrdersTabAos) {
            if (element.getText().equalsIgnoreCase(pendingOrderDetail)) {
                return true;
            }
        }

        return false;
    }

    public void cancelOrder() {
        if (driver instanceof AndroidDriver) {
            crossButtonAos.click();
            abs.waitUtilElementFind(cancelOrderButtonAos);
            cancelOrderButtonAos.click();
        }
    }

    public void closePosition() {
        if (driver instanceof AndroidDriver) {
            crossButtonAos.click();
            abs.waitUtilElementFind(closePositionAos);
            closePositionAos.click();
            confirmClosePositionBtnAos.click();
        }
    }

    public void selectValidity(String option) {
        validity = option;
        if (driver instanceof AndroidDriver) {
            driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + option + "\"]/parent::android.view.ViewGroup")).click();
        }
    }

    public List<String> stopOrderConfirmationPageValues() {
        List<String> values = new ArrayList<>();
        values.add("Stop Loss Price");
        values.add("Take Profit Price");
        values.add("Direction");
        values.add("Volume");
        values.add("Validity");
        return values;
    }

    public List<String> marketOrderConfirmationPageValues() {
        List<String> values = new ArrayList<>();
        values.add("Stop Loss Price");
        values.add("Take Profit Price");
        values.add("Direction");
        values.add("Volume");
        return values;
    }


}
