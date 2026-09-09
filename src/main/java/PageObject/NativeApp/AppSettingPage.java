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
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppSettingPage {

    private static final By TRADE_CONFIRMATION_LABEL = By.xpath(
            "//*[@text='Trade Confirmation' or contains(@text,'Trade Confirmation')"
                    + " or @content-desc='Trade Confirmation']"
    );
    private static final By SETTINGS_HEADER = By.xpath(
            "//android.widget.TextView[@text='Settings' or @text='Setting']"
    );

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    public static boolean isTradeConfirmNeeded = true;

    public AppSettingPage(AppiumDriver driver) {
        this.driver = driver;
        abs = new MobileAbstractComponents(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    public void tradeSettingsToggleOff() {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        revealTradeConfirmationRow();
        if (!isTradeConfirmationEnabled()) {
            System.out.println("Trade Confirmation has already been disabled");
            isTradeConfirmNeeded = false;
            return;
        }
        tapTradeConfirmationSwitch();
        confirmDisableIfPrompted();
        isTradeConfirmNeeded = false;
    }

    public void tradeSettingsToggleOn() {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        revealTradeConfirmationRow();
        if (isTradeConfirmationEnabled()) {
            System.out.println("Trade Confirmation has already been enabled");
            isTradeConfirmNeeded = true;
            return;
        }
        tapTradeConfirmationSwitch();
        isTradeConfirmNeeded = true;
    }

    public void tabBack() {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        for (By locator : List.of(
                By.xpath("//*[@content-desc='Back']"),
                By.xpath("//*[@content-desc='Navigate up']"),
                By.xpath("//*[@content-desc='back']")
        )) {
            try {
                abs.tapVisible(locator, 4);
                return;
            } catch (TimeoutException ignored) {
            }
        }
        WebElement title = settingsHeader();
        if (title != null) {
            try {
                Point location = title.getLocation();
                Dimension size = title.getSize();
                Dimension window = driver.manage().window().getSize();
                int y = location.getY() + Math.max(8, size.getHeight() / 2);
                int x = Math.min(Math.max(36, window.getWidth() / 14), Math.max(36, location.getX() - 24));
                abs.tapAt(x, y);
                return;
            } catch (StaleElementReferenceException ignored) {
            }
        }
        Dimension window = driver.manage().window().getSize();
        abs.tapAt(Math.max(40, window.getWidth() / 14), Math.max(80, (int) (window.getHeight() * 0.08)));
    }

    private void revealTradeConfirmationRow() {
        waitForSettingsPage();
        for (int swipe = 0; swipe < 5; swipe++) {
            if (firstVisible(TRADE_CONFIRMATION_LABEL) != null) {
                return;
            }
            abs.swipeUp(driver);
        }
        throw new NoSuchElementException("Trade Confirmation row was not visible on the settings page");
    }

    private void waitForSettingsPage() {
        new WebDriverWait(driver, Duration.ofSeconds(12))
                .ignoring(StaleElementReferenceException.class)
                .until(d -> firstVisible(TRADE_CONFIRMATION_LABEL) != null
                        || settingsHeader() != null
                        || !d.findElements(By.className("android.widget.Switch")).isEmpty());
    }

    private WebElement settingsHeader() {
        Dimension window = driver.manage().window().getSize();
        int maxY = (int) (window.getHeight() * 0.22);
        int maxHeight = 160;
        try {
            for (WebElement element : driver.findElements(SETTINGS_HEADER)) {
                try {
                    if (!element.isDisplayed()) {
                        continue;
                    }
                    Dimension size = element.getSize();
                    if (element.getLocation().getY() <= maxY && size.getHeight() <= maxHeight) {
                        return element;
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }
        } catch (RuntimeException ignored) {
        }
        return null;
    }

    private boolean isTradeConfirmationEnabled() {
        WebElement toggle = tradeConfirmationToggle();
        if (toggle != null) {
            Boolean checked = checkedState(toggle);
            if (checked != null) {
                return checked;
            }
        }
        WebElement label = firstVisible(TRADE_CONFIRMATION_LABEL);
        if (label != null) {
            String rowState = sameRowOnOffText(label);
            if ("on".equals(rowState)) {
                return true;
            }
            if ("off".equals(rowState)) {
                return false;
            }
        }
        throw new NoSuchElementException("Could not read Trade Confirmation toggle state");
    }

    private void tapTradeConfirmationSwitch() {
        WebElement toggle = tradeConfirmationToggle();
        if (toggle != null) {
            tapRightSide(toggle);
            return;
        }
        try {
            abs.tapOnSameRowRight(TRADE_CONFIRMATION_LABEL, 8);
        } catch (TimeoutException e) {
            throw new NoSuchElementException("Could not tap Trade Confirmation switch");
        }
    }

    private WebElement tradeConfirmationToggle() {
        WebElement label = firstVisible(TRADE_CONFIRMATION_LABEL);
        if (label == null) {
            return null;
        }
        int[] labelBounds = parseBounds(elementAttribute(label, "bounds"));
        if (labelBounds == null) {
            return null;
        }
        WebElement best = null;
        int bestScore = Integer.MAX_VALUE;
        for (WebElement candidate : toggleCandidates()) {
            int[] bounds = parseBounds(elementAttribute(candidate, "bounds"));
            if (bounds == null || !overlapsVertically(labelBounds, bounds)) {
                continue;
            }
            int centerX = (bounds[0] + bounds[2]) / 2;
            if (centerX < labelBounds[0]) {
                continue;
            }
            int score = Math.abs(centerX - labelBounds[2]);
            if (score < bestScore) {
                bestScore = score;
                best = candidate;
            }
        }
        return best;
    }

    private List<WebElement> toggleCandidates() {
        List<WebElement> candidates = driver.findElements(By.className("android.widget.Switch"));
        if (candidates.isEmpty()) {
            candidates = driver.findElements(By.xpath("//*[@checkable='true']"));
        }
        return candidates;
    }

    private void confirmDisableIfPrompted() {
        for (String label : List.of("Confirm", "OK", "Yes")) {
            By locator = By.xpath("//*[@text='" + label + "']");
            try {
                abs.tapBottomMost(locator, 6);
                System.out.println("Confirmed Trade Confirmation disable with [" + label + "]");
                return;
            } catch (TimeoutException ignored) {
            }
        }
        System.out.println("No confirmation dialog after disabling Trade Confirmation");
    }

    private Boolean checkedState(WebElement element) {
        for (String attr : List.of("checked", "selected")) {
            String value = elementAttribute(element, attr);
            if (value != null) {
                return Boolean.parseBoolean(value);
            }
        }
        return null;
    }

    private String sameRowOnOffText(WebElement label) {
        int[] labelBounds = parseBounds(elementAttribute(label, "bounds"));
        if (labelBounds == null) {
            return null;
        }
        for (WebElement node : driver.findElements(By.xpath("//*[@text='On' or @text='Off' or @text='ON' or @text='OFF']"))) {
            int[] bounds = parseBounds(elementAttribute(node, "bounds"));
            if (bounds == null || !overlapsVertically(labelBounds, bounds)) {
                continue;
            }
            String text = elementAttribute(node, "text");
            if (text != null) {
                return text.trim().toLowerCase();
            }
        }
        return null;
    }

    private WebElement firstVisible(By locator) {
        try {
            for (WebElement element : driver.findElements(locator)) {
                try {
                    if (element.isDisplayed()) {
                        return element;
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }
        } catch (RuntimeException ignored) {
        }
        return null;
    }

    private void tapRightSide(WebElement element) {
        Point location = element.getLocation();
        Dimension size = element.getSize();
        int x = location.getX() + Math.max(size.getWidth() * 3 / 4, size.getWidth() / 2);
        int y = location.getY() + Math.max(8, size.getHeight() / 2);
        abs.tapAt(x, y);
    }

    private boolean overlapsVertically(int[] a, int[] b) {
        int aCenter = (a[1] + a[3]) / 2;
        return aCenter >= b[1] - 12 && aCenter <= b[3] + 12;
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
}
