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
import java.util.ArrayList;
import java.util.List;

public class AppInstrumentDetailsPage {

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
    public static String editPrice;
    public static String estMargin;
    public static boolean SCROLLED = false;

    public AppInstrumentDetailsPage(AppiumDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
        this.abs = new MobileAbstractComponents(driver);
    }


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

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.EditText[1]")
    WebElement stopLossEditFieldAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.EditText[2]")
    WebElement takeProfitEditFieldAos;

    @FindBy(className = "android.widget.EditText")
    List<WebElement> editTextFieldAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup")
    WebElement orderTypeDropdownBtn;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.widget.TextView")
    WebElement dialogueTextAos;

    @FindBy(className = "android.widget.TextView")
    List<WebElement> textMessages;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[11]/android.view.ViewGroup")
    WebElement stopLossPlusBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[9]/android.view.ViewGroup")
    WebElement stopLossMinusBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[15]/android.view.ViewGroup")
    WebElement takeProfitPlusBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[13]/android.view.ViewGroup")
    WebElement takeProfitMinusBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[10]/android.view.ViewGroup")
    WebElement stopLossClearBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[14]/android.view.ViewGroup")
    WebElement takeProfitClearBtnAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[10]")
    WebElement checkboxAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[2]")
    WebElement crossButtonAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[12]")
    WebElement closeMarketConfirmationBtnAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[14]")
    WebElement closeLimitConfirmationBtnAos;


    public boolean getTextMessage(String messageContent) {
        if (driver instanceof AndroidDriver) {
            for (WebElement ele : textMessages) {
                if (ele.getText().equalsIgnoreCase(messageContent)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void switchProfitStopLoss() throws InterruptedException {
        if (driver instanceof AndroidDriver) {
            Thread.sleep(2000);
            stopLossSwitchAos.click();
        }
    }

    public String getStopLossPrice(String direction, String symbolDecimal) {
        String price = "";
        WebElement text;
        selectedDirection = direction;
        switch (selectedDirection) {
            case "BUY" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Stop Loss (≤\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≤")[1].trim().split("\\)")[0]));
            }
            case "SELL" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Stop Loss (≥\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≥")[1].trim().split("\\)")[0]));
            }
        }
        String formattedPrice = abs.normalizePriceToDecimals(price, symbolDecimal);
        stopLossPrice = formattedPrice;
        return formattedPrice;
    }

    public String getTakeProfitPrice(String direction, String symbolDecimal) {
        String price = "";
        WebElement text;
        selectedDirection = direction;
        switch (selectedDirection) {
            case "BUY" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Take Profit (≥\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≥")[1].trim().split("\\)")[0]));
            }
            case "SELL" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Take Profit (≤\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≤")[1].trim().split("\\)")[0]));
            }
        }
        String formattedPrice = abs.normalizePriceToDecimals(price, symbolDecimal);
        takeProfitPrice = formattedPrice;
        return formattedPrice;
    }

    public String getStopOrderPrice(String direction, String stopOrderType, String decimal) {
        String price = "";
        WebElement text;
        selectedDirection = direction;
        if (stopOrderType.contains("Stop")) {
            switch (selectedDirection) {
                case "BUY" -> {
                    text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Price (≥\")]"));
                    price = Float.toString(Float.parseFloat(text.getText().split("≥")[1].trim().split("\\)")[0]) + 25);
                }
                case "SELL" -> {
                    text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Price (≤\")]"));
                    price = Float.toString(Float.parseFloat(text.getText().split("≤")[1].trim().split("\\)")[0]) - 25);
                }
            }
        } else if (stopOrderType.contains("Limit")) {
            switch (selectedDirection) {
                case "BUY" -> {
                    text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Price (≤\")]"));
                    price = Float.toString(Float.parseFloat(text.getText().split("≤")[1].trim().split("\\)")[0]) - 25);
                }
                case "SELL" -> {
                    text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Price (≥\")]"));
                    price = Float.toString(Float.parseFloat(text.getText().split("≥")[1].trim().split("\\)")[0]) + 25);
                }
            }
        }
        stopOrderPrice = abs.normalizePriceToDecimals(price, decimal);
        if (AppSettingPage.isTradeConfirmNeeded) {
            executedPrice = stopOrderPrice;
        }
        return price;
    }

    public String getDefaultPrice(String direction, String priceType, String decimal) {
        if (driver instanceof AndroidDriver) {
            return switch (priceType) {
                case "Stop Loss" -> getStopLossPrice(direction, decimal);
                case "Take Profit" -> getTakeProfitPrice(direction, decimal);
                default -> "";
            };
        }
        return "Invalid price type";
    }

    public void fillInTextField(String textFieldName, String direction, String decimal) {
        if (driver instanceof AndroidDriver) {
            switch (textFieldName) {
                case "Stop Loss" -> {
                    if (!SCROLLED) {
                        abs.typeWithAndroidKeys((AndroidDriver) driver, marketStopLossTextFieldAos, getStopLossPrice(direction, decimal));
                        //  marketStopLossTextFieldAos.sendKeys(getStopLossPrice(direction, decimal));
                    } else {
                        abs.typeWithAndroidKeys((AndroidDriver) driver, stopLimitStopLossTextFieldAos, getStopLossPrice(direction, decimal));
                        //  stopLimitStopLossTextFieldAos.sendKeys(getStopLossPrice(direction, decimal));
                    }
                }
                case "Take Profit" -> {
                    if (!SCROLLED) {
                        abs.typeWithAndroidKeys((AndroidDriver) driver, marketTakeProfitTextFieldAos, getTakeProfitPrice(direction, decimal));
                        //  marketTakeProfitTextFieldAos.sendKeys(getTakeProfitPrice(direction, decimal));
                    } else {
                        abs.typeWithAndroidKeys((AndroidDriver) driver, stopLimitTakeProfitTextFieldAos, getTakeProfitPrice(direction, decimal));
                        // stopLimitTakeProfitTextFieldAos.sendKeys(getTakeProfitPrice(direction, decimal));
                    }
                }
                case "Lot Size" -> {
                    editTextFieldAos.getFirst().clear();
                    abs.typeWithAndroidKeys((AndroidDriver) driver, editTextFieldAos.getFirst(), lotSize);
                }
                case "Price" -> {
                    abs.waitUtilElementFind(editTextFieldAos.get(1));
                    editTextFieldAos.get(1).clear();
                    abs.typeWithAndroidKeys((AndroidDriver) driver, editTextFieldAos.get(1), getStopOrderPrice(direction, stopOrderType, decimal));
                }
            }
        }
    }

    public void fillInTextField(String textFieldName, String direction, String decimal, int priceDifVal) {
        String enterPrice;
        if (driver instanceof AndroidDriver) {
            switch (textFieldName) {
                case "Stop Loss" -> {
                    if (direction.equalsIgnoreCase("BUY")) {
                        enterPrice = String.valueOf(Float.parseFloat(getStopLossPrice(direction, decimal)) - priceDifVal);
                    } else {
                        enterPrice = String.valueOf(Float.parseFloat(getStopLossPrice(direction, decimal)) + priceDifVal);
                    }
                    if (!SCROLLED) {
                        abs.waitUtilElementFind(marketStopLossTextFieldAos);
                        abs.typeWithAndroidKeys((AndroidDriver) driver, marketStopLossTextFieldAos,
                                enterPrice);
                        //  marketStopLossTextFieldAos.sendKeys(getStopLossPrice(direction, decimal));
                    } else {
                        abs.waitUtilElementFind(stopLimitStopLossTextFieldAos);
                        abs.typeWithAndroidKeys((AndroidDriver) driver, stopLimitStopLossTextFieldAos,
                                enterPrice);
                        //  stopLimitStopLossTextFieldAos.sendKeys(getStopLossPrice(direction, decimal));
                    }
                    stopLossPrice = enterPrice;
                }
                case "Take Profit" -> {
                    if (direction.equalsIgnoreCase("BUY")) {
                        enterPrice = String.valueOf(Float.parseFloat(getTakeProfitPrice(direction, decimal)) + priceDifVal);
                    } else {
                        enterPrice = String.valueOf(Float.parseFloat(getTakeProfitPrice(direction, decimal)) - priceDifVal);
                    }
                    if (!SCROLLED) {
                        abs.waitUtilElementFind(marketTakeProfitTextFieldAos);
                        abs.typeWithAndroidKeys((AndroidDriver) driver, marketTakeProfitTextFieldAos,
                                enterPrice);
                        //  marketTakeProfitTextFieldAos.sendKeys(getTakeProfitPrice(direction, decimal));
                    } else {
                        abs.waitUtilElementFind(stopLimitTakeProfitTextFieldAos);
                        abs.typeWithAndroidKeys((AndroidDriver) driver, stopLimitTakeProfitTextFieldAos,
                                enterPrice);
                        // stopLimitTakeProfitTextFieldAos.sendKeys(getTakeProfitPrice(direction, decimal));
                    }
                    takeProfitPrice = enterPrice;
                }
                case "Lot Size" -> {
                    editTextFieldAos.getFirst().clear();
                    abs.typeWithAndroidKeys((AndroidDriver) driver, editTextFieldAos.getFirst(), lotSize);
                }
                case "Price" -> {
                    abs.waitUtilElementFind(editTextFieldAos.get(1));
                    editTextFieldAos.get(1).clear();
                    abs.typeWithAndroidKeys((AndroidDriver) driver, editTextFieldAos.get(1), getStopOrderPrice(direction, stopOrderType, decimal));
                }
            }
        }
    }

    public void editTextField(String textFieldName, String direction, String decimal) {
        if (driver instanceof AndroidDriver) {
            switch (textFieldName) {
                case "Stop Loss" -> {
                    abs.typeWithAndroidKeys((AndroidDriver) driver, stopLossEditFieldAos, getStopLossPrice(direction, decimal));
                    // stopLossEditFieldAos.sendKeys(getStopLossPrice(direction));
                }
                case "Take Profit" -> {
                    abs.typeWithAndroidKeys((AndroidDriver) driver, takeProfitEditFieldAos, getTakeProfitPrice(direction, decimal));
                    //   takeProfitEditFieldAos.sendKeys(getTakeProfitPrice(direction));
                }
                case "Lot Size" -> editTextFieldAos.getFirst().sendKeys("0.45");
                case "Price" -> {
                    editTextFieldAos.get(1).clear();
                    abs.typeWithAndroidKeys((AndroidDriver) driver, editTextFieldAos.get(1), getStopOrderPrice(direction, stopOrderType, decimal));
                }
                case "Stop" -> {
                    String editStopPrice = getEditPrice(direction, decimal);
                    editTextFieldAos.getFirst().clear();
                    abs.typeWithAndroidKeys((AndroidDriver) driver, editTextFieldAos.getFirst(), editStopPrice);
                }
            }
        }
    }

    public void tapsButton(String buttonName) {
        if (driver instanceof AndroidDriver) {
            if (buttonName.contains("Cancel Order")) {
                WebElement button = driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + buttonName + "\"]/parent::android.view.ViewGroup"));
                abs.waitUtilElementClickable(button);
                button.click();
            } else {
                driver.findElement(By.xpath("(//android.widget.TextView[@text=\"" + buttonName + "\"])[2]/parent::android.view.ViewGroup")).click();
            }
        }
    }

    public void tapsButtonOnConfirm(String buttonName) {
        if (driver instanceof AndroidDriver) {
            if (buttonName.contains("Position") || buttonName.contains("Modify") || buttonName.contains("Cancel Order")) {
                WebElement button = driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + buttonName + "\"]/parent::android.view.ViewGroup"));
                // abs.waitUtilElementFind(button);
                abs.waitUtilElementClickable(button);
                button.click();
            } else if (buttonName.equalsIgnoreCase("Don't Show Again")) {
                checkboxAos.click();
            } else if (buttonName.equalsIgnoreCase("Cross")) {
                crossButtonAos.click();
            } else if (buttonName.equalsIgnoreCase("x")) {
                closeConfirmation();
            } else {
                driver.findElement(By.xpath("(//android.widget.TextView[@text=\"" + buttonName + "\"])[2]/parent::android.view.ViewGroup")).click();
            }
        }
    }


    public String getInputFieldValue(String inputFieldName) {
        // return driver.findElement(By.className("android.widget.EditText")).getText();
        if (driver instanceof AndroidDriver) {
            return switch (inputFieldName) {
                case "Lots" -> editTextFieldAos.getFirst().getText();
                case "Stop Loss" -> editTextFieldAos.get(1).getText();
                case "Take Profit" -> editTextFieldAos.getLast().getText();
                default -> "";
            };
        }
        return driver.findElements(By.className("android.widget.EditText")).getFirst().getText();
    }

    public void fillValueIntoTextField(String textFieldName, String value) throws InterruptedException {
        if (driver instanceof AndroidDriver) {
            switch (textFieldName) {
                case "Lot Size" -> {
                    driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + value + "\"]/parent::android.view.ViewGroup")).click();
                    Thread.sleep(1000);
                    lotSize = getInputFieldValue("Lots");
                }
            }
        }
    }

    public void getExecutedPrice() {
        executedPrice = getDetailValue("Price");
    }

    public String getDetailValue(String value) {
        List<WebElement> text = driver.findElements(By.className("android.widget.TextView"));
        for (int i = 0; i < text.size(); i++) {
            if (text.get(i).getText().equalsIgnoreCase(value)) {
                if (value.equalsIgnoreCase("Volume")) {
                    return text.get(i + 1).getText().split("Lots")[0].trim();
                } else if (value.equalsIgnoreCase("Estimated Margin")) {
                    return text.get(i + 1).getText().split("USD")[1].trim().replace(",", "");
                }
                return text.get(i + 1).getText();
            }
        }
        return null;
    }

    public void selectOrderType(String orderType) throws InterruptedException {
        if (driver instanceof AndroidDriver) {
            abs.waitUtilElementClickable(orderTypeDropdownBtn);
            orderTypeDropdownBtn.click();
            WebElement orderTypeBtn = driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + orderType + "\"]/parent::android.view.ViewGroup"));
            abs.waitUtilElementClickable(orderTypeBtn);
            orderTypeBtn.click();
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
        if (!(stopLossPrice == null)) {
            values.add("Stop Loss Price");
        }
        if (!(takeProfitPrice == null)) {
            values.add("Take Profit Price");
        }
        values.add("Direction");
        values.add("Volume");
        values.add("Product");
        values.add("Estimated Margin");
        return values;
    }

    public String getEditPrice(String direction, String decimal) {
        String price = "";
        if (driver instanceof AndroidDriver) {
            switch (direction) {
                case "BUY" -> price = Float.toString(Float.parseFloat(editTextFieldAos.getFirst().getText()) + 10);
                case "SELL" -> price = Float.toString(Float.parseFloat(editTextFieldAos.getFirst().getText()) - 10);
            }
        }
        editPrice = abs.normalizePriceToDecimals(price, decimal);
        return price;
    }

    public void setLotSize(String symbolLotSize) {
        lotSize = symbolLotSize;
    }

    public String getValidationValue(String label) {
        return switch (label) {
            case "Stop Loss Price", "Stop Loss" -> stopLossPrice;
            case "Take Profit Price", "Take Profit" -> takeProfitPrice;
            case "Direction" -> AppTradeView.selectedDirection;
            case "Volume", "Lots" -> lotSize;
            case "Stop Order Price" -> stopOrderPrice;
            case "Validity" -> validity;
            case "Est. Margin", "Estimated Margin" -> estMargin;
            case "Product" -> AppMarketsPage.tradeSymbol;
            default -> null;
        };
    }

    public String getValue(String label, String symbol) {
        if (driver instanceof AndroidDriver) {
            if (label.equalsIgnoreCase("Lots")) {
                return editTextFieldAos.getFirst().getText();
            } else {
                return abs.getLabelValue(label);
            }
        }
        return "label not found";
    }

    public void setEstMargin(Integer initialMargin) {
        estMargin = abs.normalizePriceToDecimals(String.valueOf(Float.parseFloat(lotSize) * initialMargin), "2");
    }

    public boolean getToggleStatus() {
        //return Boolean.parseBoolean(((RemoteWebElement) stopLossSwitchAos).getDomAttribute("checked"));
        return Boolean.parseBoolean(stopLossSwitchAos.getDomAttribute("checked"));
    }

    public void adjustPrice(String ctaBtn, String priceType) {
        if (driver instanceof AndroidDriver) {
            if (ctaBtn.equalsIgnoreCase("+")) {
                switch (priceType) {
                    case "Stop Loss" -> stopLossPlusBtnAos.click();
                    case "Take Profit" -> takeProfitPlusBtnAos.click();
                }
            } else if (ctaBtn.equalsIgnoreCase("-")) {
                switch (priceType) {
                    case "Stop Loss" -> stopLossMinusBtnAos.click();
                    case "Take Profit" -> takeProfitMinusBtnAos.click();
                }
            } else if (ctaBtn.equalsIgnoreCase("✕")) {
                switch (priceType) {
                    case "Stop Loss" -> stopLossClearBtnAos.click();
                    case "Take Profit" -> takeProfitClearBtnAos.click();
                }
            }
        }
    }

    public void clearPrice(String priceType) {
        if (driver instanceof AndroidDriver) {
            switch (priceType) {
                case "Stop Loss" -> stopLossClearBtnAos.click();
                case "Take Profit" -> takeProfitClearBtnAos.click();
            }
        }
    }

    public boolean getCheckboxStatus(String checkboxLabel) {
        if (driver instanceof AndroidDriver) {
            return switch (checkboxLabel) {
                case "Don't Show Again" -> checkboxAos.isSelected();
                default -> throw new IllegalStateException("Unexpected value: " + checkboxLabel);
            };
        }
        return false;
    }

    public void tapCross() {
        if (driver instanceof AndroidDriver) {
            crossButtonAos.click();
        }
    }

    public void closeConfirmation() {
        if (driver instanceof AndroidDriver) {
            if (AppInstrumentDetailsPage.stopOrderType.isEmpty()) {
                abs.waitUtilElementClickable(closeMarketConfirmationBtnAos);
                closeMarketConfirmationBtnAos.click();
            }
            abs.waitUtilElementClickable(closeLimitConfirmationBtnAos);
            closeLimitConfirmationBtnAos.click();
        }
    }

    public boolean getTpslToggleStatus() {
        return stopLossSwitchAos.isDisplayed();
    }
}
