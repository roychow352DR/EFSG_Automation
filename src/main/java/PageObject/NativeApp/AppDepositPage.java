package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.GetPageElement;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppDepositPage {

    private static final Duration HEADER_WAIT = Duration.ofSeconds(60);

    private final AppiumDriver driver;
    private final GetPageElement getPageElement;
    private final MobileAbstractComponents abs;

    public AppDepositPage(AppiumDriver driver) {
        this.driver = driver;
        this.getPageElement = new GetPageElement(driver);
        this.abs = new MobileAbstractComponents(driver);
    }

    public void tapBack() {
        if (!(driver instanceof AndroidDriver)) {
            throw new TimeoutException("Deposit back is implemented for Android only");
        }
        String header = getHeader("Deposit");
        if (!"Deposit".equals(header)) {
            throw new TimeoutException("Deposit header was not visible before tapping back");
        }
        Point point = headerBackPoint();
        getPageElement.logInfo("Tapping Deposit header back at " + point.getX() + "," + point.getY());
        abs.tapAt(point.getX(), point.getY());
    }

    public String getHeader(String expectedHeader) {
        if (!(driver instanceof AndroidDriver) || !isFundingHeader(expectedHeader)) {
            return "";
        }
        return new WebDriverWait(driver, HEADER_WAIT)
                .ignoring(StaleElementReferenceException.class)
                .withMessage(expectedHeader + " header was not visible after redirect")
                .until(d -> resolvedHeader(expectedHeader));
    }

    private boolean isFundingHeader(String expectedHeader) {
        return "Deposit".equals(expectedHeader)
                || "Withdraw".equals(expectedHeader)
                || "Records".equals(expectedHeader);
    }

    private String resolvedHeader(String expectedHeader) {
        if (stillOnPortfolio()) {
            return null;
        }
        Dimension window = driver.manage().window().getSize();
        int maxHeaderY = (int) (window.getHeight() * 0.28);
        int bestY = Integer.MAX_VALUE;
        boolean found = false;
        for (WebElement el : driver.findElements(By.xpath(
                "//*[@text='" + expectedHeader + "' or @content-desc='" + expectedHeader + "']"
        ))) {
            try {
                String text = firstNonBlank(el.getText(), el.getAttribute("text"), el.getAttribute("content-desc"));
                if (text == null || !text.equals(expectedHeader)) {
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
        getPageElement.logInfo(expectedHeader + " header visible after redirect at y=" + bestY);
        return expectedHeader;
    }

    private Point headerBackPoint() {
        Dimension window = driver.manage().window().getSize();
        int maxHeaderY = (int) (window.getHeight() * 0.20);
        Point labeled = labeledHeaderBack(maxHeaderY);
        if (labeled != null) {
            return labeled;
        }
        Point chevron = topLeftHeaderControl(maxHeaderY);
        if (chevron != null) {
            return chevron;
        }
        return new Point(Math.max(40, window.getWidth() / 14), Math.max(80, (int) (window.getHeight() * 0.08)));
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
