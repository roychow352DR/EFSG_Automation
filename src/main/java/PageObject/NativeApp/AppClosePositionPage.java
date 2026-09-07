package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.GetPageElement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppClosePositionPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    private final GetPageElement getPageElement;
    public static String lotsBeforeAdjust;

    public static void resetCapturedOrderValues() {
        lotsBeforeAdjust = null;
    }

    public AppClosePositionPage(AppiumDriver driver){
        this.driver = driver;
        this.abs = new MobileAbstractComponents(driver);
        this.getPageElement = new GetPageElement(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @FindBy(className = "android.widget.EditText")
    WebElement editFieldAos;

    @FindBy(xpath = "(//android.widget.TextView[@text=\"Close Position\"])[1]")
    WebElement headerAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[11]")
    WebElement closeBtnConfirmAos;


    public String getEditFieldVal(){
        if (driver instanceof AndroidDriver) {
            return normalizeLots(readFieldText(lotsField()));
        }
        return "No edit field found";
    }

    public void clickBtn(String btnName) {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        waitForClosePositionHeader();
        if ("All".equals(btnName)) {
            tapAllButton();
            return;
        }
        WebElement field = lotsField();
        lotsBeforeAdjust = normalizeLots(readFieldText(field));
        Point point = lotsStepperPoint(field, btnName);
        getPageElement.logInfo("Tapping " + btnName + " on Close Position lots at " + point.getX() + "," + point.getY());
        abs.tapAt(point.getX(), point.getY());
    }

    private String readFieldText(WebElement field) {
        String raw = elementAttribute(field, "text");
        if (raw == null || raw.isBlank()) {
            raw = elementAttribute(field, "hint");
        }
        return raw;
    }

    private String normalizeLots(String raw) {
        if (raw == null || raw.isBlank()) {
            return raw;
        }
        String text = raw.replaceAll("(?i)\\s*Lots?", "").trim().replace(",", "");
        return abs.normalizePriceToDecimals(text, "2");
    }

    private void tapAllButton() {
        List<By> locators = List.of(
                By.xpath("//*[@content-desc='All']"),
                By.xpath("//android.widget.TextView[@text='All']")
        );
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.ignoring(StaleElementReferenceException.class);
        for (By locator : locators) {
            try {
                Point point = wait.until(d -> allButtonPoint(d.findElements(locator)));
                getPageElement.logInfo("Tapping All on Close Position at " + point.getX() + "," + point.getY());
                abs.tapAt(point.getX(), point.getY());
                return;
            } catch (TimeoutException ignored) {
            }
        }
        Point fallback = allButtonFallbackPoint();
        getPageElement.logInfo("Tapping All on Close Position fallback at " + fallback.getX() + "," + fallback.getY());
        abs.tapAt(fallback.getX(), fallback.getY());
    }

    private Point allButtonPoint(List<WebElement> elements) {
        Point best = null;
        int maxY = Integer.MIN_VALUE;
        for (WebElement el : elements) {
            int[] bounds = parseBounds(elementAttribute(el, "bounds"));
            if (bounds == null || bounds[2] <= bounds[0] || bounds[3] <= bounds[1]) {
                continue;
            }
            int centerY = (bounds[1] + bounds[3]) / 2;
            if (centerY >= maxY) {
                maxY = centerY;
                best = new Point((bounds[0] + bounds[2]) / 2, centerY);
            }
        }
        return best;
    }

    private Point allButtonFallbackPoint() {
        WebElement field = lotsField();
        int[] fieldBounds = parseBounds(elementAttribute(field, "bounds"));
        if (fieldBounds == null) {
            throw new NoSuchElementException("Could not find All button on Close Position");
        }
        return new Point((fieldBounds[0] + fieldBounds[2]) / 2, fieldBounds[3] + 120);
    }

    private Point lotsStepperPoint(WebElement field, String btnName) {
        int[] fieldBounds = parseBounds(elementAttribute(field, "bounds"));
        if (fieldBounds == null) {
            throw new NoSuchElementException("Could not read bounds for Close Position lots field");
        }
        int fieldCenterY = (fieldBounds[1] + fieldBounds[3]) / 2;
        int fieldLeft = fieldBounds[0];
        int fieldRight = fieldBounds[2];
        int[] minus = null;
        int[] plus = null;
        int bestMinusGap = Integer.MAX_VALUE;
        int bestPlusGap = Integer.MAX_VALUE;
        for (int[] bounds : compactControlsOnLotsRow(fieldCenterY)) {
            int centerX = (bounds[0] + bounds[2]) / 2;
            if (centerX < fieldLeft) {
                int gap = Math.abs(bounds[2] - fieldLeft);
                if (gap < bestMinusGap) {
                    bestMinusGap = gap;
                    minus = bounds;
                }
            } else if (centerX > fieldRight) {
                int gap = Math.abs(bounds[0] - fieldRight);
                if (gap < bestPlusGap) {
                    bestPlusGap = gap;
                    plus = bounds;
                }
            }
        }
        if ("-".equals(btnName)) {
            if (minus != null) {
                return insetTowardIconCenter(minus, fieldLeft, true);
            }
            return new Point(Math.max(8, fieldLeft - 52), fieldCenterY);
        }
        if ("+".equals(btnName)) {
            if (plus != null) {
                return insetTowardIconCenter(plus, fieldRight, false);
            }
            return new Point(fieldRight + 52, fieldCenterY);
        }
        throw new IllegalArgumentException("Unsupported Close Position button: " + btnName);
    }

    private Point insetTowardIconCenter(int[] bounds, int fieldEdge, boolean minus) {
        int centerY = (bounds[1] + bounds[3]) / 2;
        int centerX = (bounds[0] + bounds[2]) / 2;
        if (minus) {
            return new Point(Math.min(centerX, fieldEdge - 24), centerY);
        }
        return new Point(Math.max(centerX, fieldEdge + 24), centerY);
    }

    private WebElement lotsField() {
        abs.waitUntilElementVisible(By.className("android.widget.EditText"));
        List<WebElement> fields = driver.findElements(By.className("android.widget.EditText"));
        if (fields.isEmpty()) {
            throw new NoSuchElementException("Could not find lots EditText on Close Position");
        }
        return fields.getFirst();
    }

    private List<int[]> compactControlsOnLotsRow(int rowCenterY) {
        List<int[]> found = new ArrayList<>();
        for (WebElement el : driver.findElements(
                By.xpath("//android.widget.ScrollView//android.view.ViewGroup[@clickable='true']"))) {
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
                if (Math.abs(centerY - rowCenterY) > 80) {
                    continue;
                }
                found.add(bounds);
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return found;
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

    public boolean getHeader(){
        if (!(driver instanceof AndroidDriver)) {
            return false;
        }
        return waitForClosePositionHeader() != null;
    }

    public String getConfirmationValue(String value) {
        List<WebElement> text = driver.findElements(By.className("android.widget.TextView"));
        for (int i = 0; i < text.size(); i++) {
            if (text.get(i).getText().equalsIgnoreCase(value)) {
                if (value.equalsIgnoreCase("Volume")) {
                    return text.get(i + 1).getText().split("Lots")[0].trim();
                } else if (value.equalsIgnoreCase("Contract Value")) {
                    return text.get(i + 1).getText().split("USD")[1].trim().replace(",", "");
                }
                else if (value.equalsIgnoreCase("Floating P/L")) {
                    String token = text.get(i + 1).getText().split(" ")[0].trim();
                    return token.startsWith("+") ? token.substring(1) : token;
                }
                return text.get(i + 1).getText();
            }
        }
        return null;
    }

    public String getFloatingPnL(int contractSize){
        BigDecimal currentPrice = new BigDecimal(getDetailValue("Current Price").trim());
        BigDecimal openPrice = new BigDecimal(getDetailValue("Open Price").trim());
        BigDecimal lotSize = new BigDecimal(AppInstrumentDetailsPage.lotSize.trim());
        BigDecimal contract = BigDecimal.valueOf(contractSize);

        BigDecimal pnl = currentPrice
                .subtract(openPrice)
                .multiply(lotSize)
                .multiply(contract)
                .setScale(2, RoundingMode.HALF_UP);

        return pnl.toPlainString();

    }

    public String normalizeConfirmationValue(String label, String rawValue) {
        if (rawValue == null) {
            return null;
        }

        rawValue = rawValue.trim();

        if (label.equalsIgnoreCase("Volume")) {
            return rawValue.replace("Lots", "").trim();
        }

        if (label.equalsIgnoreCase("Initial Margin")) {
            String[] parts = rawValue.split("USD");
            return parts.length > 1 ? parts[1].trim().replace(",", "") : rawValue.replace(",", "");
        }

        if (label.equalsIgnoreCase("Contract Value")) {
            String currency = abs.getQuoteCurrency(AppMarketsPage.tradeSymbol);
            String[] parts = rawValue.split(currency);
            return parts.length > 1 ? parts[1].trim().replace(",", "") : rawValue.replace(",", "");
        }

        if (label.equalsIgnoreCase("Floating P/L")) {
            String token = rawValue.split(" ")[0].trim();
            return token.startsWith("+") ? token.substring(1) : token;
        }

        return rawValue;
    }

    public String getDetailValue(String label) {
        getPageElement.waitAndCaptureIfNeeded(
                By.xpath("//android.view.ViewGroup[@resource-id='RNE__Overlay']"), 10);
        String uiLabel = getPageElement.mapUiLabel(label);
        String rawValue = getPageElement.readLabelValueFast(uiLabel);
        if (rawValue == null || rawValue.isBlank()) {
            throw new NoSuchElementException("Could not find value on Close Position for label: " + uiLabel);
        }
        return normalizeConfirmationValue(label, rawValue);
    }

    public void confirmPositionClose() {
        abs.waitUntilElementFind(closeBtnConfirmAos);
        closeBtnConfirmAos.click();
    }

    public String getHeaderText() {
        WebElement header = waitForClosePositionHeader();
        if (header == null) {
            return "";
        }
        String text = header.getText();
        if (text == null || text.trim().isEmpty()) {
            text = header.getAttribute("text");
        }
        return text == null ? "" : text;
    }

    private WebElement waitForClosePositionHeader() {
        if (!(driver instanceof AndroidDriver)) {
            return null;
        }
        By overlay = By.xpath("//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]");
        By header = By.xpath("//*[@text='Close Position']");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.ignoring(StaleElementReferenceException.class);
        wait.until(d -> d.findElements(overlay).stream().noneMatch(el -> {
            try {
                return el.isDisplayed();
            } catch (StaleElementReferenceException e) {
                return false;
            }
        }));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(header));
    }
}
