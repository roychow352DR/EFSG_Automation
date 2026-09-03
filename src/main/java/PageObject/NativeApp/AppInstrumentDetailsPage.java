package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.GetPageElement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppInstrumentDetailsPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    private final GetPageElement getPageElement;
    public static String stopLossPrice;
    public static String stopOrderPrice;
    public static String stopOrderType = "";
    public static String takeProfitPrice;
    public static String selectedDirection;
    public static String lotSize;
    public static String executedPrice;
    public static String validity;
    public static String editPrice;
    public static String estMargin;
    public static boolean SCROLLED = false;

    public static void resetCapturedOrderValues() {
        stopLossPrice = null;
        stopOrderPrice = null;
        stopOrderType = "";
        takeProfitPrice = null;
        selectedDirection = null;
        lotSize = null;
        executedPrice = null;
        validity = null;
        editPrice = null;
        estMargin = null;
        SCROLLED = false;
    }

    public AppInstrumentDetailsPage(AppiumDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
        this.abs = new MobileAbstractComponents(driver);
        this.getPageElement = new GetPageElement(driver);
    }


    @FindBy(xpath = "//android.widget.Switch")
    WebElement stopLossSwitchAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[10]/android.widget.EditText")
    WebElement marketStopLossTextFieldAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[14]/android.widget.EditText")
    WebElement marketTakeProfitTextFieldAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[7]/android.widget.EditText")
    WebElement stopLimitStopLossTextFieldAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[11]/android.widget.EditText")
    WebElement stopLimitTakeProfitTextFieldAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.EditText[1]")
    WebElement stopLossEditFieldAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.EditText[2]")
    WebElement takeProfitEditFieldAos;

    @FindBy(className = "android.widget.EditText")
    List<WebElement> editTextFieldAos;

//    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup")
//    WebElement orderTypeDropdownBtn;


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

//    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[2]")
//    WebElement crossButtonAos;
//
    private final By crossButtonAos = By.xpath("//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[2]");

//    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[12]")
//    WebElement closeMarketConfirmationBtnAos;

    private final By closeMarketConfirmationBtnAos = By.xpath("//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[12]");

//    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[14]")
//    WebElement closeLimitConfirmationBtnAos;

    private final By closeLimitConfirmationBtnAos = By.xpath("//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[14]");


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

    public void switchProfitStopLoss() {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        waitForOrderTicket();
        TimeoutException lastError = null;
        for (By locator : tpslToggleLocators()) {
            try {
                if (isTpslLabelLocator(locator)) {
                    abs.tapOnSameRowRight(locator, 10);
                } else {
                    abs.tapVisible(locator, 10);
                }
                return;
            } catch (TimeoutException e) {
                lastError = e;
            }
        }
        throw lastError != null
                ? lastError
                : new TimeoutException("Take Profit and Stop Loss toggle was not visible");
    }

    private List<By> tpslToggleLocators() {
        return List.of(
                By.xpath("//android.widget.TextView[contains(@text,'Take Profit') and contains(@text,'Stop Loss')]"),
                By.xpath("//*[contains(@text,'Take Profit') and contains(@text,'Stop Loss')]"),
                By.xpath("//android.widget.Switch"),
                By.xpath("//*[@checkable='true']")
        );
    }

    private boolean isTpslLabelLocator(By locator) {
        String locatorText = locator.toString();
        return locatorText.contains("Take Profit") && locatorText.contains("Stop Loss");
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
                    abs.waitUntilElementFind(editTextFieldAos.get(1));
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
                        abs.waitUntilElementFind(marketStopLossTextFieldAos);
                        abs.typeWithAndroidKeys((AndroidDriver) driver, marketStopLossTextFieldAos,
                                enterPrice);
                        //  marketStopLossTextFieldAos.sendKeys(getStopLossPrice(direction, decimal));
                    } else {
                        abs.waitUntilElementFind(editTextFieldAos.get(1));
                        abs.typeWithAndroidKeys((AndroidDriver) driver, editTextFieldAos.get(1),
                                enterPrice);
                        //  stopLimitStopLossTextFieldAos.sendKeys(getStopLossPrice(direction, decimal));
                    }
                    stopLossPrice = abs.normalizePriceToDecimals(enterPrice,decimal);
                }
                case "Take Profit" -> {
                    if (direction.equalsIgnoreCase("BUY")) {
                        enterPrice = String.valueOf(Float.parseFloat(getTakeProfitPrice(direction, decimal)) + priceDifVal);
                    } else {
                        enterPrice = String.valueOf(Float.parseFloat(getTakeProfitPrice(direction, decimal)) - priceDifVal);
                    }
                    if (!SCROLLED) {
                        abs.waitUntilElementFind(marketTakeProfitTextFieldAos);
                        abs.typeWithAndroidKeys((AndroidDriver) driver, marketTakeProfitTextFieldAos,
                                enterPrice);
                        //  marketTakeProfitTextFieldAos.sendKeys(getTakeProfitPrice(direction, decimal));
                    } else {
                        abs.waitUntilElementFind(editTextFieldAos.getLast());
                        abs.typeWithAndroidKeys((AndroidDriver) driver, editTextFieldAos.getLast(),
                                enterPrice);
                        // stopLimitTakeProfitTextFieldAos.sendKeys(getTakeProfitPrice(direction, decimal));
                    }
                    takeProfitPrice = abs.normalizePriceToDecimals(enterPrice,decimal);
                }
                case "Lot Size" -> {
                    editTextFieldAos.getFirst().clear();
                    abs.typeWithAndroidKeys((AndroidDriver) driver, editTextFieldAos.getFirst(), lotSize);
                }
                case "Price" -> {
                    abs.waitUntilElementFind(editTextFieldAos.get(1));
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
                    String editedStopLoss;
                    if (direction.equalsIgnoreCase("BUY")) {
                        editedStopLoss = Float.toString(Float.parseFloat(getStopLossPrice(direction, decimal)) - 25);
                        abs.typeWithAndroidKeys((AndroidDriver) driver, stopLossEditFieldAos, editedStopLoss);
                    } else {
                        editedStopLoss = Float.toString(Float.parseFloat(getStopLossPrice(direction, decimal)) + 25);
                        abs.typeWithAndroidKeys((AndroidDriver) driver, stopLossEditFieldAos, editedStopLoss);
                    }
                    stopLossPrice = abs.normalizePriceToDecimals(editedStopLoss, decimal);
                }
                case "Take Profit" -> {
                    String editedTakeProfit;
                    if (direction.equalsIgnoreCase("BUY")) {
                        editedTakeProfit = Float.toString(Float.parseFloat(getTakeProfitPrice(direction, decimal)) + 25);
                        abs.typeWithAndroidKeys((AndroidDriver) driver, takeProfitEditFieldAos, editedTakeProfit);
                    } else {
                        editedTakeProfit = Float.toString(Float.parseFloat(getTakeProfitPrice(direction, decimal)) - 25);
                        abs.typeWithAndroidKeys((AndroidDriver) driver, takeProfitEditFieldAos, editedTakeProfit);
                    }
                    takeProfitPrice = abs.normalizePriceToDecimals(editedTakeProfit, decimal);
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
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        if (buttonName.equals("Edit Position") || buttonName.equals("Modify Order")) {
            abs.tapBottomMost(By.xpath("//*[@text='" + buttonName + "']"), 10);
            return;
        }
        By button;
        if (buttonName.contains("Cancel Order")) {
            button = By.xpath("//android.widget.TextView[@text=\"" + buttonName + "\"]/parent::android.view.ViewGroup");
        } else {
            button = By.xpath("(//android.widget.TextView[@text=\"" + buttonName + "\"])[2]/parent::android.view.ViewGroup");
        }
        abs.waitUntilElementClickable(button).click();
    }

    public void tapsButtonOnConfirm(String buttonName) {
        if (driver instanceof AndroidDriver) {
            if (buttonName.contains("Position") || buttonName.contains("Modify") || buttonName.contains("Cancel Order")) {
                By overlayButton = By.xpath(
                        "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]//*[@text=\"" + buttonName + "\"]"
                );
                abs.tapBottomMost(overlayButton, 15);
            } else if (buttonName.equalsIgnoreCase("Don't Show Again")) {
                checkboxAos.click();
            } else if (buttonName.equalsIgnoreCase("Cross") || buttonName.equalsIgnoreCase("x")) {
                closeConfirmation();
            } else {
                By button = By.xpath("(//android.widget.TextView[@text=\"" + buttonName + "\"])[2]/parent::android.view.ViewGroup");
                abs.waitUntilElementClickable(button).click();
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
        if (!(driver instanceof AndroidDriver) || !"Lot Size".equals(textFieldName)) {
            return;
        }
        TimeoutException lastError = null;
        for (By locator : lotChipLocators(value)) {
            try {
                abs.tapBottomMost(locator, 8);
                Thread.sleep(500);
                lotSize = getInputFieldValue("Lots");
                return;
            } catch (TimeoutException e) {
                lastError = e;
            }
        }
        throw lastError != null
                ? lastError
                : new TimeoutException("Lot size chip was not visible: " + value);
    }

    private List<By> lotChipLocators(String value) {
        List<By> locators = new ArrayList<>();
        for (String text : lotChipTexts(value)) {
            locators.add(By.xpath("//android.widget.TextView[@text='" + text + "']"));
            locators.add(By.xpath("//*[@text='" + text + "']"));
            locators.add(By.xpath("//android.widget.TextView[@text='" + text + "']/parent::android.view.ViewGroup"));
        }
        return locators;
    }

    private List<String> lotChipTexts(String value) {
        List<String> texts = new ArrayList<>();
        texts.add(value.trim());
        try {
            double number = Double.parseDouble(value.trim());
            addLotChipText(texts, String.valueOf(number));
            addLotChipText(texts, String.format(Locale.US, "%.1f", number));
            addLotChipText(texts, String.format(Locale.US, "%.2f", number));
        } catch (NumberFormatException ignored) {
        }
        return texts;
    }

    private void addLotChipText(List<String> texts, String text) {
        if (!texts.contains(text)) {
            texts.add(text);
        }
    }

    public void getExecutedPrice() {
        executedPrice = getDetailValue("Price");
    }

    public String getDetailValue(String value) {
        getPageElement.waitAndCaptureIfNeeded(
                By.xpath("//android.view.ViewGroup[@resource-id='RNE__Overlay']"), 10);
        String uiLabel = getPageElement.mapUiLabel(value);
        String rawValue = getPageElement.readLabelValueFast(uiLabel);
        if (rawValue == null || rawValue.isBlank()) {
            throw new NoSuchElementException("Could not find value in hierarchy for label: " + uiLabel);
        }
        return getPageElement.normalizeByLabel(value, rawValue.trim(), "");
    }

    public String getDetailValue(String label, String symbolDecimal) {
        String uiLabel = getPageElement.mapUiLabel(label);
        String rawValue = getPageElement.readLabelValueFast(uiLabel);
        if (rawValue == null || rawValue.isBlank()) {
            throw new NoSuchElementException("Could not find value in hierarchy for label: " + uiLabel);
        }
        getPageElement.logInfo("Resolved raw value for " + uiLabel + ": " + rawValue);
        return getPageElement.normalizeByLabel(label, rawValue.trim(), symbolDecimal);
    }

    public void waitForConfirmationPopup() {
        getPageElement.waitAndCapture(
                By.xpath("//android.view.ViewGroup[@resource-id='RNE__Overlay']"), 10);
    }



    public void selectOrderType(String orderType) {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }

        String text = orderType.trim();
        waitForOrderTicket();
        openOrderTypePicker(text);
        clickOrderTypeOption(text);
    }

    private void waitForOrderTicket() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                            "//android.widget.TextView[@text='Market Order' or @text='Limit / Stop Order' or @text='Lots']"
                    )));
        } catch (TimeoutException e) {
            throw new TimeoutException("Order ticket was not visible", e);
        }
    }

    private void openOrderTypePicker(String optionText) {
        if (isOrderTypeOptionVisible(optionText, 1)) {
            return;
        }
        By marketOrderText = By.xpath("//android.widget.TextView[@text='Market Order']");
        try {
            abs.tapVisibleRight(marketOrderText, 8);
            if (isOrderTypeOptionVisible(optionText, 5)) {
                return;
            }
        } catch (TimeoutException ignored) {
        }
        TimeoutException lastError = null;
        for (By locator : orderTypeTriggerLocators()) {
            try {
                abs.tapVisible(locator, 8);
                if (isOrderTypeOptionVisible(optionText, 5)) {
                    return;
                }
            } catch (TimeoutException e) {
                lastError = e;
            }
        }
        throw new TimeoutException("Failed to open order type dropdown", lastError);
    }

    private List<By> orderTypeTriggerLocators() {
        return List.of(
                By.xpath("//android.widget.TextView[@text='Market Order']/ancestor::android.view.ViewGroup[.//android.widget.TextView[@text='Order']][1]"),
                By.xpath("//android.widget.TextView[@text='Market Order']/parent::android.view.ViewGroup"),
                By.xpath("//android.widget.TextView[@text='Market Order']"),
                By.xpath("//*[@text='Market Order']"),
                By.xpath("//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup")
        );
    }

    private boolean isOrderTypeOptionVisible(String optionText, int seconds) {
        By exact = By.xpath("//android.widget.TextView[@text=\"" + optionText + "\"]");
        By containsLimitStop = By.xpath(
                "//android.widget.TextView[contains(@text,'Limit') and contains(@text,'Stop')]"
        );

        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .until(ExpectedConditions.or(
                            ExpectedConditions.visibilityOfElementLocated(exact),
                            ExpectedConditions.visibilityOfElementLocated(containsLimitStop)
                    ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void clickOrderTypeOption(String text) {
        TimeoutException lastError = null;
        for (By locator : List.of(
                By.xpath("//android.widget.TextView[@text=\"" + text + "\"]"),
                By.xpath("//*[@text=\"" + text + "\"]"),
                By.xpath("//android.widget.TextView[@text=\"" + text + "\"]/parent::android.view.ViewGroup"),
                By.xpath("//android.widget.TextView[contains(@text,'Limit') and contains(@text,'Stop')]")
        )) {
            try {
                abs.tapVisible(locator, 8);
                return;
            } catch (TimeoutException e) {
                lastError = e;
            }
        }
        throw new TimeoutException("Failed to click order type option text: " + text, lastError);
    }



    public void selectStopLimitOption(String option) {
        stopOrderType = option;
        if (driver instanceof AndroidDriver) {
            abs.tapVisible(By.xpath("//android.widget.TextView[@text=\"" + option + "\"]/parent::android.view.ViewGroup"), 10);
        }
    }

    public void scrollDown() {
        SCROLLED = true;
        //abs.swipeUp(driver);
        abs.swipeUpUntilEnd(driver);
    }

    public void selectValidity(String option) {
        validity = option;
        if (driver instanceof AndroidDriver) {
            abs.tapVisible(By.xpath("//android.widget.TextView[@text=\"" + option + "\"]/parent::android.view.ViewGroup"), 10);
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

    private String displayedLotSize(String value) {
        if (value == null) {
            return null;
        }
        String text = value.replaceAll("(?i)\\s*Lots?", "").trim().replace(",", "");
        return abs.normalizePriceToDecimals(text, "2");
    }

    public String getValidationValue(String label) {
        return switch (label) {
            case "Stop Loss Price", "Stop Loss" -> stopLossPrice;
            case "Take Profit Price", "Take Profit" -> takeProfitPrice;
            case "Direction" -> AppTradeView.selectedDirection;
            case "Lots" -> displayedLotSize(lotSize);
            case "Volume" -> getPageElement.canonicalizeVolume(lotSize);
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
            }
            String uiLabel = getPageElement.mapUiLabel(label);
            String rawValue = getPageElement.readLabelValueFast(uiLabel);
            return rawValue == null ? null : getPageElement.normalizeByLabel(label, rawValue, symbol);
        }
        return "label not found";
    }

    public void setEstMargin(Integer initialMargin) {
        estMargin = abs.normalizePriceToDecimals(String.valueOf(Float.parseFloat(lotSize) * initialMargin), "2");
    }

    public boolean getToggleStatus() {
        List<WebElement> switches = driver.findElements(By.xpath("//android.widget.Switch"));
        for (WebElement toggle : switches) {
            String checked = toggle.getDomAttribute("checked");
            if (checked == null || checked.isBlank() || "null".equalsIgnoreCase(checked)) {
                checked = toggle.getAttribute("checked");
            }
            if (checked != null && !"null".equalsIgnoreCase(checked)) {
                return Boolean.parseBoolean(checked);
            }
        }
        return !driver.findElements(By.xpath(
                "//android.widget.TextView[contains(@text,'Stop Loss (')]")).isEmpty();
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
        closeConfirmation();
    }

    public void closeConfirmation() {
        getPageElement.clearPageSourceCache();
        if (!(driver instanceof AndroidDriver)) {
            return;
        }

        abs.waitUntilElementVisible(By.xpath("//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]"));

        By[] closeButtons = {
                By.xpath("//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[last()]"),
                closeMarketConfirmationBtnAos,
                closeLimitConfirmationBtnAos,
                crossButtonAos
        };

        Exception lastError = null;
        for (By locator : closeButtons) {
            try {
                WebElement closeBtn = new WebDriverWait(driver, Duration.ofSeconds(8))
                        .until(ExpectedConditions.visibilityOfElementLocated(locator));
                abs.tapElement(closeBtn);
                return;
            } catch (Exception e) {
                lastError = e;
            }
        }

        throw new TimeoutException("Failed to close confirmation overlay", lastError);
    }

    public boolean getTpslToggleStatus() {
        return stopLossSwitchAos.isDisplayed();
    }
}
