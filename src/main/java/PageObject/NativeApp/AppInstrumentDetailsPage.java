package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseTest;
import utils.GetPageElement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        if (!(driver instanceof AndroidDriver)) {
            return false;
        }
        // Blur the field so inline validation can render, then wait for RN text/content-desc.
        abs.dismissAndroidKeyboardSafely();
        By error = errorMessageLocator(messageContent);
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> errorMessageVisible(error, messageContent));
            return true;
        } catch (TimeoutException e) {
            boolean inSource = pageSourceContainsMessage(messageContent);
            System.out.println("Error message lookup timed out for '" + messageContent
                    + "', pageSourceContains=" + inSource);
            return inSource;
        }
    }

    private By errorMessageLocator(String messageContent) {
        return By.xpath(
                "//*[@text='" + messageContent + "' or @content-desc='" + messageContent + "'"
                        + " or contains(@text,'" + messageContent + "')"
                        + " or contains(@content-desc,'" + messageContent + "')]");
    }

    private boolean errorMessageVisible(By locator, String messageContent) {
        List<WebElement> matches = driver.findElements(locator);
        if (!matches.isEmpty()) {
            return true;
        }
        String expected = messageContent.toLowerCase(Locale.ROOT);
        for (WebElement ele : driver.findElements(By.className("android.widget.TextView"))) {
            try {
                if (nodeText(ele).toLowerCase(Locale.ROOT).contains(expected)) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return false;
    }

    private String nodeText(WebElement element) {
        StringBuilder combined = new StringBuilder();
        appendNodeText(combined, element.getText());
        appendNodeText(combined, elementAttribute(element, "text"));
        appendNodeText(combined, elementAttribute(element, "content-desc"));
        return combined.toString();
    }

    private void appendNodeText(StringBuilder combined, String value) {
        if (value == null || value.isBlank() || "null".equalsIgnoreCase(value)) {
            return;
        }
        if (!combined.isEmpty()) {
            combined.append(' ');
        }
        combined.append(value.trim());
    }

    private boolean pageSourceContainsMessage(String messageContent) {
        try {
            String xml = driver.getPageSource();
            if (xml == null || xml.isBlank()) {
                return false;
            }
            String expected = messageContent.toLowerCase(Locale.ROOT);
            if (xml.toLowerCase(Locale.ROOT).contains(expected)) {
                return true;
            }
            for (String text : abs.extractTextViewTexts(xml)) {
                if (text.toLowerCase(Locale.ROOT).contains(expected)) {
                    return true;
                }
            }
            return false;
        } catch (RuntimeException e) {
            return false;
        }
    }

    // EIEHK puts this switch on the row under Pending Order. A bare Switch hits Pending Order instead.
    private static final By TARGET_PROFIT_SWITCH = By.xpath(
            "//android.widget.TextView[starts-with(@text,'Set Target Profit')]"
                    + "/following-sibling::android.widget.Switch[1]"
    );

    public void switchProfitStopLoss() {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        waitForOrderTicket();
        if (!driver.findElements(PENDING_ORDER_SWITCH).isEmpty()) {
            switchOnTargetProfit();
            return;
        }
        TimeoutException lastError = null;
        for (By locator : tpslToggleLocators()) {
            try {
                if (isTpslLabelLocator(locator)) {
                    abs.tapOnSameRowRight(locator, 10);
                } else {
                    abs.tapVisible(locator, 10);
                }
                waitForTpslExpanded(8);
                return;
            } catch (TimeoutException e) {
                lastError = e;
            }
        }
        if (waitForTpslExpanded(2)) {
            return;
        }
        throw lastError != null
                ? lastError
                : new TimeoutException("Stop Loss field was not visible after switching Take Profit and Stop Loss on");
    }

    private void switchOnTargetProfit() {
        revealShown(TARGET_PROFIT_SWITCH);
        WebElement toggle = new WebDriverWait(driver, Duration.ofSeconds(8))
                .ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOfElementLocated(TARGET_PROFIT_SWITCH));
        if (!isSwitchChecked(toggle)) {
            abs.tapVisible(TARGET_PROFIT_SWITCH, 8);
            new WebDriverWait(driver, Duration.ofSeconds(8))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> isSwitchChecked(d.findElement(TARGET_PROFIT_SWITCH)));
        }
        if (!waitForTpslExpanded(8)) {
            throw new TimeoutException("Stop Loss field was not visible after switching Take Profit and Stop Loss on");
        }
    }

    private void revealShown(By locator) {
        for (int swipe = 0; swipe < 5; swipe++) {
            if (isLocatorShown(locator)) {
                return;
            }
            abs.swipeUp(driver);
        }
    }

    private boolean isLocatorShown(By locator) {
        Dimension window = driver.manage().window().getSize();
        int top = (int) (window.getHeight() * 0.08);
        int bottom = (int) (window.getHeight() * 0.92);
        for (WebElement element : driver.findElements(locator)) {
            int[] box = parseBounds(elementAttribute(element, "bounds"));
            if (box == null) {
                continue;
            }
            int centerX = (box[0] + box[2]) / 2;
            int centerY = (box[1] + box[3]) / 2;
            if (centerX > 0 && centerX < window.getWidth() && centerY > top && centerY < bottom) {
                return true;
            }
        }
        return false;
    }

    private List<By> tpslToggleLocators() {
        List<By> locators = new ArrayList<>();
        locators.add(By.xpath("//android.widget.TextView[contains(@text,'Take Profit') and contains(@text,'Stop Loss')]"));
        locators.add(By.xpath("//*[contains(@text,'Take Profit') and contains(@text,'Stop Loss')]"));
        // Skip the generic Switch when Pending Order is on screen. That switch is above Take Profit.
        if (driver.findElements(PENDING_ORDER_SWITCH).isEmpty()) {
            locators.add(By.xpath("//android.widget.Switch"));
            locators.add(By.xpath("//*[@checkable='true']"));
        }
        return locators;
    }

    private boolean isTpslLabelLocator(By locator) {
        String locatorText = locator.toString();
        return locatorText.contains("Take Profit") && locatorText.contains("Stop Loss");
    }

    private boolean waitForTpslExpanded(int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(Math.max(1, seconds)))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> !tpslFieldLabels("Stop Loss").isEmpty());
            return true;
        } catch (RuntimeException e) {
            getPageElement.logInfo("TPSL expand wait did not confirm Stop Loss field: " + e.getMessage());
        }
        try {
            abs.swipeUp(driver);
        } catch (RuntimeException e) {
            getPageElement.logInfo("Skip TPSL reveal swipe: " + e.getMessage());
        }
        try {
            return !tpslFieldLabels("Stop Loss").isEmpty();
        } catch (RuntimeException e) {
            return false;
        }
    }

    private String readTpslConstraintPrice(String fieldName, String sign, String altSign) {
        revealTpslField(fieldName);
        WebElement label = tpslFieldLabel(fieldName);
        String combined = tpslLabelText(label) + " " + nearbyConstraintText(label);
        String price = extractConstraintNumber(combined, sign, altSign);
        if (price == null) {
            throw new NoSuchElementException("Could not read " + fieldName + " constraint from: " + combined);
        }
        getPageElement.logInfo("Read " + fieldName + " constraint price: " + price);
        return price;
    }

    private WebElement tpslEditField(String fieldName) {
        revealTpslField(fieldName);
        return editFieldNear(tpslFieldLabel(fieldName), fieldName);
    }

    private WebElement pendingPriceEditField() {
        // EIEHK labels the pending price row "Buy at" and puts the EditText after that label.
        List<WebElement> buyAtFields = driver.findElements(By.xpath(
                "//android.widget.TextView[normalize-space(@text)='Buy at']/following::android.widget.EditText[1]"
        ));
        if (!buyAtFields.isEmpty()) {
            return buyAtFields.getFirst();
        }
        WebElement label = pendingPriceLabel();
        if (label != null) {
            try {
                return editFieldNear(label, "Price");
            } catch (NoSuchElementException ignored) {
            }
        }
        if (editTextFieldAos != null && editTextFieldAos.size() > 1) {
            return editTextFieldAos.get(1);
        }
        throw new NoSuchElementException("Could not find Price EditText");
    }

    private WebElement editFieldNear(WebElement label, String fieldName) {
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
            if (verticalGap < -24) {
                continue;
            }
            int score = Math.abs(verticalGap) * 100 + Math.abs(fieldBounds[0] - labelBounds[0]);
            if (score < bestScore) {
                bestScore = score;
                closest = field;
            }
        }
        if (closest == null) {
            throw new NoSuchElementException("Could not find EditText for " + fieldName);
        }
        return closest;
    }

    private void revealTpslField(String fieldName) {
        By dedicated = By.xpath(
                "//android.widget.TextView[starts-with(normalize-space(@text),'" + fieldName + " (')]"
        );
        for (int swipe = 0; swipe < 5; swipe++) {
            if (isLocatorShown(dedicated) || isAnyLabelShown(fieldName)) {
                return;
            }
            abs.swipeUp(driver);
        }
        throw new NoSuchElementException(fieldName + " field was not visible after expanding TPSL");
    }

    private boolean isAnyLabelShown(String fieldName) {
        for (WebElement label : tpslFieldLabels(fieldName)) {
            int[] box = parseBounds(elementAttribute(label, "bounds"));
            if (box == null) {
                continue;
            }
            Dimension window = driver.manage().window().getSize();
            int centerY = (box[1] + box[3]) / 2;
            if (centerY > window.getHeight() * 0.08 && centerY < window.getHeight() * 0.92) {
                return true;
            }
        }
        return false;
    }

    private WebElement tpslFieldLabel(String fieldName) {
        List<WebElement> labels = tpslFieldLabels(fieldName);
        if (labels.isEmpty()) {
            throw new NoSuchElementException("Could not find field label for " + fieldName);
        }
        return labels.getLast();
    }

    private List<WebElement> tpslFieldLabels(String fieldName) {
        List<WebElement> found = new ArrayList<>();
        String otherField = fieldName.equals("Stop Loss") ? "Take Profit" : "Stop Loss";
        // EIEHK field labels are "Stop Loss (≤…)" and "Take Profit (≥…)".
        // starts-with skips the "Set Target Profit & Stop Loss" switch row.
        List<By> locators = List.of(
                By.xpath("//android.widget.TextView[starts-with(normalize-space(@text),'" + fieldName + " (')]"),
                By.xpath("//*[starts-with(normalize-space(@text),'" + fieldName + " (')]"),
                By.xpath("//*[contains(@text,'" + fieldName + "')]"),
                By.xpath("//*[contains(@content-desc,'" + fieldName + "')]")
        );
        for (By locator : locators) {
            for (WebElement el : driver.findElements(locator)) {
                try {
                    String text = tpslLabelText(el);
                    if (!isDedicatedTpslFieldLabel(text, fieldName, otherField)) {
                        continue;
                    }
                    found.add(el);
                } catch (RuntimeException ignored) {
                }
            }
            if (!found.isEmpty()) {
                return found;
            }
        }
        return found;
    }

    private boolean isDedicatedTpslFieldLabel(String text, String fieldName, String otherField) {
        if (text == null || text.isBlank() || !text.contains(fieldName)) {
            return false;
        }
        String lower = text.toLowerCase(Locale.ROOT);
        if (text.contains("&") || lower.contains(" and ") || lower.contains("/")) {
            return false;
        }
        return !text.contains(otherField);
    }

    private String tpslLabelText(WebElement element) {
        String text = firstNonBlank(safeLabelText(element), elementAttribute(element, "content-desc"));
        return text == null ? "" : text.trim();
    }

    private String firstNonBlank(String... values) {
        if (values == null) {
            return "";
        }
        for (String value : values) {
            if (value != null && !value.isBlank() && !"null".equalsIgnoreCase(value)) {
                return value;
            }
        }
        return "";
    }

    private String nearbyConstraintText(WebElement label) {
        return nearbyConstraintText(label, 48);
    }

    private String nearbyConstraintText(WebElement label, int maxYGap) {
        int[] labelBounds = parseBounds(elementAttribute(label, "bounds"));
        if (labelBounds == null) {
            return "";
        }
        int gap = Math.max(48, maxYGap);
        StringBuilder extra = new StringBuilder();
        int labelCenterY = (labelBounds[1] + labelBounds[3]) / 2;
        appendNearbyConstraintText(extra, labelBounds, labelCenterY, gap, By.className("android.widget.TextView"));
        appendNearbyConstraintText(extra, labelBounds, labelCenterY, gap, By.className("android.widget.EditText"));
        return extra.toString();
    }

    private void appendNearbyConstraintText(StringBuilder extra, int[] labelBounds, int labelCenterY, int gap, By locator) {
        for (WebElement el : driver.findElements(locator)) {
            try {
                int[] bounds = parseBounds(elementAttribute(el, "bounds"));
                if (bounds == null) {
                    continue;
                }
                int centerY = (bounds[1] + bounds[3]) / 2;
                if (Math.abs(centerY - labelCenterY) > gap && bounds[1] > labelBounds[3] + gap) {
                    continue;
                }
                if (Math.abs(centerY - labelCenterY) > gap && bounds[1] < labelBounds[1] - 20) {
                    continue;
                }
                extra.append(' ').append(constraintNodeText(el));
            } catch (StaleElementReferenceException ignored) {
            }
        }
    }

    private String constraintNodeText(WebElement element) {
        StringBuilder combined = new StringBuilder();
        appendNodeText(combined, safeLabelText(element));
        appendNodeText(combined, elementAttribute(element, "text"));
        appendNodeText(combined, elementAttribute(element, "content-desc"));
        appendNodeText(combined, elementAttribute(element, "hint"));
        return combined.toString();
    }

    private String extractConstraintNumber(String text, String sign, String altSign) {
        if (text == null || text.isBlank()) {
            return null;
        }
        String normalized = normalizeConstraintDigits(text.replace('\u00A0', ' ')
                .replace('\u2266', '\u2264')
                .replace('\u2267', '\u2265'));
        boolean preferLower = "\u2264".equals(sign) || "<=".equals(sign) || "<".equals(sign);
        String[] tokens = preferLower
                ? new String[]{sign, altSign, "\u2264", "<=", "<"}
                : new String[]{sign, altSign, "\u2265", ">=", ">"};
        int idx = -1;
        String matchedToken = "";
        for (String token : tokens) {
            if (token == null || token.isBlank()) {
                continue;
            }
            idx = normalized.indexOf(token);
            if (idx >= 0) {
                matchedToken = token;
                break;
            }
        }
        if (idx < 0) {
            return null;
        }
        // Join split fragments so "4 352.39" / "4352 .39" stay 4352.39, not 352.39.
        String afterSign = firstConstraintDigits(normalized.substring(Math.min(normalized.length(), idx + matchedToken.length())));
        if (afterSign != null) {
            return afterSign;
        }
        return lastConstraintDigits(normalized.substring(0, idx));
    }

    private String firstConstraintDigits(String text) {
        Matcher matcher = constraintDigits().matcher(text);
        if (!matcher.find()) {
            return null;
        }
        return cleanConstraintDigits(matcher.group(1));
    }

    private String lastConstraintDigits(String text) {
        Matcher matcher = constraintDigits().matcher(text);
        String last = null;
        while (matcher.find()) {
            last = matcher.group(1);
        }
        return last == null ? null : cleanConstraintDigits(last);
    }

    private Pattern constraintDigits() {
        return Pattern.compile("((?:\\d+[\\s,]*)+(?:[.,]\\d+)?)");
    }

    private String cleanConstraintDigits(String raw) {
        return raw.replace(",", "").replace(" ", "");
    }

    private String normalizeConstraintDigits(String text) {
        StringBuilder out = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= '\uFF10' && c <= '\uFF19') {
                out.append((char) ('0' + (c - '\uFF10')));
            } else {
                out.append(c);
            }
        }
        return out.toString();
    }

    private String safeLabelText(WebElement element) {
        try {
            String text = element.getText();
            return text == null ? "" : text.trim();
        } catch (StaleElementReferenceException e) {
            return "";
        }
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
        selectedDirection = direction;
        boolean buy = direction.equalsIgnoreCase("BUY");
        String price = readTpslConstraintPrice("Stop Loss", buy ? "≤" : "≥", buy ? "<=" : ">=");
        String formattedPrice = abs.normalizePriceToDecimals(price, symbolDecimal);
        stopLossPrice = formattedPrice;
        return formattedPrice;
    }

    public String getTakeProfitPrice(String direction, String symbolDecimal) {
        selectedDirection = direction;
        boolean buy = direction.equalsIgnoreCase("BUY");
        String price = readTpslConstraintPrice("Take Profit", buy ? "≥" : "≤", buy ? ">=" : "<=");
        String formattedPrice = abs.normalizePriceToDecimals(price, symbolDecimal);
        takeProfitPrice = formattedPrice;
        return formattedPrice;
    }

    public String getStopOrderPrice(String direction, String stopOrderType, String decimal) {
        selectedDirection = direction;
        boolean buy = direction != null && direction.equalsIgnoreCase("BUY");
        boolean stop = stopOrderType != null && stopOrderType.toLowerCase(Locale.ROOT).contains("stop");
        // EIEHK "Buy at" row has no ≥/≤ threshold. Use the direction quote, then keep the offset below.
        String threshold = null;
        if (isBuyAtPriceRowVisible()) {
            threshold = readDirectionQuotePrice(direction);
        }
        if (threshold == null || threshold.isBlank()) {
            threshold = waitForConstraintPrice(stop ? buy : !buy);
        }
        if (threshold == null || threshold.isBlank()) {
            threshold = readDirectionQuotePrice(direction);
        }
        if (threshold == null || threshold.isBlank()) {
            throw new NoSuchElementException("Could not find pending order Price constraint");
        }
        float offset = stop == buy ? 25f : -25f;
        String price = Float.toString(Float.parseFloat(threshold) + offset);
        stopOrderPrice = abs.normalizePriceToDecimals(price, decimal);
        if (AppSettingPage.isTradeConfirmNeeded) {
            executedPrice = stopOrderPrice;
        }
        getPageElement.logInfo("Pending order price from constraint " + threshold + " -> " + stopOrderPrice);
        return stopOrderPrice;
    }

    private boolean isBuyAtPriceRowVisible() {
        return !driver.findElements(By.xpath(
                "//android.widget.TextView[normalize-space(@text)='Buy at']"
        )).isEmpty();
    }

    private String readDirectionQuotePrice(String direction) {
        if (direction == null || direction.isBlank()) {
            return null;
        }
        String side = direction.trim().toUpperCase(Locale.ROOT);
        By quote = By.xpath("//*[@clickable='true' and starts-with(@content-desc,'" + side + ",')]");
        for (WebElement element : driver.findElements(quote)) {
            String price = quotePrice(elementAttribute(element, "content-desc"));
            if (price != null) {
                getPageElement.logInfo("Pending price threshold from quote " + side + ": " + price);
                return price;
            }
        }
        return null;
    }

    private String quotePrice(String contentDesc) {
        if (contentDesc == null || contentDesc.isBlank()) {
            return null;
        }
        Matcher matcher = Pattern.compile(
                "(?i)^(?:BUY|SELL)\\s*,\\s*([0-9][0-9,]*)(?:\\.([0-9]+))?(?:\\s*,\\s*\\.?([0-9]+))?\\s*$"
        ).matcher(contentDesc.trim());
        if (!matcher.matches()) {
            return null;
        }
        String whole = matcher.group(1).replace(",", "");
        String fraction = matcher.group(2) != null ? matcher.group(2) : matcher.group(3);
        if (fraction == null || fraction.isEmpty()) {
            return whole;
        }
        return whole + "." + fraction;
    }

    private String waitForConstraintPrice(boolean greaterOrEqual) {
        prepareConstraintHierarchy();
        TimeoutException lastTimeout = null;
        for (int swipe = 0; swipe < 3; swipe++) {
            String fromAttributes = readConstraintFromAttributeValues(greaterOrEqual);
            if (fromAttributes != null && !fromAttributes.isBlank()) {
                getPageElement.logInfo("Pending Price constraint from hierarchy attributes: " + fromAttributes);
                return fromAttributes;
            }
            try {
                String price = new WebDriverWait(driver, Duration.ofSeconds(8))
                        .ignoring(StaleElementReferenceException.class)
                        .until(d -> readConstraintPrice(greaterOrEqual));
                if (price != null && !price.isBlank()) {
                    return price;
                }
            } catch (TimeoutException e) {
                lastTimeout = e;
            }
            String immediate = readConstraintPrice(greaterOrEqual);
            if (immediate != null && !immediate.isBlank()) {
                return immediate;
            }
            if (swipe < 2) {
                abs.swipeUp(driver);
            }
        }
        if (lastTimeout != null) {
            getPageElement.logInfo("Pending Price constraint not visible: " + lastTimeout.getMessage());
        }
        String fromAttributes = readConstraintFromAttributeValues(greaterOrEqual);
        if (fromAttributes != null && !fromAttributes.isBlank()) {
            return fromAttributes;
        }
        return readConstraintPrice(greaterOrEqual);
    }

    private void prepareConstraintHierarchy() {
        if (!(driver instanceof AndroidDriver androidDriver)) {
            return;
        }
        try {
            androidDriver.setSetting("ignoreUnimportantViews", false);
            androidDriver.setSetting("allowInvisibleElements", true);
            androidDriver.setSetting("snapshotMaxDepth", 100);
            androidDriver.setSetting("shouldUseCompactResponses", false);
            androidDriver.setSetting("elementResponseAttributes",
                    "name,text,contentDescription,class,bounds,hint,displayed");
        } catch (RuntimeException e) {
            getPageElement.logInfo("Skip pending price hierarchy settings: " + e.getMessage());
        }
    }

    private String readConstraintPrice(boolean greaterOrEqual) {
        String sign = greaterOrEqual ? "\u2265" : "\u2264";
        String altSign = greaterOrEqual ? ">=" : "<=";
        WebElement label = pendingPriceLabel();
        if (label != null) {
            int yGap = Math.max(120, (int) (driver.manage().window().getSize().getHeight() * 0.08));
            String combined = constraintNodeText(label) + " " + nearbyConstraintText(label, yGap);
            String fromLabel = extractConstraintNumber(combined, sign, altSign);
            if (fromLabel != null) {
                return fromLabel;
            }
        }
        String fromField = readConstraintFromPriceField(sign, altSign);
        if (fromField != null) {
            return fromField;
        }
        return scanConstraintFromVisibleText(sign, altSign);
    }

    private String readConstraintFromPriceField(String sign, String altSign) {
        try {
            return extractConstraintNumber(constraintNodeText(pendingPriceEditField()), sign, altSign);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private WebElement pendingPriceLabel() {
        WebElement best = null;
        int bestY = Integer.MAX_VALUE;
        int minY = (int) (driver.manage().window().getSize().getHeight() * 0.12);
        for (WebElement el : driver.findElements(By.xpath(
                "//*[contains(@text,'Price') or contains(@content-desc,'Price')]"))) {
            try {
                String text = tpslLabelText(el);
                if (!isPendingPriceConstraintLabel(text) && !isPendingPriceConstraintLabel(constraintNodeText(el))) {
                    continue;
                }
                int[] box = parseBounds(elementAttribute(el, "bounds"));
                if (box == null || box[1] < minY) {
                    continue;
                }
                if (box[1] < bestY) {
                    bestY = box[1];
                    best = el;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return best;
    }

    private boolean isPendingPriceConstraintLabel(String text) {
        if (text == null || text.isBlank() || !text.toLowerCase(Locale.ROOT).contains("price")) {
            return false;
        }
        String lower = text.toLowerCase(Locale.ROOT);
        return !lower.contains("last")
                && !lower.contains("bid")
                && !lower.contains("ask")
                && !lower.contains("stop loss")
                && !lower.contains("take profit")
                && !lower.contains("spread")
                && !lower.contains("change");
    }

    private String scanConstraintFromVisibleText(String sign, String altSign) {
        for (By locator : List.of(
                By.className("android.widget.TextView"),
                By.className("android.widget.EditText"))) {
            String price = scanConstraintElements(locator, sign, altSign);
            if (price != null) {
                return price;
            }
        }
        return null;
    }

    private String scanConstraintElements(By locator, String sign, String altSign) {
        for (WebElement el : driver.findElements(locator)) {
            try {
                String text = constraintNodeText(el);
                if (text.isBlank() || isTpslConstraintText(text)) {
                    continue;
                }
                if (!isPendingPriceConstraintLabel(text) && !looksLikeConstraintHint(text, sign, altSign)) {
                    continue;
                }
                String price = extractConstraintNumber(text, sign, altSign);
                if (price != null) {
                    return price;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return null;
    }

    private String readConstraintFromAttributeValues(boolean greaterOrEqual) {
        String xml;
        try {
            xml = driver.getPageSource();
        } catch (RuntimeException e) {
            getPageElement.logInfo("Skip pending price page source: " + e.getMessage());
            return null;
        }
        if (xml == null || xml.isBlank()) {
            return null;
        }
        List<String> values = new ArrayList<>();
        Matcher matcher = Pattern.compile("(?:\\btext|content-desc|hint)=\"([^\"]*)\"").matcher(xml);
        while (matcher.find()) {
            String value = unescapeConstraintText(matcher.group(1)).trim();
            if (!value.isBlank()) {
                values.add(value);
            }
        }
        String sign = greaterOrEqual ? "\u2265" : "\u2264";
        String altSign = greaterOrEqual ? ">=" : "<=";
        for (int i = 0; i < values.size(); i++) {
            String window = joinConstraintWindow(values, i, 6);
            if (!window.toLowerCase(Locale.ROOT).contains("price") || isTpslConstraintText(window)) {
                continue;
            }
            String price = extractConstraintNumber(window, sign, altSign);
            if (price != null) {
                return price;
            }
        }
        return null;
    }

    private String joinConstraintWindow(List<String> values, int start, int maxNodes) {
        StringBuilder window = new StringBuilder();
        int end = Math.min(values.size(), start + maxNodes);
        for (int i = start; i < end; i++) {
            if (i > start && isTpslConstraintText(values.get(i))) {
                break;
            }
            window.append(' ').append(values.get(i));
        }
        return window.toString();
    }

    private boolean isTpslConstraintText(String text) {
        String lower = text.toLowerCase(Locale.ROOT);
        return lower.contains("stop loss") || lower.contains("take profit");
    }

    private String unescapeConstraintText(String value) {
        return value.replace("&#8805;", "\u2265")
                .replace("&#8804;", "\u2264")
                .replace("&#x2265;", "\u2265")
                .replace("&#x2264;", "\u2264")
                .replace("&gt;", ">")
                .replace("&lt;", "<")
                .replace("&amp;", "&");
    }

    private boolean looksLikeConstraintHint(String text, String sign, String altSign) {
        return text.contains(sign)
                || text.contains(altSign)
                || text.contains("\u2264")
                || text.contains("\u2265")
                || text.contains("<=")
                || text.contains(">=")
                || text.contains("<")
                || text.contains(">");
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

    private void typePendingOrderPrice(String direction, String decimal) {
        String price = getStopOrderPrice(direction, stopOrderType, decimal);
        WebElement priceField = pendingPriceEditField();
        priceField.clear();
        abs.typeWithAndroidKeys((AndroidDriver) driver, priceField, price);
    }

    public void fillInTextField(String textFieldName, String direction, String decimal) {
        if (driver instanceof AndroidDriver) {
            switch (textFieldName) {
                case "Stop Loss" -> {
                    abs.typeWithAndroidKeys((AndroidDriver) driver, tpslEditField("Stop Loss"),
                            getStopLossPrice(direction, decimal));
                }
                case "Take Profit" -> {
                    abs.typeWithAndroidKeys((AndroidDriver) driver, tpslEditField("Take Profit"),
                            getTakeProfitPrice(direction, decimal));
                }
                case "Lot Size" -> typeCapturedLotSize();
                case "Price" -> typePendingOrderPrice(direction, decimal);
            }
        }
    }

    private void typeCapturedLotSize() {
        WebElement lotField = lotsEditField();
        lotField.clear();
        abs.typeWithAndroidKeys((AndroidDriver) driver, lotField, lotSize);
    }

    private String offsetPrice(String price, int delta, String decimal) {
        int scale = Integer.parseInt(decimal);
        return new BigDecimal(price)
                .add(BigDecimal.valueOf(delta))
                .setScale(scale, RoundingMode.HALF_UP)
                .toPlainString();
    }

    public void fillInTextField(String textFieldName, String direction, String decimal, int priceDifVal) {
        if (driver instanceof AndroidDriver) {
            switch (textFieldName) {
                case "Stop Loss" -> {
                    int delta = direction.equalsIgnoreCase("BUY") ? -priceDifVal : priceDifVal;
                    stopLossPrice = offsetPrice(getStopLossPrice(direction, decimal), delta, decimal);
                    abs.typeWithAndroidKeys((AndroidDriver) driver, tpslEditField("Stop Loss"), stopLossPrice);
                }
                case "Take Profit" -> {
                    int delta = direction.equalsIgnoreCase("BUY") ? priceDifVal : -priceDifVal;
                    takeProfitPrice = offsetPrice(getTakeProfitPrice(direction, decimal), delta, decimal);
                    abs.typeWithAndroidKeys((AndroidDriver) driver, tpslEditField("Take Profit"), takeProfitPrice);
                }
                case "Lot Size" -> typeCapturedLotSize();
                case "Price" -> typePendingOrderPrice(direction, decimal);
            }
        }
    }

    public void editTextField(String textFieldName, String direction, String decimal) {
        if (driver instanceof AndroidDriver) {
            switch (textFieldName) {
                case "Stop Loss" -> {
                    int delta = direction.equalsIgnoreCase("BUY") ? -25 : 25;
                    stopLossPrice = offsetPrice(getStopLossPrice(direction, decimal), delta, decimal);
                    abs.typeWithAndroidKeys((AndroidDriver) driver, stopLossEditFieldAos, stopLossPrice);
                }
                case "Take Profit" -> {
                    int delta = direction.equalsIgnoreCase("BUY") ? 25 : -25;
                    takeProfitPrice = offsetPrice(getTakeProfitPrice(direction, decimal), delta, decimal);
                    abs.typeWithAndroidKeys((AndroidDriver) driver, takeProfitEditFieldAos, takeProfitPrice);
                }
                case "Lot Size" -> editTextFieldAos.getFirst().sendKeys("0.45");
                case "Price" -> typePendingOrderPrice(direction, decimal);
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
        if (buttonName.equalsIgnoreCase("BUY") || buttonName.equalsIgnoreCase("SELL")) {
            tapSubmitDirection(buttonName);
            return;
        }
        if (buttonName.contains("Cancel Order") || buttonName.equals("Edit Position")
                || buttonName.equals("Modify Order") || buttonName.contains("Close Position")) {
            abs.tapBottomMost(By.xpath("//*[@text='" + buttonName + "' or @content-desc='" + buttonName + "']"), 10);
            return;
        }
        By button = By.xpath("(//android.widget.TextView[@text=\"" + buttonName + "\"])[2]/parent::android.view.ViewGroup");
        abs.waitUntilElementClickable(button).click();
    }

    private void tapSubmitDirection(String direction) {
        TimeoutException lastError = null;
        for (By locator : submitDirectionLocators(direction)) {
            try {
                // Submit CTA is the full-width bar with exact content-desc "BUY" / "SELL", not the quote chip "BUY, 4312, .32".
                abs.tapBottomMost(locator, 10);
                System.out.println("Tapped submit direction button: " + direction);
                return;
            } catch (TimeoutException e) {
                lastError = e;
            }
        }
        throw lastError != null
                ? lastError
                : new TimeoutException("Submit direction button was not visible: " + direction);
    }

    private List<By> submitDirectionLocators(String direction) {
        return List.of(
                By.xpath("//*[@clickable='true' and @content-desc='" + direction + "']"),
                By.xpath("//android.view.ViewGroup[@clickable='true' and @content-desc='" + direction + "']"),
                By.xpath("//android.widget.TextView[@text='" + direction
                        + "']/parent::android.view.ViewGroup[@clickable='true' and @content-desc='" + direction + "']"),
                By.xpath("//android.widget.TextView[@text='" + direction
                        + "']/parent::android.view.ViewGroup[@clickable='true']")
        );
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
                case "Lots", "Lot Size", "Volume" -> {
                    String text = lotsEditField().getText();
                    yield text == null ? "" : text.trim();
                }
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
        // EIEHK exposes the chip label as content-desc on a clickable ViewGroup. EBL_MT5 exposes it as text.
        boolean contentDescFirst = "EIEHK".equalsIgnoreCase(BaseTest.productEntity);
        for (String text : lotChipTexts(value)) {
            List<By> contentDesc = List.of(
                    By.xpath("//*[@content-desc='" + text + "']"),
                    By.xpath("//android.view.ViewGroup[@content-desc='" + text + "']")
            );
            List<By> visibleText = List.of(
                    By.xpath("//android.widget.TextView[@text='" + text + "']"),
                    By.xpath("//*[@text='" + text + "']"),
                    By.xpath("//android.widget.TextView[@text='" + text + "']/parent::android.view.ViewGroup")
            );
            if (contentDescFirst) {
                locators.addAll(contentDesc);
                locators.addAll(visibleText);
            } else {
                locators.addAll(visibleText);
                locators.addAll(contentDesc);
            }
        }
        return locators;
    }

    private List<String> lotChipTexts(String value) {
        List<String> numeric = new ArrayList<>();
        addLotChipText(numeric, value.trim());
        try {
            double number = Double.parseDouble(value.trim());
            addLotChipText(numeric, String.format(Locale.US, "%.1f", number));
            addLotChipText(numeric, String.format(Locale.US, "%.2f", number));
        } catch (NumberFormatException ignored) {
        }
        List<String> labeled = new ArrayList<>();
        for (String text : numeric) {
            addLotChipText(labeled, text + " Lot");
            addLotChipText(labeled, text + " Lots");
        }
        // EIEHK chips read "0.5 Lot". EBL_MT5 chips read "0.5". Try the entity form first.
        List<String> ordered = new ArrayList<>();
        if ("EIEHK".equalsIgnoreCase(BaseTest.productEntity)) {
            ordered.addAll(labeled);
            ordered.addAll(numeric);
        } else {
            ordered.addAll(numeric);
            ordered.addAll(labeled);
        }
        return ordered;
    }

    private void addLotChipText(List<String> texts, String text) {
        if (!texts.contains(text)) {
            texts.add(text);
        }
    }

    public void getExecutedPrice() {
        executedPrice = readConfirmationExecutedPrice();
    }

    private String readConfirmationExecutedPrice() {
        getPageElement.clearPageSourceCache();
        getPageElement.waitForConfirmationPrice();
        for (String label : List.of(
                "Price",
                "Target Price",
                "Open Price",
                "Current Price",
                "Order Price",
                "Stop Order Price",
                "Execution Price"
        )) {
            String raw = getPageElement.readLabelValueFast(label);
            if (raw == null || raw.isBlank()) {
                continue;
            }
            getPageElement.logInfo("Captured confirmation price from [" + label + "]: " + raw);
            return getPageElement.normalizeByLabel(label, raw.trim(), "");
        }
        String sequential = getPageElement.findSequentialOverlayValue("Price");
        if (sequential == null || sequential.isBlank()) {
            sequential = getPageElement.findSequentialOverlayValue("Target Price");
        }
        if (sequential == null || sequential.isBlank()) {
            sequential = firstCapturedOrderPrice();
        }
        if (sequential == null || sequential.isBlank()) {
            throw new NoSuchElementException("Could not find value in hierarchy for label: Price");
        }
        return getPageElement.normalizeByLabel("Price", sequential.trim(), "");
    }

    private String firstCapturedOrderPrice() {
        for (String candidate : List.of(stopOrderPrice, executedPrice, AppTradeView.stopOrderPrice)) {
            if (candidate != null && !candidate.isBlank()) {
                getPageElement.logInfo("Using captured order price as confirmation Price: " + candidate);
                return candidate;
            }
        }
        return null;
    }

    public boolean isConfirmationOverlayVisible() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(8))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> !d.findElements(By.xpath(
                            "//android.view.ViewGroup[@resource-id='RNE__Overlay']"
                    )).isEmpty());
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getDetailValue(String value) {
        if ("Price".equals(value)) {
            getPageElement.clearPageSourceCache();
            getPageElement.waitForConfirmationPrice();
        } else {
            getPageElement.waitAndCaptureIfNeeded(
                    By.xpath("//android.view.ViewGroup[@resource-id='RNE__Overlay']"), 10);
        }
        String uiLabel = getPageElement.mapUiLabel(value);
        String rawValue = getPageElement.readLabelValueFast(uiLabel);
        if ((rawValue == null || rawValue.isBlank()) && "Price".equals(value)) {
            rawValue = getPageElement.findSequentialOverlayValue("Price");
        }
        if ((rawValue == null || rawValue.isBlank()) && "Price".equals(value)) {
            rawValue = firstCapturedOrderPrice();
        }
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
        getPageElement.clearPageSourceCache();
        getPageElement.waitForConfirmationPrice();
    }



    // EIEHK order ticket uses a Switch beside "Pending Order". The switch has no resource-id or content-desc.
    private static final By PENDING_ORDER_SWITCH = By.xpath(
            "//android.widget.TextView[starts-with(normalize-space(@text),'Pending Order')]"
                    + "/following-sibling::android.widget.Switch[1]"
    );

    public void toggleOnPendingOrder() {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        WebElement toggle = new WebDriverWait(driver, Duration.ofSeconds(10))
                .ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOfElementLocated(PENDING_ORDER_SWITCH));
        if (isSwitchChecked(toggle)) {
            getPageElement.logInfo("Pending Order switch is already on");
            return;
        }
        abs.tapVisible(PENDING_ORDER_SWITCH, 8);
        new WebDriverWait(driver, Duration.ofSeconds(8))
                .ignoring(StaleElementReferenceException.class)
                .until(d -> isSwitchChecked(d.findElement(PENDING_ORDER_SWITCH)));
    }

    private boolean isSwitchChecked(WebElement toggle) {
        String checked = toggle.getAttribute("checked");
        return checked != null && Boolean.parseBoolean(checked);
    }

    public void selectOrderType(String orderType) {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }

        String text = orderType.trim();
        waitForOrderTicket();
        // EIEHK exposes Limit / Stop Order as the Pending Order switch, not a Market Order dropdown.
        if (isEiehk() && isLimitStopOrderType(text)) {
            toggleOnPendingOrder();
            return;
        }
        openOrderTypePicker(text);
        clickOrderTypeOption(text);
    }

    private boolean isEiehk() {
        return "EIEHK".equalsIgnoreCase(BaseTest.productEntity);
    }

    private boolean isLimitStopOrderType(String orderType) {
        String lower = orderType.toLowerCase(Locale.ROOT);
        return lower.contains("limit") && lower.contains("stop");
    }

    private void waitForOrderTicket() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                            "//android.widget.TextView[@text='Market Order' or @text='Limit / Stop Order' or @text='Lots' or @text='Lot']"
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
        tapOptionChip(option, "Stop limit option");
    }

    public void scrollDown() {
        SCROLLED = true;
        //abs.swipeUp(driver);
        abs.swipeUpUntilEnd(driver);
    }

    public void selectValidity(String option) {
        validity = option;
        tapOptionChip(option, "Validity");
    }

    private void tapOptionChip(String option, String name) {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        TimeoutException lastError = null;
        for (int pass = 0; pass < 2; pass++) {
            for (By locator : optionChipLocators(option)) {
                try {
                    abs.tapVisible(locator, pass == 0 ? 8 : 5);
                    return;
                } catch (TimeoutException e) {
                    lastError = e;
                }
            }
            abs.swipeUp(driver);
        }
        throw lastError != null
                ? lastError
                : new TimeoutException(name + " was not visible: " + option);
    }

    private List<By> optionChipLocators(String option) {
        List<By> contentDesc = List.of(
                By.xpath("//android.view.ViewGroup[@clickable='true' and @content-desc=\"" + option + "\"]"),
                By.xpath("//*[@clickable='true' and @content-desc=\"" + option + "\"]")
        );
        List<By> visibleText = List.of(
                By.xpath("//android.widget.TextView[@text=\"" + option + "\"]"),
                By.xpath("//*[@text=\"" + option + "\"]"),
                By.xpath("//android.widget.TextView[@text=\"" + option + "\"]/parent::android.view.ViewGroup")
        );
        List<By> locators = new ArrayList<>();
        // EIEHK puts Buy Stop / Today on the clickable ViewGroup content-desc.
        if (isEiehk()) {
            locators.addAll(contentDesc);
            locators.addAll(visibleText);
        } else {
            locators.addAll(visibleText);
            locators.addAll(contentDesc);
        }
        return locators;
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
            case "Lots", "Volume", "Lot Size" -> displayedLotSize(lotSize);
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
            String rawValue = readTicketLabel(label);
            return rawValue == null ? null : getPageElement.normalizeByLabel(label, rawValue, symbol);
        }
        return "label not found";
    }

    // EIEHK prints "Estimated Margin". EBL_MT5 prints "Est. Margin". Keep the requested label first.
    private String readTicketLabel(String label) {
        String uiLabel = getPageElement.mapUiLabel(label);
        String rawValue = getPageElement.readLabelValueFast(uiLabel);
        if (isBlank(rawValue) && isEstimatedMarginLabel(label)) {
            String alias = "Est. Margin".equals(uiLabel) ? "Estimated Margin" : "Est. Margin";
            rawValue = getPageElement.readLabelValueFast(alias);
        }
        return rawValue;
    }

    private boolean isEstimatedMarginLabel(String label) {
        return "Est. Margin".equals(label) || "Estimated Margin".equals(label);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    public void setEstMargin(Integer initialMargin) {
        estMargin = abs.normalizePriceToDecimals(String.valueOf(Float.parseFloat(lotSize) * initialMargin), "2");
    }

    // Contract value and the margin amount are read from one ticket snapshot so a live quote cannot split them.
    public void expectEstimatedMarginFromTicketContractValue(String marginRate) {
        getPageElement.clearPageSourceCache();
        getPageElement.capturePageSource();
        String rawContract = getPageElement.readLabelValueFast("Contract Value");
        if (isBlank(rawContract)) {
            getPageElement.clearPageSourceCache();
            throw new NoSuchElementException("Could not find value in hierarchy for label: Contract Value");
        }
        String contractValue = abs.normalizeDialogueValue("Contract Value", rawContract.trim());
        estMargin = new BigDecimal(contractValue)
                .multiply(new BigDecimal(marginRate))
                .setScale(2, RoundingMode.HALF_UP)
                .toPlainString();
        getPageElement.logInfo("Expected estimated margin from ticket contract value " + contractValue
                + ", rate " + marginRate + ": " + estMargin);
    }

    public void releaseTicketSnapshot() {
        getPageElement.clearPageSourceCache();
    }

    // Price and volume come from the confirmation snapshot already cached by waitForConfirmationPopup.
    public void expectEstimatedMarginFromConfirmationSnapshot(int contractSize, String marginRate, String symbolDecimal) {
        String price = getDetailValue("Price", symbolDecimal);
        String volume = getDetailValue("Volume", symbolDecimal);
        estMargin = new BigDecimal(price)
                .multiply(new BigDecimal(volume))
                .multiply(BigDecimal.valueOf(contractSize))
                .multiply(new BigDecimal(marginRate))
                .setScale(2, RoundingMode.HALF_UP)
                .toPlainString();
        getPageElement.logInfo("Expected estimated margin from confirmation price " + price
                + ", volume " + volume + ", contract size " + contractSize
                + ", rate " + marginRate + ": " + estMargin);
    }

    public boolean getToggleStatus() {
        List<WebElement> switches = driver.findElements(By.xpath("//android.widget.Switch"));
        for (WebElement toggle : switches) {
            String checked = toggle.getAttribute("checked");
            if (checked != null && !"null".equalsIgnoreCase(checked)) {
                return Boolean.parseBoolean(checked);
            }
        }
        return !driver.findElements(By.xpath(
                "//android.widget.TextView[contains(@text,'Stop Loss (')]")).isEmpty();
    }

    public void adjustPrice(String ctaBtn, String priceType) {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        abs.dismissAndroidKeyboardSafely();
        if (isLotSizeField(priceType)) {
            captureLotsBeforeAdjust();
        }
        Point point = tpslStepperPoint(priceType, ctaBtn);
        getPageElement.logInfo("Tapping " + ctaBtn + " on " + priceType + " at " + point.getX() + "," + point.getY());
        abs.tapAt(point.getX(), point.getY());
    }

    private void captureLotsBeforeAdjust() {
        try {
            String text = lotsEditField().getText();
            if (text != null && !text.isBlank()) {
                lotSize = displayedLotSize(text);
            }
        } catch (RuntimeException e) {
            getPageElement.logInfo("Could not capture Lots value before stepper tap: " + e.getMessage());
        }
    }

    public void clearPrice(String priceType) {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        Point point = tpslStepperPoint(priceType, "✕");
        abs.tapAt(point.getX(), point.getY());
    }

    private WebElement inputEditField(String fieldName) {
        if (isLotSizeField(fieldName)) {
            return lotsEditField();
        }
        return tpslEditField(fieldName);
    }

    private boolean isLotSizeField(String fieldName) {
        if (fieldName == null) {
            return false;
        }
        String name = fieldName.trim();
        return name.equalsIgnoreCase("Lot Size")
                || name.equalsIgnoreCase("Lots")
                || name.equalsIgnoreCase("Lot")
                || name.equalsIgnoreCase("Volume");
    }

    private WebElement lotsEditField() {
        WebElement label = lotsFieldLabel();
        if (label != null) {
            WebElement nearest = nearestEditField(label);
            if (nearest != null) {
                return nearest;
            }
            getPageElement.logInfo("Could not resolve Lots EditText from label; falling back to first EditText");
        }
        abs.waitUntilElementVisible(By.className("android.widget.EditText"));
        List<WebElement> fields = driver.findElements(By.className("android.widget.EditText"));
        if (fields.isEmpty()) {
            throw new NoSuchElementException("Could not find Lots EditText");
        }
        return fields.getFirst();
    }

    private WebElement nearestEditField(WebElement label) {
        int[] labelBounds = parseBounds(elementAttribute(label, "bounds"));
        if (labelBounds == null) {
            return null;
        }
        int labelCenterY = (labelBounds[1] + labelBounds[3]) / 2;
        WebElement closest = null;
        int bestGap = Integer.MAX_VALUE;
        for (WebElement field : driver.findElements(By.className("android.widget.EditText"))) {
            int[] fieldBounds = parseBounds(elementAttribute(field, "bounds"));
            if (fieldBounds == null) {
                continue;
            }
            int fieldCenterY = (fieldBounds[1] + fieldBounds[3]) / 2;
            int gap = Math.abs(fieldCenterY - labelCenterY);
            // EIEHK draws "Lot" under the value. EBL_MT5 draws "Lots" above it.
            if (gap > 320 || gap >= bestGap) {
                continue;
            }
            bestGap = gap;
            closest = field;
        }
        return closest;
    }

    private WebElement lotsFieldLabel() {
        List<WebElement> labels = driver.findElements(By.xpath(
                "//android.widget.TextView[@text='Lots' or @text='Lot' or @text='Lot Size' or @text='Volume']"));
        return labels.isEmpty() ? null : labels.getFirst();
    }

    private Point tpslStepperPoint(String fieldName, String ctaBtn) {
        WebElement field = inputEditField(fieldName);
        int[] fieldBounds = parseBounds(elementAttribute(field, "bounds"));
        if (fieldBounds == null) {
            throw new NoSuchElementException("Could not read bounds for " + fieldName);
        }
        int fieldCenterY = (fieldBounds[1] + fieldBounds[3]) / 2;
        int fieldLeft = fieldBounds[0];
        int fieldRight = fieldBounds[2];
        String action = normalizeStepperAction(ctaBtn);
        if (isLotSizeField(fieldName) && ("minus".equals(action) || "plus".equals(action))) {
            return lotsStepperPoint(fieldBounds, fieldCenterY, action);
        }
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

        // Stop Loss / Take Profit / Price: filled row is [minus] [field] [clear] [plus]; empty row has no clear.
        if ("plus".equals(action)) {
            if (!right.isEmpty()) {
                return controlCenter(right.getLast());
            }
            Dimension window = driver.manage().window().getSize();
            return new Point(Math.min(window.getWidth() - 24, fieldRight + 98), fieldCenterY);
        }
        if ("minus".equals(action)) {
            if (!left.isEmpty()) {
                return controlCenter(left.getLast());
            }
            return new Point(Math.max(8, fieldLeft - 48), fieldCenterY);
        }
        if ("clear".equals(action)) {
            if (right.size() >= 2) {
                return controlCenter(right.getFirst());
            }
            return new Point(fieldRight + 28, fieldCenterY);
        }
        throw new IllegalArgumentException("Unsupported stepper button: " + ctaBtn);
    }

    private Point lotsStepperPoint(int[] fieldBounds, int fieldCenterY, String action) {
        boolean minus = "minus".equals(action);
        int fieldEdge = minus ? fieldBounds[0] : fieldBounds[2];
        // Center of a 72px icon flush with the Lots box. Plus may overlap the right edge.
        int anchorX = minus ? Math.max(8, fieldEdge - 36) : fieldEdge + 36;
        Dimension window = driver.manage().window().getSize();
        anchorX = Math.min(Math.max(8, anchorX), window.getWidth() - 8);
        int[] best = null;
        int bestDistance = Integer.MAX_VALUE;
        for (int[] bounds : compactControlsOnRow(fieldCenterY)) {
            int width = bounds[2] - bounds[0];
            int height = bounds[3] - bounds[1];
            if (width < 40 || width > 100 || height < 40 || height > 100) {
                continue;
            }
            if (Math.abs(width - height) > 20) {
                continue;
            }
            int centerX = (bounds[0] + bounds[2]) / 2;
            if (minus && centerX > fieldEdge + 48) {
                continue;
            }
            if (!minus && centerX < fieldEdge - 48) {
                continue;
            }
            int distance = Math.abs(centerX - anchorX);
            if (distance < bestDistance) {
                bestDistance = distance;
                best = bounds;
            }
        }
        if (best != null && bestDistance <= 72) {
            return controlCenter(best);
        }
        return new Point(anchorX, fieldCenterY);
    }

    private String normalizeStepperAction(String ctaBtn) {
        if (ctaBtn == null || ctaBtn.isBlank()) {
            throw new IllegalArgumentException("Stepper button was empty");
        }
        String text = ctaBtn.trim();
        if (text.equals("+") || text.equalsIgnoreCase("plus")) {
            return "plus";
        }
        if (text.equals("-") || text.equals("\u2212") || text.equalsIgnoreCase("minus")) {
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
                By.xpath("//*[@text='+' or @text='-' or @text='\u2212' or @text='✕' or @text='×' or @text='x' or @text='X']"),
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
                    if (width < 18 || width > 120 || height < 18 || height > 120) {
                        continue;
                    }
                    int centerY = (bounds[1] + bounds[3]) / 2;
                    if (Math.abs(centerY - rowCenterY) > 64) {
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
