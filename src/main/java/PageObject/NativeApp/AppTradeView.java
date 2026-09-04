package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
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
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        TimeoutException lastError = null;
        for (By locator : directionLocators(direction)) {
            try {
                abs.tapVisible(locator, 15);
                waitForOrderTicket();
                return;
            } catch (TimeoutException e) {
                lastError = e;
            }
        }
        throw lastError != null
                ? lastError
                : new TimeoutException("Direction was not visible: " + direction);
    }

    private void waitForOrderTicket() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                        "//android.widget.TextView[@text='Market Order' or @text='Limit / Stop Order' or @text='Lots']"
                )));
    }

    private List<By> directionLocators(String direction) {
        return List.of(
                By.xpath("//android.widget.TextView[@text=\"" + direction + "\"]/parent::android.view.ViewGroup"),
                By.xpath("//*[@text=\"" + direction + "\"]")
        );
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
        String uiLabel = getPageElement.mapUiLabel(value);
        String rawValue = getPageElement.resolveLabelValue(uiLabel);
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
        getPageElement.waitAndCaptureIfNeeded(By.xpath("//*[@text='Position Details']"), 10);
        String uiLabel = getPageElement.mapPositionDetailsLabel(label);
        String rawValue = getPageElement.readLabelValueFast(uiLabel);
        if (rawValue == null || rawValue.isBlank()) {
            throw new NoSuchElementException("Could not find value in hierarchy for label: " + uiLabel);
        }
        getPageElement.logInfo("Resolved raw value for " + uiLabel + ": " + rawValue);
        return getPageElement.normalizeByLabel(label, rawValue.trim(), symbolDecimal);
    }

    public void waitForPositionDetails() {
        getPageElement.waitAndCapturePositionDetails();
    }




    public String getValidationValue(String label) {
        return switch (label) {
//            case "Stop Loss Price" -> String.format("%.2f", Double.parseDouble(stopLossPrice));
//            case "Take Profit Price" -> String.format("%.2f", Double.parseDouble(takeProfitPrice));
            case "Stop Loss Price" -> stopLossPrice;
            case "Take Profit Price" -> takeProfitPrice;
            case "Direction" -> selectedDirection;
            case "Volume" -> getPageElement.canonicalizeVolume(lotSize);
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
        revealListForCurrentOrder();
        switch (buttonName) {
            case "detail" -> retryOnStale(this::openRowDetails);
            case "close" -> retryOnStale(this::tapCloseRowCta);
            case "edit" -> retryOnStale(this::openEditPosition);
        }
    }

    private void openEditPosition() {
        waitForFirstListRow();
        if (tapFirstRowCta("edit") && isEditOrModifyOpen(8)) {
            return;
        }
        if (!tryClickRowCta(rowEditLocators())) {
            revealOtherOrderList();
            tryClickRowCta(rowEditLocators());
        }
        if (!isEditOrModifyOpen(8)) {
            throw new TimeoutException("Edit or Modify page did not open after tapping the edit CTA");
        }
    }

    private boolean isEditOrModifyOpen(int seconds) {
        return isHeaderVisible("Edit Position", seconds) || isHeaderVisible("Modify Order", seconds);
    }

    private boolean isPendingOrderFlow() {
        String type = AppInstrumentDetailsPage.stopOrderType;
        return type != null && !type.isBlank();
    }

    private void revealListForCurrentOrder() {
        tapListTab(isPendingOrderFlow() ? "Pending Orders" : "Positions");
    }

    private void revealOtherOrderList() {
        tapListTab(isPendingOrderFlow() ? "Positions" : "Pending Orders");
    }

    private void tapListTab(String tabName) {
        waitForTradeRowArea();
        try {
            Point point = new WebDriverWait(driver, Duration.ofSeconds(8))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> compactTabPoint(tabName));
            abs.tapAt(point.getX(), point.getY());
            return;
        } catch (TimeoutException ignored) {
        }
        TimeoutException lastError = null;
        for (By locator : List.of(
                By.xpath("//android.widget.TextView[@text='" + tabName + "']"),
                By.xpath("//*[@text='" + tabName + "']"),
                By.xpath("//android.widget.TextView[@text='" + tabName + "']/parent::android.view.ViewGroup")
        )) {
            try {
                abs.tapVisible(locator, 8);
                return;
            } catch (TimeoutException e) {
                lastError = e;
            }
        }
        throw lastError != null
                ? lastError
                : new TimeoutException("List tab was not visible: " + tabName);
    }

    private Point compactTabPoint(String tabName) {
        Dimension window = driver.manage().window().getSize();
        Point best = null;
        int bestY = Integer.MAX_VALUE;
        for (WebElement el : driver.findElements(By.xpath("//*[@text='" + tabName + "']"))) {
            try {
                if (!el.isDisplayed()) {
                    continue;
                }
                Point location = el.getLocation();
                Dimension size = el.getSize();
                if (size.getHeight() > 100) {
                    continue;
                }
                if (size.getWidth() > (int) (window.getWidth() * 0.7)) {
                    continue;
                }
                int x = location.getX();
                int y = location.getY();
                if (size.getWidth() > 0 && size.getHeight() > 0) {
                    x += size.getWidth() / 2;
                    y += size.getHeight() / 2;
                }
                if (location.getY() < bestY) {
                    bestY = location.getY();
                    best = new Point(x, y);
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return best;
    }

    private void retryOnStale(Runnable action) {
        StaleElementReferenceException lastError = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                action.run();
                return;
            } catch (StaleElementReferenceException e) {
                lastError = e;
                try {
                    Thread.sleep(250);
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    throw e;
                }
            }
        }
        throw lastError;
    }

    private boolean tryClickRowCta(List<By> locators) {
        try {
            clickBottomRowCta(locators);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private void openRowDetails() {
        waitForFirstListRow();
        if (tapFirstRowCta("detail") && isRowDetailsOpen(8)) {
            return;
        }
        if (tapFirstRowCta("detail") && isRowDetailsOpen(6)) {
            return;
        }
        tryClickRowCta(rowDetailLocators());
        if (!isRowDetailsOpen(8)) {
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
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d ->
                    !d.findElements(By.xpath(
                            "//*[@text='Position Details' or @text='Pending Order Details' or @text='Position Detail']"
                    )).isEmpty()
            );
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private List<By> rowCloseLocators() {
        List<By> locators = new ArrayList<>(rowCtaLocators(1, crossButtonAos));
        locators.add(By.xpath(
                "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup"
                        + "/android.view.ViewGroup/android.view.ViewGroup[1]//android.widget.ImageView"));
        locators.add(By.xpath(
                "//*[@content-desc='close' or @content-desc='Close' or @content-desc='Cancel']"));
        return locators;
    }

    private List<By> rowEditLocators() {
        return rowCtaLocators(2);
    }

    private List<By> rowDetailLocators() {
        return rowCtaLocators(3, detailsButton);
    }

    private List<By> rowCtaLocators(int index, By... extras) {
        List<By> locators = new ArrayList<>();
        for (String row : Arrays.asList("[1]", "[2]", "")) {
            String rowStep = row.isEmpty() ? "" : row;
            locators.add(By.xpath(
                    "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup"
                            + rowStep + "/android.view.ViewGroup/android.view.ViewGroup[" + index + "]/android.view.ViewGroup"));
            locators.add(By.xpath(
                    "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup"
                            + rowStep + "/android.view.ViewGroup/android.view.ViewGroup[" + index + "]"));
        }
        locators.addAll(Arrays.asList(extras));
        return locators;
    }

    private void clickRowCta(List<By> locators) {
        TimeoutException lastError = null;
        for (By locator : locators) {
            try {
                abs.tapVisible(locator);
                return;
            } catch (StaleElementReferenceException e) {
                lastError = new TimeoutException("Row CTA went stale", e);
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
                    .until(d -> listRowCtaPoint(locators));
            abs.tapAt(point.getX(), point.getY());
            return true;
        } catch (StaleElementReferenceException e) {
            return false;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private Point listRowCtaPoint(List<By> locators) {
        List<Point> visible = visibleCtaPoints(locators);
        if (visible.isEmpty()) {
            return null;
        }
        int minY = listAreaTopY() - 36;
        Point inList = null;
        int bestListY = Integer.MAX_VALUE;
        Point bottomMost = null;
        int maxY = Integer.MIN_VALUE;
        for (Point point : visible) {
            if (point.getY() >= minY && point.getY() < bestListY) {
                bestListY = point.getY();
                inList = point;
            }
            if (point.getY() >= maxY) {
                maxY = point.getY();
                bottomMost = point;
            }
        }
        return inList != null ? inList : bottomMost;
    }

    private boolean tapFirstRowCta(String buttonName) {
        try {
            Point point = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> firstRowCtaPoint(buttonName));
            System.out.println("Tapping " + buttonName + " CTA at " + point.getX() + "," + point.getY());
            abs.tapAt(point.getX(), point.getY());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private void waitForFirstListRow() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(12))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> firstListRowBounds() != null);
        } catch (TimeoutException ignored) {
        }
    }

    private Point firstRowCtaPoint(String buttonName) {
        int[] row = firstListRowBounds();
        if (row == null) {
            return null;
        }
        int top = row[1];
        int width = row[2];
        int height = row[3];
        List<Point> icons = compactIconsOnRow(top, top + height);
        if (icons.size() >= 3) {
            return switch (buttonName) {
                case "close" -> icons.get(icons.size() - 3);
                case "edit" -> icons.get(icons.size() - 2);
                default -> icons.get(icons.size() - 1);
            };
        }
        if (icons.size() == 1 && "detail".equals(buttonName)) {
            return icons.getFirst();
        }
        Dimension window = driver.manage().window().getSize();
        double ratio = switch (buttonName) {
            case "close" -> 0.70;
            case "edit" -> 0.83;
            default -> 0.94;
        };
        return new Point((int) (window.getWidth() * ratio), top + height / 2);
    }

    private int[] firstListRowBounds() {
        int[] fromSymbol = rowBoundsAroundSymbol();
        if (fromSymbol != null) {
            return fromSymbol;
        }
        int minY = listAreaTopY();
        Dimension window = driver.manage().window().getSize();
        int[] best = null;
        int bestY = Integer.MAX_VALUE;
        for (WebElement el : driver.findElements(By.xpath(
                "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup"))) {
            try {
                if (!el.isDisplayed()) {
                    continue;
                }
                Point location = el.getLocation();
                Dimension size = el.getSize();
                if (location.getY() < minY - 20) {
                    continue;
                }
                if (size.getHeight() < 56 || size.getHeight() > 400) {
                    continue;
                }
                if (size.getWidth() < (int) (window.getWidth() * 0.55)) {
                    continue;
                }
                if (location.getY() < bestY) {
                    bestY = location.getY();
                    best = new int[]{location.getX(), location.getY(), size.getWidth(), size.getHeight()};
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return best;
    }

    private int[] rowBoundsAroundSymbol() {
        String symbol = AppMarketsPage.tradeSymbol;
        if (symbol == null || symbol.isBlank()) {
            return null;
        }
        int minY = listAreaTopY();
        Dimension window = driver.manage().window().getSize();
        int bestY = Integer.MAX_VALUE;
        int[] best = null;
        for (WebElement el : driver.findElements(By.xpath("//*[@text='" + symbol + "']"))) {
            try {
                if (!el.isDisplayed()) {
                    continue;
                }
                Point location = el.getLocation();
                if (location.getY() < minY - 10) {
                    continue;
                }
                Dimension size = el.getSize();
                int top = Math.max(minY, location.getY() - 16);
                int height = Math.max(size.getHeight() + 48, 88);
                if (location.getY() < bestY) {
                    bestY = location.getY();
                    best = new int[]{0, top, window.getWidth(), height};
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return best;
    }

    private List<Point> compactIconsOnRow(int rowTop, int rowBottom) {
        Dimension window = driver.manage().window().getSize();
        int minX = (int) (window.getWidth() * 0.52);
        List<Point> raw = new ArrayList<>();
        for (By locator : List.of(
                By.className("android.widget.ImageView"),
                By.xpath("//android.widget.ScrollView//android.view.ViewGroup")
        )) {
            for (WebElement el : driver.findElements(locator)) {
                try {
                    Point location = el.getLocation();
                    Dimension size = el.getSize();
                    if (size.getWidth() < 20 || size.getWidth() > 120) {
                        continue;
                    }
                    if (size.getHeight() < 20 || size.getHeight() > 120) {
                        continue;
                    }
                    int centerX = location.getX() + size.getWidth() / 2;
                    int centerY = location.getY() + size.getHeight() / 2;
                    if (centerY < rowTop - 6 || centerY > rowBottom + 6 || centerX < minX) {
                        continue;
                    }
                    raw.add(new Point(centerX, centerY));
                } catch (StaleElementReferenceException ignored) {
                }
            }
        }
        raw.sort(Comparator.comparingInt(Point::getX));
        List<Point> clustered = new ArrayList<>();
        for (Point point : raw) {
            if (clustered.isEmpty()
                    || Math.abs(point.getX() - clustered.getLast().getX()) > 24) {
                clustered.add(point);
            }
        }
        return clustered;
    }

    private List<Point> visibleCtaPoints(List<By> locators) {
        List<Point> points = new ArrayList<>();
        Dimension window = driver.manage().window().getSize();
        int maxWidth = (int) (window.getWidth() * 0.22);
        for (By locator : locators) {
            try {
                for (WebElement element : driver.findElements(locator)) {
                    Point point = compactCtaCenter(element, maxWidth);
                    if (point != null) {
                        points.add(point);
                    }
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return points;
    }

    private Point compactCtaCenter(WebElement element, int maxWidth) {
        Point point = visibleCenter(element);
        if (point == null) {
            return null;
        }
        try {
            Dimension size = element.getSize();
            if (size.getWidth() > maxWidth || size.getHeight() > 140) {
                return null;
            }
            return point;
        } catch (StaleElementReferenceException e) {
            return point;
        }
    }

    private int listAreaTopY() {
        Dimension window = driver.manage().window().getSize();
        int fallback = (int) (window.getHeight() * 0.38);
        int bestTabY = Integer.MAX_VALUE;
        Integer tabBottom = null;
        for (String tab : Arrays.asList("Positions", "Pending Orders")) {
            try {
                for (WebElement el : driver.findElements(By.xpath("//*[@text='" + tab + "']"))) {
                    if (!el.isDisplayed()) {
                        continue;
                    }
                    Point location = el.getLocation();
                    Dimension size = el.getSize();
                    if (size.getHeight() < 8 || size.getHeight() > 100) {
                        continue;
                    }
                    if (size.getWidth() <= 0 || size.getWidth() > (int) (window.getWidth() * 0.7)) {
                        continue;
                    }
                    if (location.getY() < bestTabY) {
                        bestTabY = location.getY();
                        tabBottom = location.getY() + size.getHeight();
                    }
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return tabBottom != null ? tabBottom : fallback;
    }

    private Point visibleCenter(WebElement element) {
        try {
            Point location = element.getLocation();
            Dimension size = element.getSize();
            if (size.getWidth() > 0 && size.getHeight() > 0) {
                if (!element.isDisplayed()) {
                    return null;
                }
                return new Point(location.getX() + size.getWidth() / 2, location.getY() + size.getHeight() / 2);
            }
            String bounds = element.getDomAttribute("bounds");
            if (bounds == null || bounds.isBlank()) {
                bounds = element.getDomProperty("bounds");
            }
            if (bounds == null || bounds.isBlank()) {
                return null;
            }
            Matcher matcher = Pattern.compile("\\[(\\d+),(\\d+)]\\[(\\d+),(\\d+)]").matcher(bounds.trim());
            if (!matcher.matches()) {
                return null;
            }
            int left = Integer.parseInt(matcher.group(1));
            int top = Integer.parseInt(matcher.group(2));
            int right = Integer.parseInt(matcher.group(3));
            int bottom = Integer.parseInt(matcher.group(4));
            if (right - left <= 0 || bottom - top <= 0) {
                return null;
            }
            return new Point(left + (right - left) / 2, top + (bottom - top) / 2);
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
        tapBackChevron();
        if (!isHeaderPresent("Edit Position")) {
            return;
        }
        pressAndroidBack();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(8))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> !isHeaderPresent("Edit Position"));
        } catch (TimeoutException e) {
            throw new TimeoutException("Edit Position was still visible after tapping Back");
        }
    }

    private void tapCloseRowCta() {
        waitForFirstListRow();
        if (tapFirstRowCta("close")
                && (isPendingOrderFlow() || isCancelOrderPromptVisible() || isClosePositionPageOpen(6))) {
            return;
        }
        if (!tryClickRowCta(rowCloseLocators())) {
            revealOtherOrderList();
            tryClickRowCta(rowCloseLocators());
        }
        if (isPendingOrderFlow() || isCancelOrderPromptVisible()) {
            return;
        }
        if (!isClosePositionPageOpen(8)) {
            throw new TimeoutException("Close Position page did not open after tapping the close CTA");
        }
    }

    private boolean isCancelOrderPromptVisible() {
        try {
            for (WebElement element : driver.findElements(By.xpath("//*[@text='Cancel Order']"))) {
                if (element.isDisplayed()) {
                    return true;
                }
            }
            return false;
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    private void openClosePositionPage() {
        if (!tryClickRowCta(rowCloseLocators())) {
            revealOtherOrderList();
            clickBottomRowCta(rowCloseLocators());
        }
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
        if (stopLossPrice != null
                || AppInstrumentDetailsPage.stopLossPrice != null
                || AppEditPositionPage.stopLossPrice != null) {
            values.add("Stop Loss Price");
        }
        if (takeProfitPrice != null
                || AppInstrumentDetailsPage.takeProfitPrice != null
                || AppEditPositionPage.takeProfitPrice != null) {
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
        hideAndroidKeyboard();
        if (tapLeftOfPageTitle()) {
            return;
        }
        List<By> locators = Arrays.asList(
                By.xpath("//*[@content-desc='Back']"),
                By.xpath("//*[@content-desc='Navigate up']"),
                By.xpath("//*[@content-desc='back']"),
                backBtnAos
        );
        for (By locator : locators) {
            try {
                abs.tapVisible(locator, 5);
                return;
            } catch (TimeoutException ignored) {
            }
        }
        Dimension window = driver.manage().window().getSize();
        abs.tapAt(Math.max(40, window.getWidth() / 14), Math.max(80, (int) (window.getHeight() * 0.08)));
    }

    private boolean tapLeftOfPageTitle() {
        WebElement title = pageTitleInHeader();
        if (title == null) {
            return false;
        }
        try {
            Point location = title.getLocation();
            Dimension size = title.getSize();
            Dimension window = driver.manage().window().getSize();
            int y = location.getY() + Math.max(8, size.getHeight() / 2);
            int x = Math.min(Math.max(36, window.getWidth() / 14), Math.max(36, location.getX() - 24));
            abs.tapAt(x, y);
            return true;
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    private WebElement pageTitleInHeader() {
        for (String header : Arrays.asList(
                "Edit Position",
                "Position Details",
                "Pending Order Details",
                "Close Position",
                "Modify Order"
        )) {
            WebElement title = topHeaderLabel(header);
            if (title != null) {
                return title;
            }
        }
        return null;
    }

    private void hideAndroidKeyboard() {
        if (!(driver instanceof AndroidDriver androidDriver)) {
            return;
        }
        try {
            if (androidDriver.isKeyboardShown()) {
                androidDriver.hideKeyboard();
            }
        } catch (Exception ignored) {
        }
    }

    private void pressAndroidBack() {
        if (driver instanceof AndroidDriver androidDriver) {
            androidDriver.pressKey(new KeyEvent(AndroidKey.BACK));
        }
    }

    private boolean isOnPageWithBackNavigation() {
        return isHeaderPresent("Edit Position")
                || isHeaderPresent("Position Details")
                || isHeaderPresent("Pending Order Details")
                || isHeaderPresent("Close Position")
                || isHeaderPresent("Modify Order");
    }

    public void captureVisibleRowPrice() {
        By rowTexts = By.xpath(
                "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]" +
                        "/android.view.ViewGroup/android.widget.TextView"
        );
        try {
            String price = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> firstPriceLikeText(d.findElements(rowTexts)));
            openPositionOpenPrice = price;
            openOrderTargetPrice = price;
        } catch (TimeoutException ignored) {
        }
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
            try {
                String text = element.getText();
                if (text != null && text.trim().matches("\\d+[.,]\\d+")) {
                    return text.trim();
                }
            } catch (StaleElementReferenceException ignored) {
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
        tapListTab(tabName);
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
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        tapListTab(listName);
    }


}
