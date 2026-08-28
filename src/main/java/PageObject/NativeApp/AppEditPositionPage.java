package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.GetPageElement;

import java.time.Duration;
import java.util.List;

public class AppEditPositionPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    private final GetPageElement getPageElement;
    public static String stopLossPrice;
    public static String takeProfitPrice;

    public static void resetCapturedOrderValues() {
        stopLossPrice = null;
        takeProfitPrice = null;
    }

    public AppEditPositionPage(AppiumDriver driver) {
        this.driver = driver;
        abs = new MobileAbstractComponents(driver);
        this.getPageElement = new GetPageElement(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(className = "android.widget.EditText")
    List<WebElement> inputFields;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]")
    WebElement stopLossPlusBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[1]")
    WebElement stopLossMinusBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[5]")
    WebElement takeProfitPlusBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[4]")
    WebElement takeProfitMinusBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[3]")
    WebElement stopLossClearBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[6]")
    WebElement takeProfitClearBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.EditText[1]")
    WebElement stopLossTextFieldAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.EditText[2]")
    WebElement takeProfitTextFieldAos;

    @FindBy(className = "android.widget.TextView")
    List<WebElement> textMessages;

    @FindBy(xpath = "(//android.widget.TextView[@text=\"Edit Position\"])[1]")
    WebElement headerAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[13]")
    WebElement closeBtnAos;


    public String getDisplayedValue(String label, String symbolDecimal) {
        abs.waitUntilElementVisible(By.xpath("//*[@text='Edit Position']"));
        String uiLabel = getPageElement.mapUiLabel(label);
        String rawValue = getPageElement.resolveLabelValue(uiLabel);
        if (rawValue == null || rawValue.isBlank()) {
            throw new NoSuchElementException("Could not find value on Edit Position for label: " + uiLabel);
        }
        return getPageElement.normalizeByLabel(label, rawValue.trim(), symbolDecimal);
    }

    public String getInputFieldValue(String inputFieldName) {
        if (driver instanceof AndroidDriver) {
            return switch (inputFieldName) {
                case "Stop Loss" -> inputFields.getFirst().getText();
                case "Take Profit" -> inputFields.getLast().getText();
                default -> throw new IllegalStateException("Unexpected value: " + inputFieldName);
            };
        }
        return inputFieldName;
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
                    abs.waitUntilElementFind(stopLossTextFieldAos);
                    abs.typeWithAndroidKeys((AndroidDriver) driver, stopLossTextFieldAos,
                            enterPrice);
                    //  stopLimitStopLossTextFieldAos.sendKeys(getStopLossPrice(direction, decimal));
                    stopLossPrice = abs.normalizePriceToDecimals(enterPrice, decimal);
                }
                case "Take Profit" -> {
                    if (direction.equalsIgnoreCase("BUY")) {
                        enterPrice = String.valueOf(Float.parseFloat(getTakeProfitPrice(direction, decimal)) + priceDifVal);
                    } else {
                        enterPrice = String.valueOf(Float.parseFloat(getTakeProfitPrice(direction, decimal)) - priceDifVal);
                    }

                    abs.waitUntilElementFind(takeProfitTextFieldAos);
                    abs.typeWithAndroidKeys((AndroidDriver) driver, takeProfitTextFieldAos,
                            enterPrice);
                    // stopLimitTakeProfitTextFieldAos.sendKeys(getTakeProfitPrice(direction, decimal));

                    takeProfitPrice = abs.normalizePriceToDecimals(enterPrice, decimal);
                }
            }
        }
    }

    public String getStopLossPrice(String direction, String symbolDecimal) {
        String price = "";
        WebElement text;
        switch (direction) {
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
        switch (direction) {
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

    public String getValidationValue(String label) {
        return switch (label) {
            case "Stop Loss Price", "Stop Loss" -> stopLossPrice;
            case "Take Profit Price", "Take Profit" -> takeProfitPrice;
            case "Direction" -> AppTradeView.selectedDirection;
            default -> null;
        };
    }

    public void tapsButton(String buttonName) {
        if (driver instanceof AndroidDriver) {
            if (buttonName.contains("Cancel Order")) {
                WebElement button = driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + buttonName + "\"]/parent::android.view.ViewGroup"));
                abs.waitUntilElementFind(button);
                button.click();
            } else {
                driver.findElement(By.xpath("(//android.widget.TextView[@text=\"" + buttonName + "\"])[2]/parent::android.view.ViewGroup")).click();
            }
        }
    }

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

    public boolean getHeader() {
        if (driver instanceof AndroidDriver) {
            abs.waitUntilElementFind(headerAos);
            return headerAos.isDisplayed();
        }
        return false;
    }

    public String getHeaderText() {
        if (driver instanceof AndroidDriver) {
            By locator = By.xpath("//*[@text='Edit Position']");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            String text = element.getText();
            if (text == null || text.trim().isEmpty()) {
                text = element.getAttribute("text");
            }

            return text;
        }
        return "";
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

}
