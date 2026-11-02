package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.BaseTest;
import utils.GetPageElement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppWatchlistDetailsPage {

    private static final By SHOW_MORE = By.xpath(
            "//*[@text='show more' or @content-desc='show more' or @text='Show more' or @content-desc='Show more']");
    private static final Pattern SYMBOL_HEADER = Pattern.compile(
            ".+,\\s*[A-Z][A-Z0-9]{2,},\\s*-?\\d+(?:\\.\\d+)?%");
    private static final Pattern POSITION_ROW = Pattern.compile(
            "^(BUY|SELL),\\s*\\d+(?:\\.\\d+)?,\\s*\\S.*");
    private static final Pattern BOUNDS = Pattern.compile("\\[(\\d+),(\\d+)]\\[(\\d+),(\\d+)]");
    private static final int CHEVRON_MAX_WIDTH = 40;

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    private final GetPageElement getPageElement;

    public AppWatchlistDetailsPage(AppiumDriver driver) {
        this.driver = driver;
        this.abs = new MobileAbstractComponents(driver);
        this.getPageElement = new GetPageElement(driver);
        PageFactory.initElements(driver, this);
    }

    public void tapShowMore() {
        requireAndroid();
        // EIEHK lists open positions under the symbol. EBL keeps them collapsed behind show more.
        boolean rowsAlreadyVisible = rowAppearsWithin(2)
                || (!"EBL_MT5".equalsIgnoreCase(BaseTest.productEntity) && rowAppearsWithin(8));
        if (rowsAlreadyVisible) {
            getPageElement.logInfo("Open position rows are already visible on the watchlist");
            return;
        }
        if (!"EBL_MT5".equalsIgnoreCase(BaseTest.productEntity)) {
            throw new TimeoutException(
                    "Open position row was not visible on the " + BaseTest.productEntity + " watchlist");
        }
        abs.tapVisible(SHOW_MORE, 10);
        if (!rowAppearsWithin(10)) {
            throw new TimeoutException("Open position row was not visible after tapping show more");
        }
        getPageElement.logInfo("Tapped show more on the watchlist");
    }

    public void tapOpenPositionCta(String buttonName) {
        requireAndroid();
        String name = buttonName == null ? "" : buttonName.trim().toLowerCase(Locale.ROOT);
        if (!name.equals("detail") && !name.equals("close") && !name.equals("edit")) {
            throw new IllegalArgumentException("Unsupported watchlist position CTA: " + buttonName);
        }
        if ("EBL_MT5".equalsIgnoreCase(BaseTest.productEntity)) {
            tapShowMore();
        } else {
            getPageElement.logInfo("Skip show more for entity " + BaseTest.productEntity);
        }
        WebElement row = waitForPositionRow();
        int[] rowBounds = boundsOf(row);
        if (rowBounds == null) {
            throw new TimeoutException("Could not read bounds for the open position row");
        }
        String rowLabel = safeAttribute(row, "content-desc");
        Point target = switch (name) {
            case "close" -> iconCenter(rowBounds, 0);
            case "edit" -> iconCenter(rowBounds, 1);
            default -> chevronCenter(rowBounds);
        };
        if (target == null) {
            throw new TimeoutException("Could not find " + buttonName + " CTA on the open position row");
        }
        getPageElement.logInfo("Tapping " + buttonName + " CTA on watchlist row '"
                + rowLabel + "' at " + target.getX() + "," + target.getY());
        abs.tapAt(target.getX(), target.getY());
    }

    private void requireAndroid() {
        if (!(driver instanceof AndroidDriver)) {
            throw new UnsupportedOperationException("Watchlist details actions are implemented for Android");
        }
    }

    private boolean rowAppearsWithin(int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> anyOpenPositionRow() != null);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private WebElement waitForPositionRow() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(15))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> positionRowUnderSymbol());
        } catch (TimeoutException e) {
            throw new TimeoutException(
                    "Open position row was not visible under " + requiredSymbol(), e);
        }
    }

    private WebElement anyOpenPositionRow() {
        int minWidth = (int) (driver.manage().window().getSize().getWidth() * 0.7);
        for (WebElement element : driver.findElements(By.xpath("//*[@content-desc]"))) {
            String desc = safeAttribute(element, "content-desc");
            int[] box = boundsOf(element);
            if (desc != null && POSITION_ROW.matcher(desc.trim()).matches()
                    && box != null && box[2] - box[0] >= minWidth) {
                return element;
            }
        }
        return null;
    }

    private WebElement positionRowUnderSymbol() {
        String symbol = requiredSymbol();
        WebElement header = symbolHeader(symbol);
        if (header == null) {
            return null;
        }
        int[] headerBounds = boundsOf(header);
        if (headerBounds == null) {
            return null;
        }
        int nextHeaderTop = nextSymbolHeaderTop(headerBounds[3], symbol);
        int minWidth = (int) (driver.manage().window().getSize().getWidth() * 0.7);
        WebElement best = null;
        int bestTop = Integer.MAX_VALUE;
        for (WebElement element : driver.findElements(By.xpath("//*[@content-desc]"))) {
            String desc = safeAttribute(element, "content-desc");
            int[] box = boundsOf(element);
            if (desc == null || box == null || !POSITION_ROW.matcher(desc.trim()).matches()) {
                continue;
            }
            if (box[2] - box[0] < minWidth || box[1] < headerBounds[3] || box[1] >= nextHeaderTop) {
                continue;
            }
            if (box[1] < bestTop) {
                bestTop = box[1];
                best = element;
            }
        }
        return best;
    }

    private WebElement symbolHeader(String symbol) {
        Pattern token = Pattern.compile("(^|[^A-Z0-9])" + Pattern.quote(symbol) + "([^A-Z0-9]|$)");
        for (int pass = 0; pass < 4; pass++) {
            WebElement header = findSymbolHeader(token);
            if (header != null) {
                return header;
            }
            if (pass < 3) {
                abs.swipeUp(driver);
            }
        }
        return null;
    }

    private WebElement findSymbolHeader(Pattern token) {
        WebElement best = null;
        int bestTop = Integer.MAX_VALUE;
        for (WebElement element : driver.findElements(By.xpath("//*[@content-desc]"))) {
            String desc = safeAttribute(element, "content-desc");
            int[] box = boundsOf(element);
            if (desc == null || box == null || !SYMBOL_HEADER.matcher(desc.trim()).matches()) {
                continue;
            }
            if (!token.matcher(desc).find()) {
                continue;
            }
            if (box[1] < bestTop) {
                bestTop = box[1];
                best = element;
            }
        }
        return best;
    }

    private int nextSymbolHeaderTop(int afterY, String currentSymbol) {
        Pattern token = Pattern.compile("(^|[^A-Z0-9])" + Pattern.quote(currentSymbol) + "([^A-Z0-9]|$)");
        int nextTop = Integer.MAX_VALUE;
        for (WebElement element : driver.findElements(By.xpath("//*[@content-desc]"))) {
            String desc = safeAttribute(element, "content-desc");
            int[] box = boundsOf(element);
            if (desc == null || box == null || box[1] <= afterY) {
                continue;
            }
            if (!SYMBOL_HEADER.matcher(desc.trim()).matches() || token.matcher(desc).find()) {
                continue;
            }
            nextTop = Math.min(nextTop, box[1]);
        }
        return nextTop;
    }

    private Point iconCenter(int[] rowBounds, int index) {
        List<int[]> icons = new ArrayList<>();
        for (WebElement element : driver.findElements(By.xpath("//*[@clickable='true']"))) {
            int[] box = boundsOf(element);
            if (box == null || !contained(rowBounds, box)) {
                continue;
            }
            int width = box[2] - box[0];
            if (width <= CHEVRON_MAX_WIDTH || width > 180) {
                continue;
            }
            icons.add(box);
        }
        icons.sort(Comparator.comparingInt(box -> box[0]));
        if (icons.size() <= index) {
            return null;
        }
        return center(icons.get(index));
    }

    private Point chevronCenter(int[] rowBounds) {
        int[] best = null;
        for (WebElement element : driver.findElements(By.className("com.horcrux.svg.SvgView"))) {
            int[] box = boundsOf(element);
            if (box == null || !contained(rowBounds, box)) {
                continue;
            }
            int width = box[2] - box[0];
            if (width <= 0 || width > CHEVRON_MAX_WIDTH) {
                continue;
            }
            if (best == null || box[0] > best[0]) {
                best = box;
            }
        }
        return best == null ? null : center(best);
    }

    private boolean contained(int[] outer, int[] inner) {
        return inner[0] >= outer[0]
                && inner[2] <= outer[2]
                && inner[1] >= outer[1] - 4
                && inner[3] <= outer[3] + 4;
    }

    private Point center(int[] box) {
        return new Point((box[0] + box[2]) / 2, (box[1] + box[3]) / 2);
    }

    private int[] boundsOf(WebElement element) {
        String bounds = safeAttribute(element, "bounds");
        if (bounds == null) {
            return null;
        }
        Matcher matcher = BOUNDS.matcher(bounds);
        if (!matcher.find()) {
            return null;
        }
        int left = Integer.parseInt(matcher.group(1));
        int top = Integer.parseInt(matcher.group(2));
        int right = Integer.parseInt(matcher.group(3));
        int bottom = Integer.parseInt(matcher.group(4));
        if (right <= left || bottom <= top) {
            return null;
        }
        return new int[]{left, top, right, bottom};
    }

    private String safeAttribute(WebElement element, String name) {
        try {
            String value = element.getAttribute(name);
            if (value == null || value.isBlank() || "null".equalsIgnoreCase(value)) {
                return null;
            }
            return value;
        } catch (StaleElementReferenceException e) {
            return null;
        }
    }

    private String requiredSymbol() {
        String symbol = AppMarketsPage.tradeSymbol;
        if (symbol == null || symbol.isBlank()) {
            throw new TimeoutException("No symbol is selected for the watchlist position row");
        }
        return symbol.trim();
    }
}
