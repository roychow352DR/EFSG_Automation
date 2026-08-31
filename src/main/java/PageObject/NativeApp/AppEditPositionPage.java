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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            return priceField(inputFieldName).getText();
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
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        String enterPrice;
        switch (textFieldName) {
            case "Stop Loss" -> {
                if (direction.equalsIgnoreCase("BUY")) {
                    enterPrice = String.valueOf(Float.parseFloat(getStopLossPrice(direction, decimal)) - priceDifVal);
                } else {
                    enterPrice = String.valueOf(Float.parseFloat(getStopLossPrice(direction, decimal)) + priceDifVal);
                }
                stopLossPrice = abs.normalizePriceToDecimals(enterPrice, decimal);
            }
            case "Take Profit" -> {
                if (direction.equalsIgnoreCase("BUY")) {
                    enterPrice = String.valueOf(Float.parseFloat(getTakeProfitPrice(direction, decimal)) + priceDifVal);
                } else {
                    enterPrice = String.valueOf(Float.parseFloat(getTakeProfitPrice(direction, decimal)) - priceDifVal);
                }
                takeProfitPrice = abs.normalizePriceToDecimals(enterPrice, decimal);
            }
            default -> throw new IllegalStateException("Unexpected value: " + textFieldName);
        }
        typeIntoPriceField(textFieldName, enterPrice);
    }

    private void typeIntoPriceField(String textFieldName, String enterPrice) {
        WebElement field = priceField(textFieldName);
        try {
            field.clear();
        } catch (Exception ignored) {
            abs.tapElement(field);
        }
        field = priceField(textFieldName);
        abs.typeWithAndroidKeys((AndroidDriver) driver, field, enterPrice);
    }

    private WebElement priceField(String priceType) {
        abs.waitUntilElementVisible(By.xpath("//*[@text='Edit Position']"));
        WebElement label = priceLabel(priceType);
        int[] labelBounds = parseBounds(elementAttribute(label, "bounds"));

        WebElement closest = null;
        int bestScore = Integer.MAX_VALUE;
        for (WebElement field : driver.findElements(By.className("android.widget.EditText"))) {
            int[] fieldBounds = parseBounds(elementAttribute(field, "bounds"));
            if (labelBounds == null || fieldBounds == null) {
                continue;
            }
            int labelCenterY = (labelBounds[1] + labelBounds[3]) / 2;
            int fieldCenterY = (fieldBounds[1] + fieldBounds[3]) / 2;
            int verticalGap = fieldCenterY - labelCenterY;
            if (verticalGap < -20) {
                continue;
            }
            int score = Math.abs(verticalGap) * 100 + Math.abs(fieldBounds[0] - labelBounds[0]);
            if (score < bestScore) {
                bestScore = score;
                closest = field;
            }
        }

        if (closest == null) {
            throw new NoSuchElementException("Could not find EditText for price type: " + priceType);
        }
        return closest;
    }

    private WebElement priceLabel(String priceType) {
        String labelToken = switch (priceType) {
            case "Stop Loss" -> "Stop Loss";
            case "Take Profit" -> "Take Profit";
            default -> throw new IllegalStateException("Unexpected value: " + priceType);
        };

        abs.waitUntilElementVisible(
                By.xpath("//android.widget.TextView[contains(@text,\"" + labelToken + "\")]")
        );

        WebElement fieldLabel = null;
        for (WebElement el : driver.findElements(
                By.xpath("//android.widget.TextView[contains(@text,\"" + labelToken + "\")]"))) {
            String text = el.getText() == null ? "" : el.getText().trim();
            if (text.contains("&") || text.toLowerCase().contains(" and ")) {
                continue;
            }
            fieldLabel = el;
        }

        if (fieldLabel == null) {
            throw new NoSuchElementException("Could not find field label for price type: " + priceType);
        }
        return fieldLabel;
    }

    private String elementAttribute(WebElement element, String name) {
        String value = element.getDomAttribute(name);
        if (value == null || value.isBlank()) {
            value = element.getDomProperty(name);
        }
        return value;
    }

    private int[] parseBounds(String bounds) {
        if (bounds == null || bounds.isBlank()) {
            return null;
        }
        Matcher matcher = Pattern.compile("\\[(\\d+),(\\d+)]\\[(\\d+),(\\d+)]").matcher(bounds);
        if (!matcher.matches()) {
            return null;
        }
        return new int[]{
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)),
                Integer.parseInt(matcher.group(4))
        };
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
                text = elementAttribute(element, "text");
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
