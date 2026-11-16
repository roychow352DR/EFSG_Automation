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
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppModifyOrderPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    public static String editPrice;
    public static boolean SCROLLED = false;

    public static void resetCapturedOrderValues() {
        editPrice = null;
        SCROLLED = false;
    }

    public AppModifyOrderPage(AppiumDriver driver) {
        this.driver = driver;
        this.abs = new MobileAbstractComponents(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[15]")
    WebElement closeBtnAos;


    public String getEditPrice(String direction, String decimal, String priceType, int value) {
        String price = "";
        if (driver instanceof AndroidDriver) {
            float current = Float.parseFloat(priceField(priceType).getText());
            if (priceType.equalsIgnoreCase("Stop Limit")) {
                price = Float.toString(direction.equalsIgnoreCase("BUY") ? current + value : current - value);
            } else if (priceType.equalsIgnoreCase("Stop Loss")) {
                price = Float.toString(direction.equalsIgnoreCase("BUY") ? current - value : current + value);
            } else {
                price = Float.toString(direction.equalsIgnoreCase("BUY") ? current + value : current - value);
            }
        }
        editPrice = abs.normalizePriceToDecimals(price, decimal);
        return price;
    }

    public void editTextField(String priceType, String direction, String decimal, int value) {
        if (driver instanceof AndroidDriver) {
            String editedPrice = getEditPrice(direction, decimal, priceType, value);
            WebElement field = priceField(priceType);
            field.clear();
            abs.typeWithAndroidKeys((AndroidDriver) driver, field, editedPrice);
        }
    }

    private WebElement priceField(String priceType) {
        WebElement label = priceLabel(priceType);
        int[] labelBounds = parseBounds(label.getAttribute("bounds"));

        WebElement closest = null;
        int bestScore = Integer.MAX_VALUE;
        for (WebElement field : driver.findElements(By.className("android.widget.EditText"))) {
            int[] fieldBounds = parseBounds(field.getAttribute("bounds"));
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
            throw new NoSuchElementException(
                    "Could not find EditText for price type: " + priceType
            );
        }
        return closest;
    }

    private WebElement priceLabel(String priceType) {
        String labelToken = switch (priceType) {
            case "Stop Loss" -> "Stop Loss";
            case "Take Profit" -> "Take Profit";
            default -> "Price (";
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

    public void tapsButton(String buttonName) {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        abs.tapBottomMost(By.xpath("//*[@text='" + buttonName + "']"), 10);
    }

    public void scrollDown() {
        SCROLLED = true;
        abs.swipeUpUntilEnd(driver);
    }

    public boolean getTextMessage(String messageContent) {
        if (!(driver instanceof AndroidDriver)) {
            return false;
        }
        By error = By.xpath(
                "//*[@text='" + messageContent + "' or @content-desc='" + messageContent + "'"
                        + " or contains(@text,'" + messageContent + "')"
                        + " or contains(@content-desc,'" + messageContent + "')]");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(8))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> visibleMessage(d.findElements(error), messageContent));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean visibleMessage(List<WebElement> elements, String messageContent) {
        for (WebElement ele : elements) {
            try {
                if (!ele.isDisplayed()) {
                    continue;
                }
                String text = ele.getText();
                if (text != null && text.toLowerCase().contains(messageContent.toLowerCase())) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return false;
    }

    public void tapBack() {
        new AppTradeView(driver).tapBack();
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
    public boolean getHeader() {
        return !getHeaderText().isEmpty();
    }

    // Read the compact Modify Order title. A missing header stays empty so the assertion fails.
    public String getHeaderText() {
        if (!(driver instanceof AndroidDriver)) {
            return "";
        }
        try {
            WebElement header = new WebDriverWait(driver, Duration.ofSeconds(10))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> compactModifyOrderHeader());
            if (header == null) {
                return "";
            }
            String text = header.getText();
            if (text == null || text.isBlank()) {
                text = header.getAttribute("text");
            }
            if (text == null || text.isBlank()) {
                text = header.getAttribute("content-desc");
            }
            return text == null ? "" : text.trim();
        } catch (TimeoutException e) {
            return "";
        }
    }

    private WebElement compactModifyOrderHeader() {
        try {
            Dimension window = driver.manage().window().getSize();
            int maxHeaderY = (int) (window.getHeight() * 0.45);
            WebElement best = null;
            int bestY = Integer.MAX_VALUE;
            for (WebElement el : driver.findElements(By.xpath(
                    "//*[@text='Modify Order' or @content-desc='Modify Order']"))) {
                Point location = el.getLocation();
                Dimension size = el.getSize();
                if (location.getY() > maxHeaderY || size.getHeight() > 220) {
                    continue;
                }
                if (location.getY() < bestY) {
                    bestY = location.getY();
                    best = el;
                }
            }
            return best;
        } catch (StaleElementReferenceException e) {
            return null;
        }
    }

    // Modify Order may nest the Switch instead of placing it as the label's next sibling.
    // Pending Order is a different row, so the switch is chosen from the Take Profit / Stop Loss row.
    private static final By PROFIT_OR_LOSS_LABEL = By.xpath(
            "//*[contains(@text,'Profit') or contains(@text,'Loss')"
                    + " or contains(@content-desc,'Profit') or contains(@content-desc,'Loss')]"
    );

    public void switchOnTakeProfitAndStopLoss() {
        requireAndroid("Take Profit and Stop Loss on the modify order page is implemented for Android only");
        if (getHeaderText().isEmpty()) {
            throw new TimeoutException("Modify Order header was not visible");
        }
        if (revealTargetProfitLabel() == null) {
            throw new TimeoutException(
                    "Take Profit and Stop Loss switch was not visible on the modify order page. Labels: "
                            + profitLossLabelSample());
        }
        turnSwitch(true);
    }

    public void switchOffTakeProfitAndStopLoss() {
        requireAndroid("Take Profit and Stop Loss on the modify order page is implemented for Android only");
        if (getHeaderText().isEmpty()) {
            throw new TimeoutException("Modify Order header was not visible");
        }
        if (revealTargetProfitLabel() == null) {
            throw new TimeoutException(
                    "Take Profit and Stop Loss switch was not visible on the modify order page. Labels: "
                            + profitLossLabelSample());
        }
        turnSwitch(false);
    }

    public boolean isTakeProfitStopLossOn() {
        requireAndroid("Take Profit and Stop Loss on the modify order page is implemented for Android only");
        WebElement toggle = shownTargetProfitSwitch();
        if (toggle == null) {
            revealTargetProfitLabel();
            toggle = shownTargetProfitSwitch();
        }
        Boolean checked = checkedState(toggle);
        if (checked == null) {
            throw new NoSuchElementException(
                    "Take Profit and Stop Loss switch checked state was not readable on the modify order page. Labels: "
                            + profitLossLabelSample());
        }
        return checked;
    }

    private void requireAndroid(String message) {
        if (!(driver instanceof AndroidDriver)) {
            throw new UnsupportedOperationException(message);
        }
    }

    private void turnSwitch(boolean desiredOn) {
        WebElement toggle = new WebDriverWait(driver, Duration.ofSeconds(8))
                .ignoring(StaleElementReferenceException.class)
                .until(d -> shownTargetProfitSwitch());
        if (toggle != null && isSwitchChecked(toggle) == desiredOn) {
            return;
        }
        Point center = toggle == null ? null : shownCenter(toggle);
        if (center == null) {
            throw new TimeoutException("Take Profit and Stop Loss switch was not visible on the modify order page");
        }
        abs.tapAt(center.getX(), center.getY());
        new WebDriverWait(driver, Duration.ofSeconds(8))
                .ignoring(StaleElementReferenceException.class)
                .until(d -> {
                    WebElement current = shownTargetProfitSwitch();
                    return current != null && isSwitchChecked(current) == desiredOn;
                });
    }

    private WebElement revealTargetProfitLabel() {
        for (int swipe = 0; swipe < 6; swipe++) {
            WebElement label = targetProfitLabel();
            if (label != null) {
                return label;
            }
            abs.swipeUp(driver);
        }
        return targetProfitLabel();
    }

    private WebElement shownTargetProfitSwitch() {
        WebElement label = targetProfitLabel();
        if (label == null) {
            return null;
        }
        WebElement toggle = nearestSwitchOnRow(label, driver.findElements(By.className("android.widget.Switch")));
        if (toggle != null) {
            return toggle;
        }
        return nearestSwitchOnRow(label, driver.findElements(By.xpath("//*[@checkable='true']")));
    }

    private WebElement targetProfitLabel() {
        WebElement fallback = null;
        try {
            for (WebElement element : driver.findElements(PROFIT_OR_LOSS_LABEL)) {
                String text = visibleText(element);
                if (!isToggleLabel(text) || shownCenter(element) == null) {
                    continue;
                }
                String lower = text.toLowerCase(Locale.ROOT);
                if (lower.contains("stop loss")
                        && (lower.contains("take profit") || lower.contains("target profit"))) {
                    return element;
                }
                if (fallback == null) {
                    fallback = element;
                }
            }
        } catch (StaleElementReferenceException e) {
            return null;
        }
        return fallback;
    }

    private boolean isToggleLabel(String text) {
        String lower = text.toLowerCase(Locale.ROOT);
        if (lower.isBlank()) {
            return false;
        }
        if (lower.contains("(")) {
            return false;
        }
        if (lower.startsWith("set target profit") || lower.contains("set target profit")) {
            return true;
        }
        boolean stopLoss = lower.contains("stop loss");
        boolean profit = lower.contains("take profit") || lower.contains("target profit");
        return stopLoss && profit;
    }

    private WebElement nearestSwitchOnRow(WebElement label, List<WebElement> toggles) {
        int[] labelBox = elementBox(label);
        if (labelBox == null) {
            return null;
        }
        int labelCenterY = (labelBox[1] + labelBox[3]) / 2;
        int tolerance = Math.max(labelBox[3] - labelBox[1], 72);
        WebElement best = null;
        int bestScore = Integer.MAX_VALUE;
        for (WebElement toggle : toggles) {
            try {
                if (toggle.equals(label)) {
                    continue;
                }
                int[] toggleBox = elementBox(toggle);
                Point center = shownCenter(toggle);
                if (toggleBox == null || center == null) {
                    continue;
                }
                int toggleCenterY = (toggleBox[1] + toggleBox[3]) / 2;
                if (Math.abs(toggleCenterY - labelCenterY) > tolerance) {
                    continue;
                }
                if (toggleBox[2] < labelBox[0]) {
                    continue;
                }
                int score = Math.abs(toggleCenterY - labelCenterY) * 10 + Math.abs(toggleBox[0] - labelBox[2]);
                if (score < bestScore) {
                    bestScore = score;
                    best = toggle;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return best;
    }

    private String visibleText(WebElement element) {
        String text = element.getAttribute("text");
        if (text == null || text.isBlank()) {
            text = element.getText();
        }
        if (text == null || text.isBlank()) {
            text = element.getAttribute("content-desc");
        }
        return text == null ? "" : text.replace('\n', ' ').trim();
    }

    private String profitLossLabelSample() {
        List<String> lines = new ArrayList<>();
        try {
            for (WebElement element : driver.findElements(PROFIT_OR_LOSS_LABEL)) {
                String text = visibleText(element);
                if (text.isBlank() || lines.contains(text)) {
                    continue;
                }
                lines.add(text);
                if (lines.size() == 8) {
                    break;
                }
            }
        } catch (StaleElementReferenceException ignored) {
        }
        return lines.isEmpty() ? "(none)" : String.join(" | ", lines);
    }

    private Boolean checkedState(WebElement toggle) {
        if (toggle == null) {
            return null;
        }
        String checked = toggle.getAttribute("checked");
        if (checked == null || "null".equalsIgnoreCase(checked)) {
            return null;
        }
        return Boolean.parseBoolean(checked);
    }

    private int[] elementBox(WebElement element) {
        int[] bounds = parseBounds(element.getAttribute("bounds"));
        if (bounds != null && bounds[2] > bounds[0] && bounds[3] > bounds[1]) {
            return bounds;
        }
        Point location = element.getLocation();
        Dimension size = element.getSize();
        if (size.getWidth() <= 0 || size.getHeight() <= 0) {
            return null;
        }
        return new int[]{
                location.getX(),
                location.getY(),
                location.getX() + size.getWidth(),
                location.getY() + size.getHeight()
        };
    }

    // React Native often reports getLocation() as 0,0 while UiAutomator bounds stay on screen.
    private Point shownCenter(WebElement element) {
        int[] bounds = parseBounds(element.getAttribute("bounds"));
        if (bounds != null && bounds[2] > bounds[0] && bounds[3] > bounds[1]) {
            Point center = new Point((bounds[0] + bounds[2]) / 2, (bounds[1] + bounds[3]) / 2);
            if (isInsideWindow(center)) {
                return center;
            }
        }
        if (!element.isDisplayed()) {
            return null;
        }
        Point location = element.getLocation();
        Dimension size = element.getSize();
        if (size.getWidth() <= 0 || size.getHeight() <= 0) {
            return null;
        }
        Point center = new Point(location.getX() + size.getWidth() / 2, location.getY() + size.getHeight() / 2);
        return isInsideWindow(center) ? center : null;
    }

    private boolean isInsideWindow(Point point) {
        Dimension window = driver.manage().window().getSize();
        int top = (int) (window.getHeight() * 0.04);
        int bottom = (int) (window.getHeight() * 0.98);
        return point.getX() > 0
                && point.getX() < window.getWidth()
                && point.getY() > top
                && point.getY() < bottom;
    }

    private boolean isSwitchChecked(WebElement toggle) {
        String checked = toggle.getAttribute("checked");
        return checked != null && Boolean.parseBoolean(checked);
    }

}
