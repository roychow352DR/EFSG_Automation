package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
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

import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.GetPageElement;

import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppTradeView {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    private final GetPageElement getPageElement;
    public static String stopLossPrice;
    public static String stopOrderPrice;
    public static String stopOrderType;
    public static String takeProfitPrice;
    public static String selectedDirection;
    public static String lotSize;
    public static String executedPrice;
    public static String openPositionOpenPrice;
    public static String openOrderTargetPrice;
    public static String validity;
    public static String editPrice;
    public static int positionsCount = 0;
    public static int pendingOrdersCount = 0;

    public static void resetCapturedOrderValues() {
        stopLossPrice = null;
        stopOrderPrice = null;
        stopOrderType = null;
        takeProfitPrice = null;
        selectedDirection = null;
        lotSize = null;
        executedPrice = null;
        openPositionOpenPrice = null;
        openOrderTargetPrice = null;
        validity = null;
        editPrice = null;
        positionsCount = 0;
        pendingOrdersCount = 0;
    }

    public AppTradeView(AppiumDriver driver) {
        this.driver = driver;
        abs = new MobileAbstractComponents(driver);
        PageFactory.initElements(driver, this);
        this.getPageElement = new GetPageElement(driver);

    }

    @FindBy(xpath = "//android.widget.TextView[@text=\"BUY\"]/parent::android.view.ViewGroup")
    WebElement buyButtonAos;

    @FindBy(xpath = "//android.widget.TextView[@text=\"SELL\"]/parent::android.view.ViewGroup")
    WebElement sellButtonAos;


    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup")
    List<WebElement> rowsOnPositionTabAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.widget.TextView")
    List<WebElement> rowsOnPendingOrdersTabAos;

//    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup")
//    WebElement detailsButton;

    private final By detailsButton = By.xpath("//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup");

//    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[1]")
//    WebElement crossButtonAos;

    private final By crossButtonAos = By.xpath("//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[1]");

//    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[14]")
//    WebElement cancelOrderButtonAos;

    private final By cancelOrderButtonAos = By.xpath("//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[14]");

    @FindBy(xpath = "(//android.widget.TextView[@text=\"Close Position\"])[2]/parent::android.view.ViewGroup")
    WebElement closePositionAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[11]")
    WebElement confirmClosePositionBtnAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.widget.TextView")
    List<WebElement> dialogueTextAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup")
    WebElement editPositionButtonAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]" +
            "/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup/" +
            "android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.view.ViewGroup[2]")
    WebElement closePositionBtnInDetailsAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout" +
            "/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup" +
            "/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup")
    WebElement closePositionBtnWithLotsAos;


    private final By backBtnAos = By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout" +
            "/android.view.ViewGroup/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup" +
            "/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]");

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[15]")
    WebElement closeDialogueBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.widget.TextView")
    List<WebElement> openPositionRecordDetailsAos;


    private final By positionsTabAos = By.xpath("//android.widget.HorizontalScrollView/android.view.ViewGroup/android.view.View[2]");



    public void selectDirection(String direction) {
        selectedDirection = direction;
        resetLeftoverTpslValues();
        if (driver instanceof AndroidDriver) {
            switch (direction) {
                case "BUY" -> buyButtonAos.click();
                case "SELL" -> sellButtonAos.click();
            }
        }
    }

    private void resetLeftoverTpslValues() {
        AppInstrumentDetailsPage.stopOrderType = "";
        AppInstrumentDetailsPage.stopLossPrice = null;
        AppInstrumentDetailsPage.takeProfitPrice = null;
        AppInstrumentDetailsPage.stopOrderPrice = null;
        AppInstrumentDetailsPage.SCROLLED = false;
        stopLossPrice = null;
        takeProfitPrice = null;
        stopOrderType = null;
        stopOrderPrice = null;
    }


    public String getStopLossPrice(String direction, String symbolDecimal) {
        String price = "";
        WebElement text;
        selectedDirection = direction;
        switch (selectedDirection) {
            case "BUY" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Stop Loss (≤\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≤")[1].trim().split("\\)")[0]) - 25);
            }
            case "SELL" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Stop Loss (≥\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≥")[1].trim().split("\\)")[0]) + 25);
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
                price = Float.toString(Float.parseFloat(text.getText().split("≥")[1].trim().split("\\)")[0]) + 25);
            }
            case "SELL" -> {
                text = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,\"Take Profit (≤\")]"));
                price = Float.toString(Float.parseFloat(text.getText().split("≤")[1].trim().split("\\)")[0]) - 25);
            }
        }
        String formattedPrice = abs.normalizePriceToDecimals(price, symbolDecimal);
        takeProfitPrice = formattedPrice;
        return formattedPrice;
    }

    public String getStopOrderPrice(String direction, String stopOrderType) {
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
        stopOrderPrice = price;
        return price;
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

    public void tapsButtonOnConfirm(String buttonName) {
        if (driver instanceof AndroidDriver) {
            if (buttonName.contains("Position") || buttonName.contains("Modify") || buttonName.contains("Cancel Order")) {
                WebElement button = driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + buttonName + "\"]/parent::android.view.ViewGroup"));
                abs.waitUntilElementFind(button);
                button.click();
            } else {
                driver.findElement(By.xpath("(//android.widget.TextView[@text=\"" + buttonName + "\"])[2]/parent::android.view.ViewGroup")).click();
            }
        }
    }

//    public String getDetailValue(String value) {
//        List<WebElement> text = driver.findElements(By.className("android.widget.TextView"));
//        abs.waitUtilAllElementFind(text);
//        for (int i = 0; i < text.size(); i++) {
//            if (text.get(i).getText().equalsIgnoreCase(value)) {
//                if (value.equalsIgnoreCase("Volume")) {
//                    return text.get(i + 1).getText().split("Lots")[0].trim();
//                }
//                return text.get(i + 1).getText();
//            }
//        }
//        return null;
//    }

    public String getDetailValue(String value) {
        By locator = By.className("android.widget.TextView");
        for (int attempt = 1; attempt <= 5; attempt++) {
            try {
                List<WebElement> text = driver.findElements(locator);
                for (int i = 0; i < text.size(); i++) {
                    if (text.get(i).getText().equalsIgnoreCase(value)) {
                        if (value.equalsIgnoreCase("Volume")) {
                            return text.get(i + 1).getText().split("Lots")[0].trim();
                        }
                        return text.get(i + 1).getText();
                    }
                }
            } catch (StaleElementReferenceException e) {
                System.out.println("Stale elements while reading texts, retrying...");
            }
        }
        throw new RuntimeException("Unable to get texts from TextViews");

    }

    public String getDetailValue(String label, String symbolDecimal) {
        String uiLabel = getPageElement.mapUiLabel(label);

        String rawValue = getPageElement.findValueOnSameRow(uiLabel);

        if (rawValue == null || rawValue.isBlank()) {
            rawValue = getPageElement.findValueByFollowingSibling(uiLabel);
        }

        if (rawValue == null || rawValue.isBlank()) {
            rawValue = getPageElement.findValueByFollowingSiblingScoped(uiLabel);
        }

        if (rawValue == null || rawValue.isBlank()) {
            rawValue = getPageElement.findValueFromPageSource(uiLabel);
        }

        if (rawValue == null || rawValue.isBlank()) {
            throw new NoSuchElementException("Could not find value in hierarchy for label: " + uiLabel);
        }

        getPageElement.logInfo("Resolved raw value for " + uiLabel + ": " + rawValue);

        return getPageElement.normalizeByLabel(label, rawValue.trim(), symbolDecimal);
    }

    public String getPositionValue(String value, String symbolDecimal) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
        wait.ignoring(StaleElementReferenceException.class);

        try {
            return wait.until(d -> {
                List<WebElement> elements = d.findElements(By.className("android.widget.TextView"));
                List<String> texts = new ArrayList<>();

                for (WebElement element : elements) {
                    String text = element.getText();
                    if (text != null && !text.trim().isEmpty()) {
                        texts.add(text.trim());
                    }
                }

                for (int i = 0; i < texts.size(); i++) {
                    String currentLabel = texts.get(i);

                    if (currentLabel != null && currentLabel.equalsIgnoreCase(value)) {
                        for (int j = i + 1; j < Math.min(i + 4, texts.size()); j++) {
                            String candidate = texts.get(j);

                            if (candidate == null || candidate.isBlank()) {
                                continue;
                            }

                            if (candidate.equalsIgnoreCase(value)) {
                                continue;
                            }

                            if (value.equalsIgnoreCase("Take Profit Price") || value.equalsIgnoreCase("Stop Loss Price")) {
                                return abs.normalizePriceToDecimals(candidate, symbolDecimal);
                            }

                            return abs.normalizeDialogueValue(value, candidate);
                        }
                    }
                }

                return null;
            });
        } catch (TimeoutException e) {
            return null;
        }
    }

//    public String getPositionValueByLabel(String label, String symbolDecimal) {
//        final int maxAttempts = 5;
//
//        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
//            try {
//                String xml = driver.getPageSource();
//                List<String> texts = abs.extractTextViewTexts(xml);
//
//                for (int i = 0; i < texts.size() - 1; i++) {
//                    if (label.equals(texts.get(i))) {
//                        String rawValue = texts.get(i + 1);
//                        if (rawValue == null || rawValue.trim().isEmpty()) {
//                            return null;
//                        }
//
//                        rawValue = rawValue.trim();
//
//                        return isPriceLabel(label)
//                                ? abs.normalizePriceToDecimals(rawValue, symbolDecimal)
//                                : abs.normalizeDialogueValue(label, rawValue);
//                    }
//                }
//
//                return null;
//
//            } catch (Exception e) {
//                if (attempt == maxAttempts) {
//                    throw e;
//                }
//                abs.sleep(250);
//            }
//        }
//
//        return null;
//    }


    public String getPositionValueByLabel(String label, String symbolDecimal) {
        abs.waitUntilElementVisible(By.xpath("//*[@text='Position Details']"));
        String uiLabel = getPageElement.mapPositionDetailsLabel(label);

        String rawValue = getPageElement.resolveLabelValue(uiLabel);

        if (rawValue == null || rawValue.isBlank()) {
            throw new NoSuchElementException("Could not find value in hierarchy for label: " + uiLabel);
        }

        getPageElement.logInfo("Resolved raw value for " + uiLabel + ": " + rawValue);

        return getPageElement.normalizeByLabel(label, rawValue.trim(), symbolDecimal);
    }




    public String getValidationValue(String label) {
        return switch (label) {
//            case "Stop Loss Price" -> String.format("%.2f", Double.parseDouble(stopLossPrice));
//            case "Take Profit Price" -> String.format("%.2f", Double.parseDouble(takeProfitPrice));
            case "Stop Loss Price" -> stopLossPrice;
            case "Take Profit Price" -> takeProfitPrice;
            case "Direction" -> selectedDirection;
            case "Volume" -> lotSize;
            case "Stop Order Price" -> stopOrderPrice;
            case "Validity" -> validity;
            default -> null;
        };
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

    public boolean getPositionDetail(String positionDetail) {
        for (WebElement element : rowsOnPositionTabAos) {
            if (element.getText().equalsIgnoreCase(positionDetail)) {
                return true;
            }
        }
        return false;
    }

    public void getExecutedPrice() {
        executedPrice = getDetailValue("Price");
    }

    public void tapCtaButton(String buttonName) {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        waitUntilOverlayGone();
        waitForTradeRowArea();
        switch (buttonName) {
            case "detail" -> openRowDetails();
            case "close" -> openClosePositionPage();
            case "edit" -> openEditPosition();
        }
    }

    private void openEditPosition() {
        clickBottomRowCta(rowEditLocators());
        if (isHeaderVisible("Edit Position", 8)) {
            return;
        }
        abs.swipeUp(driver);
        clickBottomRowCta(rowEditLocators());
        if (!isHeaderVisible("Edit Position", 8)) {
            throw new TimeoutException("Edit Position did not open after tapping the edit CTA");
        }
    }

    private void openRowDetails() {
        clickBottomRowCta(rowDetailLocators());
        if (isRowDetailsOpen(10)) {
            return;
        }
        abs.swipeUp(driver);
        clickBottomRowCta(rowDetailLocators());
        if (!isRowDetailsOpen(10)) {
            throw new TimeoutException("Details page did not open after tapping the detail CTA");
        }
    }

    private boolean isHeaderVisible(String header, int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> isHeaderPresent(header));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean isHeaderPresent(String header) {
        return topHeaderLabel(header) != null;
    }

    private WebElement topHeaderLabel(String header) {
        try {
            int maxHeaderY = (int) (driver.manage().window().getSize().getHeight() * 0.28);
            for (WebElement el : driver.findElements(By.xpath("//*[@text='" + header + "']"))) {
                if (!el.isDisplayed()) {
                    continue;
                }
                if (el.getLocation().getY() < maxHeaderY) {
                    return el;
                }
            }
            return null;
        } catch (StaleElementReferenceException e) {
            return null;
        }
    }

    private void waitUntilOverlayGone() {
        By overlay = By.xpath("//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(12))
                    .ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.invisibilityOfElementLocated(overlay));
            return;
        } catch (TimeoutException ignored) {
        }
        try {
            abs.tapVisible(By.xpath(
                    "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[last()]"), 3);
            new WebDriverWait(driver, Duration.ofSeconds(8))
                    .ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.invisibilityOfElementLocated(overlay));
        } catch (TimeoutException ignored) {
        }
    }

    private void waitForTradeRowArea() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> !d.findElements(By.xpath("//*[@text='Positions']")).isEmpty()
                            || !d.findElements(By.xpath("//*[@text='Pending Orders']")).isEmpty());
        } catch (TimeoutException ignored) {
        }
    }

    private boolean isRowDetailsOpen(int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(d ->
                    !d.findElements(By.xpath("//*[@text='Position Details']")).isEmpty()
                            || !d.findElements(By.xpath("//*[@text='Pending Order Details']")).isEmpty()
            );
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private List<By> rowCloseLocators() {
        return rowCtaLocators(1, crossButtonAos);
    }

    private List<By> rowEditLocators() {
        return rowCtaLocators(2);
    }

    private List<By> rowDetailLocators() {
        List<By> locators = new ArrayList<>(rowCtaLocators(3, detailsButton));
        locators.add(By.xpath(
                "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]"
                        + "/android.view.ViewGroup/android.view.ViewGroup[last()]/android.view.ViewGroup"));
        locators.add(By.xpath(
                "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup"
                        + "/android.view.ViewGroup/android.view.ViewGroup[last()]"));
        return locators;
    }

    private List<By> rowCtaLocators(int index, By... extras) {
        List<By> locators = new ArrayList<>();
        locators.add(By.xpath(
                "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]"
                        + "/android.view.ViewGroup/android.view.ViewGroup[" + index + "]/android.view.ViewGroup"));
        locators.add(By.xpath(
                "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]"
                        + "/android.view.ViewGroup/android.view.ViewGroup[" + index + "]"));
        locators.add(By.xpath(
                "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup"
                        + "/android.view.ViewGroup/android.view.ViewGroup[" + index + "]/android.view.ViewGroup"));
        locators.add(By.xpath(
                "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup"
                        + "/android.view.ViewGroup/android.view.ViewGroup[" + index + "]"));
        locators.addAll(Arrays.asList(extras));
        return locators;
    }

    private void clickRowCta(List<By> locators) {
        TimeoutException lastError = null;
        for (By locator : locators) {
            try {
                abs.tapVisible(locator);
                return;
            } catch (TimeoutException e) {
                lastError = e;
            }
        }
        throw lastError != null ? lastError : new TimeoutException("Row CTA was not visible");
    }

    private void clickBottomRowCta(List<By> locators) {
        if (tapRowCtaWhenVisible(locators, 15)) {
            return;
        }
        for (int swipe = 0; swipe < 3; swipe++) {
            abs.swipeUp(driver);
            if (tapRowCtaWhenVisible(locators, 8)) {
                return;
            }
        }
        throw new TimeoutException("Row CTA was not visible");
    }

    private boolean tapRowCtaWhenVisible(List<By> locators, int seconds) {
        try {
            Point point = new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> bottomMostRowCtaPoint(locators));
            abs.tapAt(point.getX(), point.getY());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private Point bottomMostRowCtaPoint(List<By> locators) {
        Point best = null;
        int maxY = Integer.MIN_VALUE;
        for (By locator : locators) {
            for (WebElement element : driver.findElements(locator)) {
                Point point = visibleCenter(element);
                if (point == null) {
                    continue;
                }
                if (point.getY() >= maxY) {
                    maxY = point.getY();
                    best = point;
                }
            }
        }
        return best;
    }

    private Point visibleCenter(WebElement element) {
        try {
            if (!element.isDisplayed()) {
                return null;
            }
            Point location = element.getLocation();
            Dimension size = element.getSize();
            if (size.getWidth() <= 0 || size.getHeight() <= 0) {
                return null;
            }
            return new Point(location.getX() + size.getWidth() / 2, location.getY() + size.getHeight() / 2);
        } catch (StaleElementReferenceException e) {
            return null;
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
            clickBottomRowCta(rowCloseLocators());
            if (AppSettingPage.isTradeConfirmNeeded) {
                abs.waitUntilElementClickable(cancelOrderButtonAos).click();
            } else {
                System.out.println("Pending order cancelled");
            }

        }
    }

    public void closePosition() {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        leaveEditPositionIfOpen();
        if (!isClosePositionPageOpen(2)) {
            openClosePositionPage();
        }
        tapClosePositionSubmit();
        if (AppSettingPage.isTradeConfirmNeeded) {
            confirmClosePositionDialogue();
        }
    }

    private void leaveEditPositionIfOpen() {
        if (!isHeaderVisible("Edit Position", 1)) {
            return;
        }
        tapBack();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(8))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> !isHeaderPresent("Edit Position"));
        } catch (TimeoutException e) {
            throw new TimeoutException("Edit Position was still visible after tapping Back");
        }
    }

    private void openClosePositionPage() {
        clickBottomRowCta(rowCloseLocators());
        if (isClosePositionPageOpen(8)) {
            return;
        }
        abs.swipeUp(driver);
        clickBottomRowCta(rowCloseLocators());
        if (!isClosePositionPageOpen(8)) {
            throw new TimeoutException("Close Position page did not open after tapping the close CTA");
        }
    }

    private void confirmClosePositionDialogue() {
        By overlay = By.xpath("//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]");
        By overlayClosePosition = By.xpath(
                "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]//*[@text='Close Position']"
        );
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .ignoring(StaleElementReferenceException.class)
                    .until(ExpectedConditions.visibilityOfElementLocated(overlay));
            abs.tapBottomMost(overlayClosePosition, 10);
        } catch (TimeoutException ignored) {
            System.out.println("Close position confirmation overlay was not shown");
        }
    }

    private boolean isClosePositionPageOpen(int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> bottomVisibleClosePositionLabel() != null);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private void tapClosePositionSubmit() {
        try {
            Point tapPoint = new WebDriverWait(driver, Duration.ofSeconds(8))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> closePositionSubmitPoint());
            abs.tapAt(tapPoint.getX(), tapPoint.getY());
            return;
        } catch (TimeoutException ignored) {
            System.out.println("Bottom Close Position label was not a visible @text node; trying submit fallbacks");
        }
        try {
            abs.tapVisible(AppiumBy.androidUIAutomator("new UiSelector().text(\"Close Position\").instance(1)"), 5);
            return;
        } catch (TimeoutException ignored) {
        }
        Point fallback = closePositionButtonFallbackPoint();
        if (fallback != null) {
            abs.tapAt(fallback.getX(), fallback.getY());
            return;
        }
        Dimension window = driver.manage().window().getSize();
        abs.tapAt(window.getWidth() / 2, (int) (window.getHeight() * 0.90));
    }

    private Point closePositionSubmitPoint() {
        try {
            WebElement bottom = bottomVisibleClosePositionLabel();
            if (bottom == null) {
                return null;
            }
            Point location = bottom.getLocation();
            Dimension size = bottom.getSize();
            if (location.getY() < driver.manage().window().getSize().getHeight() / 2) {
                return null;
            }
            return new Point(location.getX() + size.getWidth() / 2, location.getY() + size.getHeight() / 2);
        } catch (StaleElementReferenceException e) {
            return null;
        }
    }

    private WebElement bottomVisibleClosePositionLabel() {
        WebElement bottom = null;
        int maxY = Integer.MIN_VALUE;
        for (WebElement el : driver.findElements(By.xpath("//*[@text='Close Position']"))) {
            try {
                if (!el.isDisplayed()) {
                    continue;
                }
                int y = el.getLocation().getY();
                if (y >= maxY) {
                    maxY = y;
                    bottom = el;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return bottom;
    }

    private Point closePositionButtonFallbackPoint() {
        try {
            Dimension window = driver.manage().window().getSize();
            int minY = (int) (window.getHeight() * 0.65);
            int minWidth = (int) (window.getWidth() * 0.45);
            Point best = null;
            int bestHeight = Integer.MAX_VALUE;
            for (WebElement el : driver.findElements(By.className("android.view.ViewGroup"))) {
                if (!el.isDisplayed()) {
                    continue;
                }
                Point location = el.getLocation();
                Dimension size = el.getSize();
                if (location.getY() < minY || size.getWidth() < minWidth) {
                    continue;
                }
                if (size.getHeight() < 40 || size.getHeight() > 160) {
                    continue;
                }
                if (size.getHeight() <= bestHeight) {
                    bestHeight = size.getHeight();
                    best = new Point(location.getX() + size.getWidth() / 2, location.getY() + size.getHeight() / 2);
                }
            }
            return best;
        } catch (StaleElementReferenceException e) {
            return null;
        }
    }

    public void closePositionInDetails() {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        tapClosePositionSubmit();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> {
                        try {
                            return d.findElements(By.xpath("//*[@text='Position Details']")).isEmpty();
                        } catch (StaleElementReferenceException e) {
                            return false;
                        }
                    });
        } catch (TimeoutException ignored) {
            System.out.println("Position Details was still visible after tapping Close Position");
        }
        closePosition();
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
        if (stopLossPrice != null || AppInstrumentDetailsPage.stopLossPrice != null) {
            values.add("Stop Loss Price");
        }
        if (takeProfitPrice != null || AppInstrumentDetailsPage.takeProfitPrice != null) {
            values.add("Take Profit Price");
        }
        values.add("Direction");
        values.add("Volume");
        return values;
    }

    public String getDialogueTextAos() {
        String dialogText = "";
        if (driver instanceof AndroidDriver) {
            dialogText = abs.captureTransientText(
                    () -> dialogueTextAos,
                    Duration.ofSeconds(5)
            );
        }
        return dialogText;
    }

    public void cancelPendingOrderInDetail() throws InterruptedException {
        tapsButton("Cancel Order");
        Thread.sleep(500);
        tapsButtonOnConfirm("Cancel Order");
    }

    public boolean getPendingOrder() {
        if (driver instanceof AndroidDriver) {
            if (rowsOnPendingOrdersTabAos == null || rowsOnPendingOrdersTabAos.isEmpty()) {
                return false;
            }
            return rowsOnPendingOrdersTabAos.getFirst().isDisplayed();
        }
        return false;
    }

    public boolean getPosition() {
        if (driver instanceof AndroidDriver) {
            if (rowsOnPositionTabAos == null || rowsOnPositionTabAos.isEmpty()) {
                return false;
            }
            return rowsOnPositionTabAos.getFirst().isDisplayed();
        }
        return false;
    }

    public void setLotSize(String symbolLotSize) {
        lotSize = symbolLotSize;
    }

    public void tapBack() {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        if (!isOnPageWithBackNavigation()) {
            return;
        }
        tapBackChevron();
    }

    public void leaveTradeView() {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        waitUntilOverlayGone();
        tapBackChevron();
    }

    private void tapBackChevron() {
        List<By> locators = Arrays.asList(
                backBtnAos,
                By.xpath("//*[@content-desc='Back']"),
                By.xpath("//*[@content-desc='Navigate up']")
        );
        for (By locator : locators) {
            try {
                abs.tapVisible(locator, 8);
                return;
            } catch (TimeoutException ignored) {
            }
        }
        Dimension window = driver.manage().window().getSize();
        abs.tapAt(Math.max(40, window.getWidth() / 12), Math.max(80, (int) (window.getHeight() * 0.08)));
    }

    private boolean isOnPageWithBackNavigation() {
        return isHeaderPresent("Edit Position")
                || isHeaderPresent("Position Details")
                || isHeaderPresent("Pending Order Details")
                || isHeaderPresent("Close Position")
                || isHeaderPresent("Modify Order");
    }

    public void captureVisibleRowPrice() {
        List<WebElement> texts = driver.findElements(By.xpath(
                "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]" +
                        "/android.view.ViewGroup/android.widget.TextView"
        ));
        String price = firstPriceLikeText(texts);
        if (price == null) {
            return;
        }
        openPositionOpenPrice = price;
        openOrderTargetPrice = price;
    }

    public void getOpenPositionOpenPrice() {
        By rowTexts = By.xpath(
                "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]" +
                        "/android.view.ViewGroup/android.widget.TextView"
        );
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.ignoring(StaleElementReferenceException.class);
        openPositionOpenPrice = wait.until(d -> firstPriceLikeText(d.findElements(rowTexts)));
    }

    public void getPendingOrderTargetPrice() {
        By rowTexts = By.xpath(
                "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup" +
                        "/android.view.ViewGroup/android.widget.TextView"
        );
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.ignoring(StaleElementReferenceException.class);
        openOrderTargetPrice = wait.until(d -> firstPriceLikeText(d.findElements(rowTexts)));
    }

    private String firstPriceLikeText(List<WebElement> texts) {
        if (texts == null || texts.isEmpty()) {
            return null;
        }
        for (WebElement element : texts) {
            String text = element.getText();
            if (text != null && text.trim().matches("\\d+[.,]\\d+")) {
                return text.trim();
            }
        }
        return null;
    }

    public void closeDialogue() {
        if (driver instanceof AndroidDriver) {
            abs.waitUntilElementFind(closeDialogueBtnAos);
            closeDialogueBtnAos.click();
        }
    }

    public String getOpenPositionTime() {
        if (driver instanceof AndroidDriver) {
            return openPositionRecordDetailsAos.get(3).getText();
        }
        return null;
    }

    public boolean isOpenPositionDateValid() {
        return abs.dateValidator(getOpenPositionTime());
    }

    public void selectTab(String tabName){
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        List<By> locators = Arrays.asList(
                By.xpath("//android.widget.TextView[@text=\"" + tabName + "\"]/parent::android.view.ViewGroup"),
                By.xpath("//android.widget.TextView[@text=\"" + tabName + "\"]"),
                positionsTabAos
        );
        clickRowCta(locators);
    }

    public int getNumberOfPositions(){
        By rowsOnPositionTab = By.xpath(
                "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup"
        );
        if (driver instanceof AndroidDriver) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
            final int[] previous = {-1};
            final int[] stable = {0};

            return wait.until(d -> {
                int current = d.findElements(rowsOnPositionTab).size();

                if (current == previous[0]) {
                    stable[0]++;
                } else {
                    previous[0] = current;
                    stable[0] = 0;
                }

                return stable[0] >= 2 ? current : null;
            });
        }
        return 0;

    }

    public int getNumberOfPendingOrders(){
        By rowsOnPendingOrders = By.xpath(
                "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup"
        );
        if (driver instanceof AndroidDriver) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(100));
            final int[] previous = {-1};
            final int[] stable = {0};

            return wait.until(d -> {
                int current = d.findElements(rowsOnPendingOrders).size();

                if (current == previous[0]) {
                    stable[0]++;
                } else {
                    previous[0] = current;
                    stable[0] = 0;
                }

                return stable[0] >= 2 ? current : null;
            });
        }
        return 0;
    }

    public void selectList(String listName){
        if (driver instanceof AndroidDriver) {
            driver.findElement(By.xpath("//android.widget.TextView[@text=\"" + listName + "\"]/parent::android.view.ViewGroup")).click();
        }
    }


}
