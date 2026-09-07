package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.GetPageElement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
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
        getPageElement.waitAndCaptureIfNeeded(By.xpath("//*[@text='Edit Position']"), 10);
        String uiLabel = getPageElement.mapUiLabel(label);
        String rawValue = getPageElement.readLabelValueFast(uiLabel);
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
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        Point point = priceStepperPoint(priceType, ctaBtn);
        getPageElement.logInfo("Tapping " + ctaBtn + " on " + priceType + " at " + point.getX() + "," + point.getY());
        abs.tapAt(point.getX(), point.getY());
    }

    private Point priceStepperPoint(String fieldName, String ctaBtn) {
        WebElement field = priceField(fieldName);
        int[] fieldBounds = parseBounds(elementAttribute(field, "bounds"));
        if (fieldBounds == null) {
            throw new NoSuchElementException("Could not read bounds for " + fieldName);
        }
        int fieldCenterY = (fieldBounds[1] + fieldBounds[3]) / 2;
        int fieldLeft = fieldBounds[0];
        int fieldRight = fieldBounds[2];
        List<int[]> left = new ArrayList<>();
        List<int[]> right = new ArrayList<>();
        for (int[] bounds : compactControlsOnRow(fieldCenterY)) {
            int centerX = (bounds[0] + bounds[2]) / 2;
            if (centerX < fieldLeft) {
                left.add(bounds);
            } else if (centerX > fieldRight) {
                right.add(bounds);
            }
        }
        left.sort(Comparator.comparingInt(bounds -> bounds[0]));
        right.sort(Comparator.comparingInt(bounds -> bounds[0]));

        String action = normalizeStepperAction(ctaBtn);
        if ("plus".equals(action)) {
            if (!right.isEmpty()) {
                return controlCenter(right.getFirst());
            }
            return new Point(fieldRight + 36, fieldCenterY);
        }
        if ("minus".equals(action)) {
            if (!left.isEmpty()) {
                return controlCenter(left.getLast());
            }
            return new Point(Math.max(8, fieldLeft - 36), fieldCenterY);
        }
        if ("clear".equals(action)) {
            if (right.size() >= 2) {
                return controlCenter(right.getLast());
            }
            if (right.size() == 1) {
                return new Point(right.getFirst()[2] + 26, fieldCenterY);
            }
            return new Point(fieldRight + 160, fieldCenterY);
        }
        throw new IllegalArgumentException("Unsupported stepper button: " + ctaBtn);
    }

    private String normalizeStepperAction(String ctaBtn) {
        if (ctaBtn == null || ctaBtn.isBlank()) {
            throw new IllegalArgumentException("Stepper button was empty");
        }
        String text = ctaBtn.trim();
        if (text.equals("+") || text.equalsIgnoreCase("plus")) {
            return "plus";
        }
        if (text.equals("-") || text.equalsIgnoreCase("minus")) {
            return "minus";
        }
        if (text.equals("✕") || text.equals("×") || text.equalsIgnoreCase("x")
                || text.equals("?") || text.equalsIgnoreCase("clear")) {
            return "clear";
        }
        return text.toLowerCase(Locale.ROOT);
    }

    private List<int[]> compactControlsOnRow(int rowCenterY) {
        List<int[]> found = new ArrayList<>();
        List<By> locators = List.of(
                By.xpath("//android.widget.ScrollView//android.view.ViewGroup[@clickable='true']"),
                By.xpath("//*[@text='+' or @text='-' or @text='✕' or @text='×' or @text='x' or @text='X']"),
                By.className("android.widget.ImageView")
        );
        for (By locator : locators) {
            for (WebElement el : driver.findElements(locator)) {
                try {
                    int[] bounds = parseBounds(elementAttribute(el, "bounds"));
                    if (bounds == null) {
                        continue;
                    }
                    int width = bounds[2] - bounds[0];
                    int height = bounds[3] - bounds[1];
                    if (width < 18 || width > 96 || height < 18 || height > 96) {
                        continue;
                    }
                    int centerY = (bounds[1] + bounds[3]) / 2;
                    if (Math.abs(centerY - rowCenterY) > 48) {
                        continue;
                    }
                    if (alreadyHasSimilarControl(found, bounds)) {
                        continue;
                    }
                    found.add(bounds);
                } catch (StaleElementReferenceException ignored) {
                }
            }
        }
        return found;
    }

    private boolean alreadyHasSimilarControl(List<int[]> found, int[] bounds) {
        int centerX = (bounds[0] + bounds[2]) / 2;
        int centerY = (bounds[1] + bounds[3]) / 2;
        for (int[] existing : found) {
            int existingX = (existing[0] + existing[2]) / 2;
            int existingY = (existing[1] + existing[3]) / 2;
            if (Math.abs(existingX - centerX) <= 12 && Math.abs(existingY - centerY) <= 12) {
                return true;
            }
        }
        return false;
    }

    private Point controlCenter(int[] bounds) {
        return new Point((bounds[0] + bounds[2]) / 2, (bounds[1] + bounds[3]) / 2);
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
        try {
            String value = element.getAttribute(name);
            if (value == null || value.isBlank() || "null".equalsIgnoreCase(value)) {
                return null;
            }
            return value;
        } catch (RuntimeException e) {
            return null;
        }
    }

    private int[] parseBounds(String bounds) {
        if (bounds == null || bounds.isBlank()) {
            return null;
        }
        Matcher matcher = Pattern.compile("\\[(\\d+),(\\d+)]\\[(\\d+),(\\d+)]").matcher(bounds);
        if (!matcher.find()) {
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
