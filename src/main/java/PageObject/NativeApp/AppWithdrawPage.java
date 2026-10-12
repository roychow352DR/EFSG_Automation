package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.GetPageElement;

import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppWithdrawPage {

    private static final String HEADER = "Withdraw";
    private static final Duration HEADER_WAIT = Duration.ofSeconds(60);

    private final AppiumDriver driver;
    private final GetPageElement getPageElement;
    private final MobileAbstractComponents abs;

    public AppWithdrawPage(AppiumDriver driver) {
        this.driver = driver;
        this.getPageElement = new GetPageElement(driver);
        this.abs = new MobileAbstractComponents(driver);
    }

    public void tapBack() {
        if (!(driver instanceof AndroidDriver)) {
            throw new TimeoutException("Withdraw back is implemented for Android only");
        }
        if (!HEADER.equals(waitForHeader())) {
            throw new TimeoutException("Withdraw header was not visible before tapping back");
        }
        tapHeaderBack("Tapping Withdraw header back at ");
        if (!leftWithdraw(3)) {
            getPageElement.logInfo("Still on Withdraw after back; tapping header back again");
            tapHeaderBack("Tapping Withdraw header back again at ");
        }
        if (!leftWithdraw(3)) {
            getPageElement.logInfo("Still on Withdraw after header taps; pressing Android back");
            ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.BACK));
        }
        if (!leftWithdraw(8)) {
            throw new TimeoutException("Withdraw header was still visible after tapping back");
        }
    }

    private void tapHeaderBack(String logPrefix) {
        Point point = headerBackPoint();
        getPageElement.logInfo(logPrefix + point.getX() + "," + point.getY());
        abs.tapAt(point.getX(), point.getY());
    }

    private boolean leftWithdraw(int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(Math.max(1, seconds)))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> portfolioMarkersVisible() || !withdrawTitleVisible());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private String waitForHeader() {
        if (!(driver instanceof AndroidDriver)) {
            return "";
        }
        return new WebDriverWait(driver, HEADER_WAIT)
                .ignoring(StaleElementReferenceException.class)
                .withMessage("Withdraw header was not visible before tapping back")
                .until(d -> resolvedHeader());
    }

    private String resolvedHeader() {
        if (stillOnPortfolio()) {
            return null;
        }
        Dimension window = driver.manage().window().getSize();
        int maxHeaderY = (int) (window.getHeight() * 0.28);
        int bestY = Integer.MAX_VALUE;
        boolean found = false;
        for (WebElement el : driver.findElements(By.xpath(
                "//*[@text='Withdraw' or @content-desc='Withdraw']"
        ))) {
            try {
                String text = firstNonBlank(el.getText(), el.getAttribute("text"), el.getAttribute("content-desc"));
                if (!HEADER.equals(text)) {
                    continue;
                }
                int[] box = parseBounds(el.getAttribute("bounds"));
                if (box == null) {
                    continue;
                }
                int top = box[1];
                int width = box[2] - box[0];
                int height = box[3] - box[1];
                if (width <= 0 || height <= 0 || height > 160 || top > maxHeaderY) {
                    continue;
                }
                if (top < bestY) {
                    bestY = top;
                    found = true;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        if (!found) {
            return null;
        }
        getPageElement.logInfo("Withdraw header visible before back at y=" + bestY);
        return HEADER;
    }

    private Point headerBackPoint() {
        Dimension window = driver.manage().window().getSize();
        int maxHeaderY = (int) (window.getHeight() * 0.20);
        Point labeled = labeledHeaderBack(maxHeaderY);
        if (labeled != null) {
            return labeled;
        }
        Point icon = topLeftHeaderIcon(maxHeaderY);
        if (icon != null) {
            return icon;
        }
        Point chevron = topLeftHeaderControl(maxHeaderY);
        if (chevron != null) {
            return chevron;
        }
        int x = Math.max(48, (int) (window.getWidth() * 0.066));
        int y = Math.max(200, (int) (window.getHeight() * 0.084));
        y = Math.min(y, (int) (window.getHeight() * 0.12));
        return new Point(x, y);
    }

    private Point topLeftHeaderIcon(int maxHeaderY) {
        for (By locator : List.of(
                By.className("com.horcrux.svg.SvgView"),
                By.className("android.widget.ImageView"))) {
            Point icon = topLeftMatch(locator, maxHeaderY);
            if (icon != null) {
                return icon;
            }
        }
        return null;
    }

    private Point topLeftMatch(By locator, int maxHeaderY) {
        Point best = null;
        int bestScore = Integer.MAX_VALUE;
        for (WebElement el : driver.findElements(locator)) {
            try {
                int[] box = parseBounds(el.getAttribute("bounds"));
                if (!isHeaderControl(box, maxHeaderY)) {
                    continue;
                }
                int score = box[0] * 10 + box[1];
                if (score < bestScore) {
                    bestScore = score;
                    best = center(box);
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return best;
    }

    private boolean withdrawTitleVisible() {
        if (stillOnPortfolio()) {
            return false;
        }
        Dimension window = driver.manage().window().getSize();
        // Title row only. A Portfolio Withdraw action sits lower than this band.
        int maxHeaderY = (int) (window.getHeight() * 0.20);
        for (WebElement el : driver.findElements(By.xpath(
                "//*[@text='Withdraw' or @content-desc='Withdraw']"))) {
            try {
                String text = firstNonBlank(el.getText(), el.getAttribute("text"), el.getAttribute("content-desc"));
                if (!HEADER.equals(text)) {
                    continue;
                }
                int[] box = parseBounds(el.getAttribute("bounds"));
                if (box == null) {
                    continue;
                }
                int width = box[2] - box[0];
                int height = box[3] - box[1];
                if (width <= 0 || height <= 0 || height > 160 || box[1] > maxHeaderY) {
                    continue;
                }
                return true;
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return false;
    }

    private boolean portfolioMarkersVisible() {
        Dimension window = driver.manage().window().getSize();
        int maxY = (int) (window.getHeight() * 0.70);
        if (compactLabelVisible(
                "//*[@text='Show all' or @text='Show All' or @content-desc='Show all'"
                        + " or contains(@text,'Show last') or contains(@content-desc,'Show last')"
                        + " or @text='History' or @content-desc='History'"
                        + " or @text='Newest to Oldest' or @content-desc='Newest to Oldest']",
                maxY,
                false)) {
            return true;
        }
        return compactLabelVisible(
                "//*[@text='Open Positions' or @content-desc='Open Positions'"
                        + " or @text='Open\nPositions' or @content-desc='Open\nPositions']",
                maxY,
                true);
    }

    private boolean compactLabelVisible(String xpath, int maxY, boolean requireHeight) {
        for (WebElement el : driver.findElements(By.xpath(xpath))) {
            try {
                Point location = el.getLocation();
                Dimension size = el.getSize();
                if (location.getY() >= maxY || size.getHeight() > 140) {
                    continue;
                }
                if (requireHeight && size.getHeight() <= 0) {
                    continue;
                }
                return true;
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return false;
    }

    private Point labeledHeaderBack(int maxHeaderY) {
        for (WebElement el : driver.findElements(By.xpath(
                "//*[@content-desc='Back' or @content-desc='Navigate up' or @content-desc='back']"
        ))) {
            try {
                int[] box = parseBounds(el.getAttribute("bounds"));
                if (!isHeaderControl(box, maxHeaderY)) {
                    continue;
                }
                return center(box);
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return null;
    }

    private Point topLeftHeaderControl(int maxHeaderY) {
        Point best = null;
        int bestScore = Integer.MAX_VALUE;
        for (WebElement el : driver.findElements(By.xpath("//android.view.ViewGroup[@clickable='true']"))) {
            try {
                int[] box = parseBounds(el.getAttribute("bounds"));
                if (!isHeaderControl(box, maxHeaderY)) {
                    continue;
                }
                int score = box[0] * 10 + box[1];
                if (score < bestScore) {
                    bestScore = score;
                    best = center(box);
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return best;
    }

    private boolean isHeaderControl(int[] box, int maxHeaderY) {
        if (box == null) {
            return false;
        }
        int width = box[2] - box[0];
        int height = box[3] - box[1];
        return width >= 16 && width <= 180
                && height >= 16 && height <= 180
                && box[0] <= 220
                && box[1] <= maxHeaderY;
    }

    private Point center(int[] box) {
        return new Point((box[0] + box[2]) / 2, (box[1] + box[3]) / 2);
    }

    private boolean stillOnPortfolio() {
        return !driver.findElements(By.xpath(
                "//*[@text='Open Positions' or @content-desc='Open Positions'"
                        + " or @text='Open\nPositions' or @content-desc='Open\nPositions'"
                        + " or @text='Newest to Oldest' or @content-desc='Newest to Oldest']"
        )).isEmpty();
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
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
