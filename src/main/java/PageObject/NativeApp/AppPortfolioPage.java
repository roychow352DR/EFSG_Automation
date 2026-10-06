package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumBy;
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
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppPortfolioPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;

    public AppPortfolioPage(AppiumDriver driver) {
        this.driver = driver;
        this.abs = new MobileAbstractComponents(driver);
        //    PageFactory.initElements(driver, this);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout//android.view.ViewGroup[3]//android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]")
    WebElement applicationButtonAos;

    @FindBy(className = "android.view.ViewGroup")
    List<WebElement> buttons;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup/android.view.ViewGroup[3]" +
            "/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.widget.TextView")
    WebElement titleAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup")
    List<WebElement> products;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup")
    WebElement checkedIconAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup" +
            "/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]" +
            "/android.view.ViewGroup/android.view.ViewGroup")
    WebElement backButtonAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[1]")
    WebElement arrowBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[1]")
    WebElement closeBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[2]")
    WebElement editBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]")
    WebElement cancelBtnAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]")
    WebElement confirmationDialogueAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[14]")
    WebElement confirmBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.widget.TextView[6]")
    WebElement dateAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]")
    WebElement cancelOrderBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[3]")
    WebElement detailBtnAos;

    public void tapButtonOnPortfolioPage(String buttonName) {
        if (buttonName.equals("Open a Live Trading Accounts")) {
            abs.waitUntilElementFind(applicationButtonAos);
            applicationButtonAos.click();
        }
        else if (buttonName.equalsIgnoreCase("Cancel Order")) {
            confirmCancelOrder();
        }
    }

    public void clickButton(String buttonName) {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        waitForPortfolioPage();
        TimeoutException lastError = null;
        for (By locator : portfolioButtonLocators(buttonName)) {
            try {
                abs.tapBottomMost(locator, 8);
                return;
            } catch (TimeoutException e) {
                lastError = e;
            }
        }
        throw lastError != null
                ? lastError
                : new TimeoutException("Button was not visible on the portfolio page: " + buttonName);
    }

    public void waitUntilDisplayed() {
        if (!(driver instanceof AndroidDriver)) {
            throw new TimeoutException("Portfolio landing is implemented for Android only");
        }
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .ignoring(StaleElementReferenceException.class)
                .withMessage("Portfolio page was not visible after leaving Deposit")
                .until(d -> portfolioListChromeVisible() || openPositionsTabVisible());
    }

    private boolean openPositionsTabVisible() {
        Dimension window = driver.manage().window().getSize();
        int maxY = (int) (window.getHeight() * 0.70);
        for (WebElement el : driver.findElements(By.xpath(
                "//*[@text='Open Positions' or @content-desc='Open Positions']"
        ))) {
            try {
                Point location = el.getLocation();
                Dimension size = el.getSize();
                if (location.getY() < maxY && size.getHeight() > 0 && size.getHeight() <= 140) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return false;
    }

    private void waitForPortfolioPage() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> portfolioListChromeVisible());
        } catch (TimeoutException ignored) {
        }
    }

    private boolean portfolioListChromeVisible() {
        Dimension window = driver.manage().window().getSize();
        int maxY = (int) (window.getHeight() * 0.70);
        for (WebElement el : driver.findElements(By.xpath(
                "//*[@text='Show all' or @text='Show All' or @content-desc='Show all'"
                        + " or contains(@text,'Show last') or contains(@content-desc,'Show last')"
                        + " or @text='History' or @content-desc='History'"
                        + " or @text='Newest to Oldest' or @content-desc='Newest to Oldest']"
        ))) {
            try {
                Point location = el.getLocation();
                Dimension size = el.getSize();
                if (location.getY() < maxY && size.getHeight() <= 140) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return false;
    }

    private List<By> portfolioButtonLocators(String buttonName) {
        List<By> locators = List.of(
                By.xpath("//android.widget.TextView[@text='" + buttonName + "']"),
                By.xpath("//*[@text='" + buttonName + "']"),
                By.xpath("//*[contains(@text,'" + buttonName + "')]"),
                By.xpath("//*[contains(@content-desc,'" + buttonName + "')]")
        );
        if (!buttonName.equalsIgnoreCase("Show all")) {
            return locators;
        }
        return List.of(
                By.xpath("//android.widget.TextView[@text='Show all']"),
                By.xpath("//android.widget.TextView[@text='Show All']"),
                By.xpath("//*[@text='Show all' or @text='Show All']"),
                By.xpath("//*[contains(@text,'Show all') or contains(@text,'Show All')]"),
                By.xpath("//*[contains(@content-desc,'Show all') or contains(@content-desc,'Show All')]")
        );
    }

    public String getTitleAos() {
        if (!(driver instanceof AndroidDriver)) {
            return "";
        }
        WebElement heading = filteringHeading();
        String text = heading.getText();
        if (text == null || text.isBlank()) {
            text = elementAttribute(heading, "text");
        }
        return text == null ? "" : text.trim();
    }

    private WebElement filteringHeading() {
        List<By> locators = List.of(
                By.xpath("//android.widget.TextView[@text='Show']"),
                By.xpath("//*[@text='Show']"),
                By.xpath("//android.widget.TextView[@content-desc='Show']")
        );
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.ignoring(StaleElementReferenceException.class);
        TimeoutException lastError = null;
        for (By locator : locators) {
            try {
                return wait.until(d -> {
                    for (WebElement el : d.findElements(locator)) {
                        try {
                            String text = el.getText();
                            if (text == null || text.isBlank()) {
                                text = elementAttribute(el, "text");
                            }
                            if (text != null && text.trim().equals("Show") && el.isDisplayed()) {
                                return el;
                            }
                        } catch (StaleElementReferenceException ignored) {
                        }
                    }
                    return null;
                });
            } catch (TimeoutException e) {
                lastError = e;
            }
        }
        throw lastError != null
                ? lastError
                : new TimeoutException("Portfolio filtering heading was not visible");
    }

    public String getCheckedProduct() {
        if (!(driver instanceof AndroidDriver)) {
            return "No checked product found";
        }
        waitForFilteringPage();
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(10))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> selectedProductName());
        } catch (TimeoutException e) {
            throw new NoSuchElementException("Could not find the selected product on the portfolio filtering page");
        }
    }

    private String selectedProductName() {
        String fromState = productNameFromSelectedState();
        if (fromState != null) {
            return fromState;
        }
        String fromCheckmark = productNameBesideCheckmark();
        if (fromCheckmark != null) {
            return fromCheckmark;
        }
        if (!driver.findElements(By.xpath("//android.widget.TextView[@text='All']")).isEmpty()
                || !driver.findElements(By.xpath("//*[@content-desc='All']")).isEmpty()) {
            return "All";
        }
        return null;
    }

    private String productNameFromSelectedState() {
        for (WebElement el : driver.findElements(By.xpath(
                "//*[@selected='true' or @checked='true' or @content-desc='All']"))) {
            try {
                String name = visibleProductLabel(el);
                if (name != null) {
                    return name;
                }
                int[] box = parseBounds(elementAttribute(el, "bounds"));
                String nearby = productLabelOnRow(box);
                if (nearby != null) {
                    return nearby;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return null;
    }

    private String productNameBesideCheckmark() {
        for (WebElement el : driver.findElements(By.xpath(
                "//android.widget.ScrollView//android.widget.TextView"))) {
            try {
                String name = visibleProductLabel(el);
                if (name == null) {
                    continue;
                }
                int[] labelBounds = parseBounds(elementAttribute(el, "bounds"));
                if (rowHasCheckmark(labelBounds)) {
                    return name;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return null;
    }

    private String visibleProductLabel(WebElement el) {
        String text = el.getText();
        if (text == null || text.isBlank()) {
            text = elementAttribute(el, "text");
        }
        if (text == null || text.isBlank()) {
            text = elementAttribute(el, "content-desc");
        }
        if (text == null) {
            return null;
        }
        text = text.trim();
        if (text.equals("Show") || text.toLowerCase().contains("show all") || text.length() > 40) {
            return null;
        }
        return text;
    }

    private String productLabelOnRow(int[] box) {
        if (box == null) {
            return null;
        }
        int centerY = (box[1] + box[3]) / 2;
        for (WebElement el : driver.findElements(By.className("android.widget.TextView"))) {
            try {
                int[] bounds = parseBounds(elementAttribute(el, "bounds"));
                if (bounds == null) {
                    continue;
                }
                int labelY = (bounds[1] + bounds[3]) / 2;
                if (Math.abs(labelY - centerY) > 40) {
                    continue;
                }
                String name = visibleProductLabel(el);
                if (name != null) {
                    return name;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return null;
    }

    private boolean rowHasCheckmark(int[] labelBounds) {
        if (labelBounds == null) {
            return false;
        }
        int labelY = (labelBounds[1] + labelBounds[3]) / 2;
        int labelRight = labelBounds[2];
        List<By> locators = List.of(
                By.className("android.widget.ImageView"),
                By.xpath("//android.widget.ScrollView//android.view.ViewGroup[@clickable='true']")
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
                    if (width < 16 || width > 96 || height < 16 || height > 96) {
                        continue;
                    }
                    int centerY = (bounds[1] + bounds[3]) / 2;
                    int centerX = (bounds[0] + bounds[2]) / 2;
                    if (Math.abs(centerY - labelY) <= 40 && centerX > labelRight) {
                        return true;
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }
        }
        return false;
    }

    public void tapBack() {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        waitForFilteringPage();
        Point point = headerBackPoint();
        abs.tapAt(point.getX(), point.getY());
    }

    private void waitForFilteringPage() {
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(d -> !d.findElements(By.xpath("//android.widget.TextView[@text='Show']")).isEmpty());
    }

    private Point headerBackPoint() {
        for (By locator : List.of(
                By.xpath("//*[@content-desc='Back']"),
                By.xpath("//*[@content-desc='Navigate up']"),
                By.xpath("//*[@content-desc='back']")
        )) {
            Point labeled = firstVisibleCenter(locator);
            if (labeled != null) {
                return labeled;
            }
        }
        Point chevron = topLeftClickableChevron();
        if (chevron != null) {
            return chevron;
        }
        Dimension window = driver.manage().window().getSize();
        return new Point(Math.max(40, window.getWidth() / 14), Math.max(80, (int) (window.getHeight() * 0.08)));
    }

    private Point topLeftClickableChevron() {
        Point best = null;
        int bestScore = Integer.MAX_VALUE;
        for (WebElement el : driver.findElements(
                By.xpath("//android.view.ViewGroup[@clickable='true']"))) {
            try {
                int[] bounds = parseBounds(elementAttribute(el, "bounds"));
                if (bounds == null) {
                    continue;
                }
                int width = bounds[2] - bounds[0];
                int height = bounds[3] - bounds[1];
                if (bounds[0] > 160 || bounds[1] > 400 || bounds[2] > 280) {
                    continue;
                }
                if (width < 40 || width > 160 || height < 40 || height > 220) {
                    continue;
                }
                int score = bounds[0] * 10 + bounds[1];
                if (score < bestScore) {
                    bestScore = score;
                    best = new Point((bounds[0] + bounds[2]) / 2, (bounds[1] + bounds[3]) / 2);
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return best;
    }

    private Point firstVisibleCenter(By locator) {
        for (WebElement el : driver.findElements(locator)) {
            int[] bounds = parseBounds(elementAttribute(el, "bounds"));
            if (bounds == null || bounds[2] <= bounds[0] || bounds[3] <= bounds[1]) {
                continue;
            }
            return new Point((bounds[0] + bounds[2]) / 2, (bounds[1] + bounds[3]) / 2);
        }
        return null;
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

    public boolean tabIsSelected(String tabName) {
        if (!(driver instanceof AndroidDriver)) {
            return false;
        }
        Point tab = tabPoint(tabName);
        if (tab == null) {
            return false;
        }
        for (By locator : tabLocators(tabName)) {
            try {
                for (WebElement el : driver.findElements(locator)) {
                    Point location = el.getLocation();
                    Dimension size = el.getSize();
                    int centerX = location.getX() + Math.max(8, size.getWidth() / 2);
                    int centerY = location.getY() + Math.max(8, size.getHeight() / 2);
                    if (Math.abs(centerX - tab.getX()) > 40 || Math.abs(centerY - tab.getY()) > 40) {
                        continue;
                    }
                    String selected = el.getAttribute("selected");
                    if ("true".equalsIgnoreCase(selected)) {
                        return true;
                    }
                }
            } catch (StaleElementReferenceException ignored) {
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    public void selectTab(String tabName) {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        waitForPortfolioPage();
        Point point = null;
        try {
            point = waitForTabPoint(tabName);
        } catch (RuntimeException e) {
            System.out.println("Portfolio tab lookup failed for " + tabName + ": " + e.getMessage());
        }
        if (point == null) {
            point = estimatedTabPoint(tabName);
        }
        abs.tapAt(point.getX(), point.getY());
    }

    private Point waitForTabPoint(String tabName) {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(8))
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(NoSuchElementException.class)
                    .until(d -> tabPoint(tabName));
        } catch (TimeoutException e) {
            return null;
        }
    }

    private Point tabPoint(String tabName) {
        Dimension window = driver.manage().window().getSize();
        int maxHeight = Math.max(140, (int) (window.getHeight() * 0.16));
        int maxWidth = (int) (window.getWidth() * 0.70);
        int maxY = (int) (window.getHeight() * 0.65);
        Point best = null;
        int bestY = Integer.MAX_VALUE;
        for (By locator : tabLocators(tabName)) {
            List<WebElement> matches;
            try {
                matches = driver.findElements(locator);
            } catch (RuntimeException e) {
                continue;
            }
            for (WebElement el : matches) {
                try {
                    int[] box = visibleBox(el);
                    if (box == null) {
                        continue;
                    }
                    int width = box[2] - box[0];
                    int height = box[3] - box[1];
                    if (box[1] > maxY) {
                        continue;
                    }
                    if (height < 8 || height > maxHeight) {
                        continue;
                    }
                    if (width <= 0 || width > maxWidth) {
                        continue;
                    }
                    if (box[1] < bestY) {
                        bestY = box[1];
                        best = new Point(
                                box[0] + Math.max(8, width / 2),
                                box[1] + Math.max(8, height / 2)
                        );
                    }
                } catch (StaleElementReferenceException | NoSuchElementException ignored) {
                }
            }
            if (best != null) {
                return best;
            }
        }
        return null;
    }

    private List<By> tabLocators(String tabName) {
        return switch (tabName) {
            case "History" -> List.of(
                    By.xpath("//*[@text='History' or @content-desc='History']"),
                    AppiumBy.androidUIAutomator("new UiSelector().text(\"History\")")
            );
            case "Pending Orders" -> List.of(
                    By.xpath("//*[@text='Pending Orders' or @content-desc='Pending Orders'"
                            + " or @text='Pending\nOrders' or @content-desc='Pending\nOrders']"),
                    By.xpath("//android.widget.TextView[contains(@text,'Pending')]"),
                    By.xpath("//*[contains(@text,'Pending') or contains(@content-desc,'Pending')]"),
                    AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Pending\")")
            );
            default -> List.of(
                    By.xpath("//*[@text='Open Positions' or @content-desc='Open Positions'"
                            + " or @text='Open\nPositions' or @content-desc='Open\nPositions']"),
                    By.xpath("//android.widget.TextView[contains(@text,'Open')]"),
                    By.xpath("//*[contains(@text,'Open') or contains(@content-desc,'Open')]"),
                    AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Open\")")
            );
        };
    }

    private Point estimatedTabPoint(String tabName) {
        Dimension window = driver.manage().window().getSize();
        int y = Math.max(80, listAreaTopY() - 28);
        double ratio = switch (tabName) {
            case "History" -> 0.82;
            case "Pending Orders" -> 0.50;
            default -> 0.18;
        };
        return new Point((int) (window.getWidth() * ratio), y);
    }

    public void tapButtonOnRow(String buttonName) {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        waitForPortfolioPage();
        if ("arrow".equalsIgnoreCase(buttonName) || "detail".equalsIgnoreCase(buttonName)) {
            tapFirstRowDetailsCta(buttonName);
            return;
        }
        Point point;
        try {
            point = new WebDriverWait(driver, Duration.ofSeconds(8))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> firstRowCtaPoint(buttonName));
        } catch (TimeoutException e) {
            point = estimatedCtaPoint(buttonName);
            System.out.println("Portfolio row CTA fallback for " + buttonName
                    + " at " + point.getX() + "," + point.getY());
        }
        abs.tapAt(point.getX(), point.getY());
        if ("cancel".equalsIgnoreCase(buttonName) && !confirmationDialogueVisible(2)) {
            Point retry = estimatedCtaPoint(buttonName);
            abs.tapAt(retry.getX(), retry.getY());
        } else if ("close".equalsIgnoreCase(buttonName) && !closePositionPageVisible(5)) {
            Point retry = estimatedCtaPoint("close");
            abs.tapAt(retry.getX(), retry.getY());
        } else if ("edit".equalsIgnoreCase(buttonName) && !editPositionPageVisible(5)) {
            Point retry = estimatedCtaPoint("edit");
            abs.tapAt(retry.getX(), retry.getY());
        }
    }

    private void tapFirstRowDetailsCta(String buttonName) {
        waitForFirstPortfolioRow();
        List<Point> candidates = new ArrayList<>();
        addUniquePoint(candidates, firstRowCtaPoint(buttonName));
        addUniquePoint(candidates, firstRowArrowPoint());
        addUniquePoint(candidates, estimatedCtaPoint("detail"));
        for (Point point : candidates) {
            System.out.println("Tapping portfolio first-row " + buttonName + " CTA at "
                    + point.getX() + "," + point.getY());
            abs.tapAt(point.getX(), point.getY());
            if (detailsPageVisible(4)) {
                return;
            }
        }
    }

    private void addUniquePoint(List<Point> points, Point point) {
        if (point == null) {
            return;
        }
        for (Point existing : points) {
            if (Math.abs(existing.getX() - point.getX()) <= px(8)
                    && Math.abs(existing.getY() - point.getY()) <= px(8)) {
                return;
            }
        }
        points.add(point);
    }

    private void waitForFirstPortfolioRow() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(8))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> firstPositionRowBounds() != null);
        } catch (TimeoutException ignored) {
        }
    }

    private Point firstRowArrowPoint() {
        int[] row = firstRowBand();
        int rowTop = row[1];
        int rowBottom = row[1] + row[3];
        int rowRight = row[2] > row[0] ? row[2] : driver.manage().window().getSize().getWidth();
        int minCenterX = rowRight / 2;
        int[] best = null;
        int bestRight = Integer.MIN_VALUE;
        for (WebElement el : clickableRowCandidates()) {
            try {
                int[] box = visibleBox(el);
                if (box == null) {
                    continue;
                }
                int width = box[2] - box[0];
                int height = box[3] - box[1];
                int centerX = (box[0] + box[2]) / 2;
                int centerY = (box[1] + box[3]) / 2;
                if (centerY < rowTop - px(8) || centerY > rowBottom + px(8)) {
                    continue;
                }
                if (centerX < minCenterX) {
                    continue;
                }
                if (width < px(12) || height < px(12) || width > px(180) || height > px(96)) {
                    continue;
                }
                if (box[2] > bestRight) {
                    bestRight = box[2];
                    best = box;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        if (best == null) {
            return null;
        }
        int width = best[2] - best[0];
        // Compact detail icon: tap center. Wide History PnL chip: tap the right-edge chevron.
        int x = width > px(80)
                ? best[2] - Math.min(px(16), Math.max(px(8), width / 10))
                : (best[0] + best[2]) / 2;
        int y = (best[1] + best[3]) / 2;
        return new Point(x, y);
    }

    private Point firstRowCtaPoint(String buttonName) {
        List<int[]> icons = firstRowIconsByGeometry();
        int index = ctaIndex(buttonName, icons.size());
        if (index < 0 || index >= icons.size()) {
            return null;
        }
        int[] box = icons.get(index);
        return new Point((box[0] + box[2]) / 2, (box[1] + box[3]) / 2);
    }

    private int ctaIndex(String buttonName, int count) {
        String name = buttonName == null ? "" : buttonName.trim().toLowerCase();
        if (count <= 0) {
            return -1;
        }
        if (name.equals("arrow") || name.equals("detail")) {
            return count - 1;
        }
        if (name.equals("close") || name.equals("cancel")) {
            return count >= 3 ? count - 3 : (count >= 2 ? 0 : -1);
        }
        if (name.equals("edit")) {
            return count >= 3 ? count - 2 : -1;
        }
        return -1;
    }

    private List<int[]> firstRowIconsByGeometry() {
        int[] row = firstRowBand();
        int rowTop = row[1];
        int rowBottom = row[1] + row[3];
        int minLeft = row[2] / 2;
        List<int[]> all = new ArrayList<>();
        for (WebElement el : clickableRowCandidates()) {
            try {
                int[] box = visibleBox(el);
                if (!isPortfolioRowCta(box, rowTop, rowBottom, minLeft)) {
                    continue;
                }
                all.add(box);
            } catch (StaleElementReferenceException ignored) {
            }
        }
        if (all.isEmpty()) {
            return List.of();
        }
        all.sort(Comparator.comparingInt(box -> box[1]));
        int clusterTop = all.getFirst()[1];
        int clusterSlack = px(24);
        List<int[]> clustered = new ArrayList<>();
        for (int[] box : all) {
            if (Math.abs(box[1] - clusterTop) <= clusterSlack) {
                clustered.add(box);
            }
        }
        return dedupeByX(clustered);
    }

    private int[] firstRowBand() {
        Dimension window = driver.manage().window().getSize();
        int maxHeight = Math.min(px(120), Math.max(px(72), (int) (window.getHeight() * 0.12)));
        int[] row = firstPositionRowBounds();
        if (row == null) {
            int top = listAreaTopY();
            return new int[]{0, top, window.getWidth(), maxHeight};
        }
        return new int[]{row[0], row[1], row[2], Math.min(row[3], maxHeight)};
    }

    private int[] firstPositionRowBounds() {
        int listTop = listAreaTopY();
        int footerTop = footerTopY();
        Dimension window = driver.manage().window().getSize();
        int[] fromProduct = firstRowBoundsFromLeftLabel(listTop, footerTop, window);
        if (fromProduct != null) {
            return fromProduct;
        }
        int[] best = null;
        int bestY = Integer.MAX_VALUE;
        List<By> markers = new ArrayList<>();
        String symbol = AppMarketsPage.tradeSymbol;
        if (symbol != null && !symbol.isBlank()) {
            markers.add(By.xpath("//*[@text='" + symbol + "']"));
        }
        markers.add(By.xpath(
                "//*[@text='BUY' or @text='SELL' or @text='Buy' or @text='Sell'"
                        + " or contains(@text,'BUY') or contains(@text,'SELL')]"));
        for (By locator : markers) {
            for (WebElement el : driver.findElements(locator)) {
                try {
                    int[] box = visibleBox(el);
                    if (box == null) {
                        continue;
                    }
                    int width = box[2] - box[0];
                    int height = box[3] - box[1];
                    if (box[1] < listTop - 10 || box[1] > footerTop - 40) {
                        continue;
                    }
                    if (height > 90 || width > (int) (window.getWidth() * 0.45)) {
                        continue;
                    }
                    if (box[1] < bestY) {
                        bestY = box[1];
                        int top = Math.max(listTop, box[1] - px(16));
                        int bottom = Math.min(footerTop - px(8), box[3] + px(72));
                        best = new int[]{0, top, window.getWidth(), Math.max(px(72), bottom - top)};
                    }
                } catch (StaleElementReferenceException ignored) {
                }
            }
            if (best != null) {
                return best;
            }
        }
        return null;
    }

    private int[] firstRowBoundsFromLeftLabel(int listTop, int footerTop, Dimension window) {
        int maxLeft = (int) (window.getWidth() * 0.28);
        int[] best = null;
        int bestY = Integer.MAX_VALUE;
        for (WebElement el : driver.findElements(By.xpath(
                "//android.widget.ScrollView//android.widget.TextView"))) {
            try {
                int[] box = visibleBox(el);
                if (box == null) {
                    continue;
                }
                int width = box[2] - box[0];
                int height = box[3] - box[1];
                if (box[0] > maxLeft || box[1] < listTop - 10 || box[1] > footerTop - 80) {
                    continue;
                }
                if (height < 18 || height > 90 || width > (int) (window.getWidth() * 0.40)) {
                    continue;
                }
                if (box[1] < bestY) {
                    bestY = box[1];
                    int top = Math.max(listTop, box[1] - px(12));
                    int bottom = Math.min(footerTop - px(8), box[3] + px(80));
                    best = new int[]{0, top, window.getWidth(), Math.max(px(72), bottom - top)};
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return best;
    }

    private List<WebElement> clickableRowCandidates() {
        List<WebElement> found = new ArrayList<>();
        for (By locator : List.of(
                By.xpath("//android.widget.ScrollView//android.view.ViewGroup[@clickable='true']"),
                By.className("android.widget.ImageView"),
                By.xpath("//android.widget.ScrollView//android.view.ViewGroup"),
                By.xpath("//*[@clickable='true']")
        )) {
            found.addAll(driver.findElements(locator));
        }
        return found;
    }

    private boolean isPortfolioRowCta(int[] box, int listTop, int footerTop, int minLeft) {
        if (box == null) {
            return false;
        }
        int width = box[2] - box[0];
        int height = box[3] - box[1];
        int centerY = (box[1] + box[3]) / 2;
        if (width < px(12) || width > px(180) || height < px(12) || height > px(120)) {
            return false;
        }
        if (box[0] < minLeft) {
            return false;
        }
        return centerY >= listTop - px(8) && centerY <= footerTop + px(8);
    }

    private boolean closePositionPageVisible(int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(Math.max(1, seconds)))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> !d.findElements(By.xpath(
                            "//*[@text='Close Position' or @content-desc='Close Position']"
                    )).isEmpty());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean editPositionPageVisible(int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(Math.max(1, seconds)))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> !d.findElements(By.xpath(
                            "//*[@text='Edit Position' or @content-desc='Edit Position']"
                    )).isEmpty());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean detailsPageVisible(int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(Math.max(1, seconds)))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> !d.findElements(By.xpath(
                            "//*[@text='Position Details' or @content-desc='Position Details'"
                                    + " or @text='Position Detail' or @content-desc='Position Detail'"
                                    + " or contains(@text,'Pending Order Detail')"
                                    + " or contains(@content-desc,'Pending Order Detail')"
                                    + " or contains(@text,'Pending Order Details')"
                                    + " or contains(@content-desc,'Pending Order Details')"
                                    + " or @text='Pending Order\nDetails'"
                                    + " or @content-desc='Pending Order\nDetails']"
                    )).isEmpty());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private int[] visibleBox(WebElement element) {
        int[] fromBounds = parseBounds(elementAttribute(element, "bounds"));
        if (fromBounds != null && fromBounds[2] > fromBounds[0] && fromBounds[3] > fromBounds[1]) {
            return fromBounds;
        }
        try {
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
        } catch (RuntimeException e) {
            return null;
        }
    }

    private Point estimatedCtaPoint(String buttonName) {
        Dimension window = driver.manage().window().getSize();
        String name = buttonName == null ? "" : buttonName.trim().toLowerCase();
        float dpFromRight = switch (name) {
            case "close", "cancel" -> 108f;
            case "edit" -> 68f;
            default -> 28f;
        };
        int[] row = firstRowBand();
        int x = Math.max(row[2] / 2, window.getWidth() - px(dpFromRight));
        int y = row[1] + Math.max(px(20), row[3] / 2);
        return new Point(x, y);
    }

    private List<int[]> dedupeByX(List<int[]> boxes) {
        boxes.sort(Comparator.comparingInt(box -> box[0]));
        List<int[]> unique = new ArrayList<>();
        for (int[] box : boxes) {
            if (unique.isEmpty()) {
                unique.add(box);
                continue;
            }
            int[] last = unique.getLast();
            int lastCenter = (last[0] + last[2]) / 2;
            int center = (box[0] + box[2]) / 2;
            if (Math.abs(center - lastCenter) <= px(16)) {
                if ((box[2] - box[0]) * (box[3] - box[1]) > (last[2] - last[0]) * (last[3] - last[1])) {
                    unique.set(unique.size() - 1, box);
                }
            } else {
                unique.add(box);
            }
        }
        return unique;
    }

    private int listAreaTopY() {
        for (By locator : List.of(
                By.xpath("//*[@content-desc='Show all' or @text='Show all' or @text='Show All']"),
                By.xpath("//*[contains(@content-desc,'Show last') or contains(@text,'Show last')]"),
                By.xpath("//*[@content-desc='Newest to Oldest' or @text='Newest to Oldest']"),
                By.xpath("//*[@text='History' or @content-desc='History']")
        )) {
            for (WebElement el : driver.findElements(locator)) {
                int[] box = parseBounds(elementAttribute(el, "bounds"));
                if (box != null && box[3] > box[1] && box[3] - box[1] <= 140) {
                    return box[3] + 8;
                }
            }
        }
        return (int) (driver.manage().window().getSize().getHeight() * 0.38);
    }

    private int footerTopY() {
        Dimension window = driver.manage().window().getSize();
        int minFooterY = (int) (window.getHeight() * 0.75);
        for (WebElement el : driver.findElements(By.xpath(
                "//*[@content-desc='Home' or @text='Home']"))) {
            int[] box = parseBounds(elementAttribute(el, "bounds"));
            if (box != null && box[1] > minFooterY) {
                return box[1];
            }
        }
        return window.getHeight() - px(88);
    }

    private int px(float dp) {
        return Math.max(1, Math.round(dp * pxPerDp()));
    }

    private float pxPerDp() {
        if (driver instanceof AndroidDriver androidDriver) {
            try {
                int dpi = Math.toIntExact(androidDriver.getDisplayDensity());
                if (dpi > 0) {
                    return dpi / 160f;
                }
            } catch (RuntimeException ignored) {
            }
        }
        Dimension window = driver.manage().window().getSize();
        return Math.max(1f, window.getWidth() / 360f);
    }

    public boolean confirmationDialogueIsDisplayed() {
        if (!(driver instanceof AndroidDriver)) {
            return false;
        }
        return confirmationDialogueVisible(12);
    }

    private boolean confirmationDialogueVisible(int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(Math.max(1, seconds)))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> !confirmationDialogueNodes().isEmpty());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private List<WebElement> confirmationDialogueNodes() {
        return driver.findElements(By.xpath(
                "//*[@resource-id='RNE__Overlay']"
                        + " | //*[@text='Cancel Order']"
                        + " | //*[contains(@text,\"Don't Show Again\")]"
                        + " | //*[@text='Order Confirmation' or @text='Confirm Order']"
        ));
    }

    private void confirmCancelOrder() {
        By overlayCancel = By.xpath(
                "//*[@resource-id='RNE__Overlay']//*[@text='Cancel Order']"
        );
        By cancelLabel = By.xpath("//*[@text='Cancel Order']");
        try {
            abs.tapBottomMost(overlayCancel, 8);
        } catch (TimeoutException e) {
            abs.tapBottomMost(cancelLabel, 8);
        }
    }

    public String getDate(){
        if (driver instanceof AndroidDriver) {
            abs.waitUntilElementVisible(dateAos);
            return dateAos.getText();
        }
        return "";
    }
    public boolean isPendingOrderDateValid(){
        return abs.dateValidator(getDate());
    }

    public void cancelPendingOrder() {
        if (driver instanceof AndroidDriver) {
            tapButtonOnRow("cancel");
            confirmCancelOrder();
        }
    }

    public boolean isValueDisplayedCorrect(String label) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.ignoring(StaleElementReferenceException.class);

        try {
            return wait.until(d -> {
                String source = d.getPageSource();
                return source != null && source.contains(label);
            });
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getValidationValue(String label) {
        return switch (label) {
            case "Direction" -> AppTradeView.selectedDirection;
            case "Product" -> AppMarketsPage.tradeSymbol;
            case "Status" -> "Pending";
            case "Product Name" -> abs.getProductName(AppMarketsPage.tradeSymbol);
            case "Order Type" -> AppInstrumentDetailsPage.stopOrderType.split(" ")[1];
            default -> null;
        };
    }
}
