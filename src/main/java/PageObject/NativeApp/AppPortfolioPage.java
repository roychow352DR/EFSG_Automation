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
import utils.BaseTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppPortfolioPage {

    private static final DateTimeFormatter HISTORY_TIMESTAMP =
            DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss");

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    private String lastHistoryTransactionDetail;

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
                .withMessage("Portfolio page was not visible")
                .until(d -> portfolioListChromeVisible() || openPositionsTabVisible());
    }

    private boolean openPositionsTabVisible() {
        Dimension window = driver.manage().window().getSize();
        int maxY = (int) (window.getHeight() * 0.70);
        for (WebElement el : driver.findElements(By.xpath(
                "//*[@text='Open Positions' or @content-desc='Open Positions'"
                        + " or @text='Open\nPositions' or @content-desc='Open\nPositions']"
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
        } else if ("close".equalsIgnoreCase(buttonName)) {
            if (closeOrEditPositionVisible(5)) {
                if (editPositionLabelPresent() && !closePositionLabelPresent()) {
                    System.out.println("Portfolio close CTA opened Edit Position; skipping estimated close retry");
                }
            } else {
                Point retry = estimatedCtaPoint("close");
                abs.tapAt(retry.getX(), retry.getY());
            }
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

    private boolean closeOrEditPositionVisible(int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(Math.max(1, seconds)))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> closePositionLabelPresent() || editPositionLabelPresent());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private boolean closePositionLabelPresent() {
        try {
            return !driver.findElements(By.xpath(
                    "//*[@text='Close Position' or @content-desc='Close Position']"
            )).isEmpty();
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    private boolean editPositionLabelPresent() {
        try {
            return !driver.findElements(By.xpath(
                    "//*[@text='Edit Position' or @content-desc='Edit Position']"
            )).isEmpty();
        } catch (StaleElementReferenceException e) {
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

    // EBL history is one row per deal: Open keeps the traded side, Close uses the opposite side.
    // EIEHK history is one card per position: Open: and Close: share the original side and volume.
    public boolean historyPositionRecordVisible(String action) {
        if (!(driver instanceof AndroidDriver)) {
            return false;
        }
        String expectedAction = "Close".equalsIgnoreCase(action) ? "Close" : "Open";
        String symbol = AppMarketsPage.tradeSymbol;
        String direction = expectedHistoryDirection(expectedAction);
        String volume = expectedHistoryVolume();
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(25))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> historyRowMatches(expectedAction, symbol, direction, volume));
        } catch (TimeoutException e) {
            System.out.println("Portfolio history " + expectedAction
                    + " record was not visible for symbol=" + symbol
                    + ", direction=" + direction
                    + ", volume=" + volume);
            String actionLabel = isEiehkHistory() ? expectedAction + ":" : expectedAction;
            for (HistoryText text : visibleHistoryTexts()) {
                if (text.text.equals(actionLabel) || text.text.equals(expectedAction) || text.text.equals(symbol)
                        || text.text.equalsIgnoreCase(direction) || text.text.startsWith("20")) {
                    System.out.println("Visible history text '" + text.text
                            + "' at " + text.left + "," + text.top);
                }
            }
            return false;
        }
    }

    public String lastHistoryTransactionDetail() {
        return lastHistoryTransactionDetail;
    }

    // Product Code, Product Name, Direction, and Open Price each use their own expected value.
    public boolean historyTransactionItemDisplayed(String item) {
        lastHistoryTransactionDetail = null;
        if (!(driver instanceof AndroidDriver)) {
            return false;
        }
        if (item != null && "Product Name".equalsIgnoreCase(item.trim())) {
            String name = expectedHistoryProductName();
            if (name == null) {
                return false;
            }
            return waitForHistoryResult(() -> latestHistoryShowsProductName(name));
        }
        if (item != null && "Direction".equalsIgnoreCase(item.trim())) {
            return waitForHistoryResult(this::latestHistoryShowsDirection);
        }
        if (item != null && "Open Price".equalsIgnoreCase(item.trim())) {
            return waitForHistoryResult(() -> latestHistoryShowsPrice("Open"));
        }
        String expectedCode = expectedHistoryProductCode(item);
        if (expectedCode == null) {
            return false;
        }
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(25))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> latestHistoryShowsProductCode(expectedCode).shown());
        } catch (TimeoutException e) {
            HistoryCodeResult result = latestHistoryShowsProductCode(expectedCode);
            lastHistoryTransactionDetail = result.detail();
            if (result.detail() != null) {
                System.out.println(result.detail());
            }
            logProductNameShownInsteadOfCode(expectedCode);
            return false;
        }
    }

    public boolean historyTransactionItemEquals(String item, String expected) {
        if (!(driver instanceof AndroidDriver)) {
            return false;
        }
        if (item == null || !"Action".equalsIgnoreCase(item.trim())) {
            System.out.println("Unsupported portfolio history transaction item: " + item);
            return false;
        }
        String action = normalizeHistoryAction(expected);
        if (action == null) {
            System.out.println("Unsupported portfolio history action: " + expected);
            return false;
        }
        return waitForHistoryResult(() -> latestHistoryActionIs(action));
    }

    // Open and Close must both be today's local date. The full trimmed node text has to pass dateValidator
    // (strict uuuu-MM-dd HH:mm:ss). historyTimestamp substring(0, 19) is not a pass.
    // EBL: nearest date-like text under the action. EIEHK: date-like text to the right of Open: and Close:.
    public boolean historyTransactionTimeFormatValid() {
        lastHistoryTransactionDetail = null;
        if (!(driver instanceof AndroidDriver)) {
            lastHistoryTransactionDetail = "Transaction time format is implemented for Android only";
            return false;
        }
        return waitForHistoryResult(this::latestHistoryTransactionTimeFormat);
    }

    private boolean waitForHistoryResult(Supplier<HistoryCodeResult> check) {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(25))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> check.get().shown());
        } catch (TimeoutException e) {
            HistoryCodeResult result = check.get();
            lastHistoryTransactionDetail = result.detail();
            if (result.detail() != null) {
                System.out.println(result.detail());
            }
            return false;
        }
    }

    // History prices are read from text and content-desc via visibleHistoryLabels.
    // EBL: a plain decimal on the Price line to the right counts, and a plain decimal under Price still counts.
    // Signed P/L (plus, minus, parentheses, or percent) is ignored. The nearest plain decimal wins.
    // EIEHK: Open: stays about 8dp to 40dp below the symbol. Close: uses about 16dp to 140dp.
    // The close price is the plain decimal on the Close: line left of the timestamp, or inside "Close: 4144.50".
    // An open-only card may have no Close: line. EBL close uses the opposite side; EIEHK keeps the original side.
    private HistoryCodeResult latestHistoryShowsPrice(String action) {
        String symbol = trimmedHistorySymbol();
        String volume = expectedHistoryVolume();
        if (symbol == null || volume == null || volume.isBlank()) {
            return new HistoryCodeResult(false, "Portfolio history symbol or volume is missing");
        }
        String expected = AppInstrumentDetailsPage.executedPrice;
        List<HistoryText> texts = visibleHistoryTexts();
        if (isEiehkHistory()) {
            return latestEiehkHistoryPrice(texts, symbol, volume, action, expected);
        }
        return latestEblHistoryPrice(texts, symbol, volume, action, expected);
    }

    private HistoryCodeResult latestEblHistoryPrice(List<HistoryText> texts, String symbol, String volume,
                                                     String action, String expectedPrice) {
        List<HistoryFact> facts = eblSymbolFacts(texts, symbol, volume);
        HistoryFact target = latestDirectedFact(facts, action);
        String direction = expectedHistoryDirection(action);
        if (target == null) {
            return historyPriceMissing(action, symbol, volume, direction, expectedPrice);
        }
        if ("Close".equals(action)) {
            HistoryFact open = latestDirectedFact(facts, "Open");
            if (open == null) {
                return new HistoryCodeResult(false,
                        "Latest history Close record for " + symbol
                                + " has no Open row; expected price '" + expectedPrice + "'");
            }
            if (target.timestamp.isBefore(open.timestamp)) {
                return new HistoryCodeResult(false,
                        "Latest history Close " + target.timestamp + " is before Open " + open.timestamp
                                + "; expected price '" + expectedPrice + "'");
            }
        }
        String displayed = eblPriceOnRow(texts, visibleHistoryLabels(), target);
        return historyPriceResult(action, symbol, target.timestamp, expectedPrice, displayed);
    }

    private HistoryCodeResult latestEiehkHistoryPrice(List<HistoryText> texts, String symbol, String volume,
                                                       String action, String expectedPrice) {
        List<EiehkCard> cards = directedEiehkCards(eiehkSymbolCards(texts, symbol, volume), action);
        List<HistoryText> labels = visibleHistoryLabels();
        String direction = expectedHistoryDirection(action);
        if ("Close".equals(action)) {
            EiehkCard latest = latestEiehkCloseCard(cards);
            if (latest == null) {
                return historyPriceMissing(action, symbol, volume, direction, expectedPrice);
            }
            String displayed = eiehkLinePrice(texts, labels, latest, "Close:");
            return historyPriceResult(action, symbol, latest.closeTime, expectedPrice, displayed);
        }
        EiehkCard latest = latestEiehkCard(cards);
        if (latest == null) {
            return historyPriceMissing(action, symbol, volume, direction, expectedPrice);
        }
        String displayed = eiehkLinePrice(texts, labels, latest, "Open:");
        return historyPriceResult(action, symbol, latest.openTime, expectedPrice, displayed);
    }

    private HistoryFact latestDirectedFact(List<HistoryFact> facts, String action) {
        String direction = expectedHistoryDirection(action);
        if (direction == null || direction.isBlank()) {
            return null;
        }
        List<HistoryFact> matching = new ArrayList<>();
        for (HistoryFact fact : facts) {
            if (direction.equalsIgnoreCase(fact.direction)) {
                matching.add(fact);
            }
        }
        return latestFact(matching, action);
    }

    private List<EiehkCard> directedEiehkCards(List<EiehkCard> cards, String action) {
        String direction = expectedHistoryDirection(action);
        List<EiehkCard> matching = new ArrayList<>();
        if (direction == null || direction.isBlank()) {
            return matching;
        }
        for (EiehkCard card : cards) {
            if (direction.equalsIgnoreCase(card.direction)) {
                matching.add(card);
            }
        }
        return matching;
    }

    private EiehkCard latestEiehkCloseCard(List<EiehkCard> cards) {
        return cards.stream()
                .filter(card -> card.closeTime != null && !card.closeTime.isBefore(card.openTime))
                .max(Comparator.comparing(EiehkCard::closeTime)
                        .thenComparing(Comparator.comparingInt(EiehkCard::top).reversed()))
                .orElse(null);
    }

    private String eblPriceOnRow(List<HistoryText> texts, List<HistoryText> labels, HistoryFact row) {
        HistoryText action = historyTextAt(texts, row.action, row.top);
        if (action == null) {
            return null;
        }
        int actionCenter = (action.top + action.bottom) / 2;
        int bandTop = action.top - px(56);
        int bandBottom = action.top + px(72);
        HistoryText priceLabel = null;
        int bestGap = Integer.MAX_VALUE;
        for (HistoryText text : labels) {
            if (!"Price".equals(text.text) || text.left < action.right - px(8)) {
                continue;
            }
            int center = (text.top + text.bottom) / 2;
            if (center < bandTop || center > bandBottom) {
                continue;
            }
            int gap = Math.abs(center - actionCenter);
            if (gap < bestGap) {
                bestGap = gap;
                priceLabel = text;
            }
        }
        if (priceLabel == null) {
            return null;
        }
        return plainPriceUnderLabel(labels, priceLabel);
    }

    // Same-line prices sit to the right of Price (vertical gap can be 0). Prices below stay within 64dp.
    private String plainPriceUnderLabel(List<HistoryText> texts, HistoryText label) {
        int labelCenterY = (label.top + label.bottom) / 2;
        HistoryText best = null;
        int bestDistance = Integer.MAX_VALUE;
        for (HistoryText text : texts) {
            if (!isPlainHistoryPrice(text.text)) {
                continue;
            }
            int centerY = (text.top + text.bottom) / 2;
            int verticalGap = centerY - labelCenterY;
            boolean sameLineRight = Math.abs(verticalGap) <= px(12)
                    && text.left >= label.right - px(4);
            boolean under = verticalGap > 0 && verticalGap <= px(64)
                    && horizontallyAligned(label, text);
            if (!sameLineRight && !under) {
                continue;
            }
            int distance = sameLineRight
                    ? Math.abs(verticalGap) + Math.max(0, text.left - label.right)
                    : verticalGap;
            if (best == null || distance < bestDistance
                    || (distance == bestDistance && text.left < best.left)) {
                bestDistance = distance;
                best = text;
            }
        }
        return best == null ? null : best.text;
    }

    private boolean horizontallyAligned(HistoryText label, HistoryText value) {
        int slack = px(48);
        return value.left <= label.right + slack && value.right >= label.left - slack;
    }

    private String eiehkLinePrice(List<HistoryText> texts, List<HistoryText> labels, EiehkCard card, String label) {
        HistoryText symbol = historyTextAt(texts, card.identifier, card.top);
        if (symbol == null) {
            return null;
        }
        String embedded = embeddedCaptionPrice(labels, symbol, label);
        if (embedded != null) {
            return embedded;
        }
        HistoryText actionLabel = eiehkActionLabel(labels, symbol, label);
        if (actionLabel == null) {
            return null;
        }
        return plainPriceOnLabelLine(labels, actionLabel);
    }

    // Open: stays about 8dp to 40dp below the symbol. Close: is about 16dp to 140dp for a taller card.
    private int eiehkCaptionMinDelta(String label) {
        return "Close:".equals(label) ? px(16) : px(8);
    }

    private int eiehkCaptionMaxDelta(String label) {
        return "Close:".equals(label) ? px(140) : px(40);
    }

    private boolean historyActionCaption(String text, String caption) {
        if (text == null || caption == null) {
            return false;
        }
        String value = text.trim();
        return value.equals(caption) || value.startsWith(caption);
    }

    private String embeddedCaptionPrice(List<HistoryText> labels, HistoryText identifier, String label) {
        int identifierCenter = (identifier.top + identifier.bottom) / 2;
        int minDelta = eiehkCaptionMinDelta(label);
        int maxDelta = eiehkCaptionMaxDelta(label);
        for (HistoryText other : labels) {
            int delta = (other.top + other.bottom) / 2 - identifierCenter;
            if (other.left > identifier.right || delta < minDelta || delta > maxDelta) {
                continue;
            }
            String price = plainDecimalAfterCaption(other.text, label);
            if (price != null) {
                return price;
            }
        }
        return null;
    }

    private String plainDecimalAfterCaption(String text, String caption) {
        if (text == null || caption == null || !text.startsWith(caption) || text.length() == caption.length()) {
            return null;
        }
        String rest = text.substring(caption.length()).trim();
        if (rest.contains("+") || rest.contains("-") || rest.contains("(") || rest.contains(")")
                || rest.contains("%")) {
            return null;
        }
        Matcher matcher = Pattern.compile("\\d+(?:,\\d{3})*(?:\\.\\d+)?").matcher(rest);
        if (!matcher.find()) {
            return null;
        }
        String number = matcher.group().replace(",", "");
        return isPlainHistoryPrice(number) ? number : null;
    }

    // Same vertical window as eiehkActionTimestamp. Open does not require a Close: line.
    private HistoryText eiehkActionLabel(List<HistoryText> texts, HistoryText identifier, String label) {
        int identifierCenter = (identifier.top + identifier.bottom) / 2;
        int minDelta = eiehkCaptionMinDelta(label);
        int maxDelta = eiehkCaptionMaxDelta(label);
        HistoryText prefix = null;
        for (HistoryText other : texts) {
            int otherCenter = (other.top + other.bottom) / 2;
            int delta = otherCenter - identifierCenter;
            if (!historyActionCaption(other.text, label) || other.left > identifier.right
                    || delta < minDelta || delta > maxDelta) {
                continue;
            }
            if (label.equals(other.text)) {
                return other;
            }
            if (prefix == null) {
                prefix = other;
            }
        }
        return prefix;
    }

    private String plainPriceOnLabelLine(List<HistoryText> texts, HistoryText label) {
        int labelCenter = (label.top + label.bottom) / 2;
        Integer timestampLeft = null;
        for (HistoryText other : texts) {
            int otherCenter = (other.top + other.bottom) / 2;
            if (other.left > label.left
                    && Math.abs(otherCenter - labelCenter) <= px(12)
                    && historyTimestamp(other.text) != null) {
                timestampLeft = other.left;
                break;
            }
        }
        HistoryText best = null;
        for (HistoryText other : texts) {
            int otherCenter = (other.top + other.bottom) / 2;
            if (Math.abs(otherCenter - labelCenter) > px(12) || other.left <= label.right - px(4)) {
                continue;
            }
            if (timestampLeft != null && other.left >= timestampLeft) {
                continue;
            }
            if (!isPlainHistoryPrice(other.text)) {
                continue;
            }
            if (best == null || other.left < best.left) {
                best = other;
            }
        }
        return best == null ? null : best.text;
    }

    private HistoryText historyTextAt(List<HistoryText> texts, String text, int top) {
        for (HistoryText candidate : texts) {
            if (text.equals(candidate.text) && candidate.top == top) {
                return candidate;
            }
        }
        return null;
    }

    private boolean isPlainHistoryPrice(String text) {
        if (text == null) {
            return false;
        }
        String value = text.trim();
        if (value.contains("+") || value.contains("-") || value.contains("(") || value.contains(")")
                || value.contains("%")) {
            return false;
        }
        return value.replace(",", "").matches("\\d+(\\.\\d+)?");
    }

    private HistoryCodeResult historyPriceMissing(String action, String symbol, String volume,
                                                   String direction, String expectedPrice) {
        return new HistoryCodeResult(false,
                "Latest history " + action + " record for " + symbol
                        + " volume " + volume + " direction " + direction
                        + " was not visible; expected price '" + expectedPrice
                        + "', displayed price was not read");
    }

    private HistoryCodeResult historyPriceResult(String action, String symbol, LocalDateTime timestamp,
                                                  String expectedRaw, String displayedRaw) {
        BigDecimal expected = historyPriceNumber(expectedRaw);
        BigDecimal displayed = historyPriceNumber(displayedRaw);
        if (expected != null && displayed != null && expected.compareTo(displayed) == 0) {
            return new HistoryCodeResult(true, null);
        }
        return new HistoryCodeResult(false,
                "Latest history " + action + " record for " + symbol + " at " + timestamp
                        + " displayed price '" + displayedRaw + "' instead of expected price '"
                        + expectedRaw + "'");
    }

    private BigDecimal historyPriceNumber(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String text = value.replace(",", "").trim();
        if (text.startsWith("+")) {
            text = text.substring(1).trim();
        }
        if (!text.matches("\\d+(\\.\\d+)?")) {
            return null;
        }
        try {
            return new BigDecimal(text);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private String normalizeHistoryAction(String expected) {
        if (expected == null) {
            return null;
        }
        String text = expected.trim();
        if (text.endsWith(":")) {
            text = text.substring(0, text.length() - 1).trim();
        }
        if ("Open".equalsIgnoreCase(text)) {
            return "Open";
        }
        if ("Close".equalsIgnoreCase(text)) {
            return "Close";
        }
        return null;
    }

    private String expectedHistoryProductCode(String item) {
        if (item == null || !"Product Code".equalsIgnoreCase(item.trim())) {
            System.out.println("Unsupported portfolio history transaction item: " + item);
            return null;
        }
        String symbol = AppMarketsPage.tradeSymbol;
        if (symbol == null || symbol.isBlank()) {
            System.out.println("Portfolio history product code is missing");
            return null;
        }
        return symbol.trim();
    }

    private void logProductNameShownInsteadOfCode(String symbol) {
        String productName = abs.getProductName(symbol);
        if (productName == null || productName.isBlank()
                || "symbol not found".equalsIgnoreCase(productName)
                || productName.equalsIgnoreCase(symbol)) {
            return;
        }
        int listTop = listAreaTopY();
        boolean codeVisible = false;
        boolean nameVisible = false;
        for (HistoryText text : visibleHistoryTexts()) {
            if (text.top < listTop) {
                continue;
            }
            if (symbol.equals(text.text)) {
                codeVisible = true;
            }
            if (productName.equalsIgnoreCase(text.text)) {
                nameVisible = true;
            }
        }
        if (!codeVisible && nameVisible) {
            System.out.println("Portfolio history shows product name '" + productName
                    + "' instead of product code '" + symbol + "'");
        }
    }

    private HistoryCodeResult latestHistoryShowsProductCode(String symbol) {
        String volume = expectedHistoryVolume();
        String openDirection = expectedHistoryDirection("Open");
        String closeDirection = expectedHistoryDirection("Close");
        if (volume == null || volume.isBlank() || openDirection.isBlank() || closeDirection.isBlank()) {
            return new HistoryCodeResult(false, "Portfolio history direction or volume is missing");
        }
        List<HistoryText> texts = visibleHistoryTexts();
        if (isEiehkHistory()) {
            return latestEiehkCardShowsCode(texts, symbol, openDirection, volume);
        }
        return latestEblRowsShowCode(texts, symbol, openDirection, closeDirection, volume);
    }

    private HistoryCodeResult latestEblRowsShowCode(List<HistoryText> texts, String symbol,
                                                     String openDirection, String closeDirection, String volume) {
        int listTop = listAreaTopY();
        String today = LocalDate.now().toString();
        List<HistoryCandidate> opens = new ArrayList<>();
        List<HistoryCandidate> closes = new ArrayList<>();
        for (HistoryText actionText : texts) {
            if (actionText.top < listTop) {
                continue;
            }
            if ("Open".equals(actionText.text)) {
                addCandidate(opens, eblActionCandidate(texts, actionText, openDirection, volume, today));
            } else if ("Close".equals(actionText.text)) {
                addCandidate(closes, eblActionCandidate(texts, actionText, closeDirection, volume, today));
            }
        }
        HistoryCandidate open = latestCandidate(opens);
        HistoryCandidate close = latestCandidate(closes);
        if (open == null || close == null) {
            return new HistoryCodeResult(false,
                    "Latest portfolio history Open/Close rows were not visible for volume " + volume);
        }
        if (close.timestamp.isBefore(open.timestamp)) {
            return new HistoryCodeResult(false,
                    "Latest history Close " + close.timestamp + " is before Open " + open.timestamp);
        }
        if (symbol.equals(open.identifier) && symbol.equals(close.identifier)) {
            return new HistoryCodeResult(true, null);
        }
        return new HistoryCodeResult(false,
                "Latest history Open shows '" + open.identifier
                        + "' and Close shows '" + close.identifier
                        + "' instead of product code '" + symbol + "'");
    }

    private HistoryCodeResult latestEiehkCardShowsCode(List<HistoryText> texts, String symbol,
                                                        String openDirection, String volume) {
        int listTop = listAreaTopY();
        String today = LocalDate.now().toString();
        List<HistoryCandidate> cards = new ArrayList<>();
        for (HistoryText directionText : texts) {
            if (directionText.top < listTop || !openDirection.equalsIgnoreCase(directionText.text)) {
                continue;
            }
            HistoryText identifier = eiehkIdentifierLeftOf(texts, directionText);
            if (identifier == null || !eiehkVolumeOnIdentifierLine(texts, identifier, volume)) {
                continue;
            }
            LocalDateTime openTime = eiehkActionTimestamp(texts, identifier, "Open:", today);
            LocalDateTime closeTime = eiehkActionTimestamp(texts, identifier, "Close:", today);
            if (openTime == null || closeTime == null || closeTime.isBefore(openTime)) {
                continue;
            }
            cards.add(new HistoryCandidate(identifier.text, closeTime, identifier.top));
        }
        HistoryCandidate latest = latestCandidate(cards);
        if (latest == null) {
            return new HistoryCodeResult(false,
                    "Latest EIEHK history card with Open and Close was not visible for volume " + volume);
        }
        if (symbol.equals(latest.identifier)) {
            return new HistoryCodeResult(true, null);
        }
        return new HistoryCodeResult(false,
                "Latest history card shows '" + latest.identifier
                        + "' instead of product code '" + symbol + "'");
    }

    private void addCandidate(List<HistoryCandidate> candidates, HistoryCandidate candidate) {
        if (candidate != null) {
            candidates.add(candidate);
        }
    }

    private HistoryCandidate latestCandidate(List<HistoryCandidate> candidates) {
        return candidates.stream()
                .max(Comparator.comparing(HistoryCandidate::timestamp)
                        .thenComparing(Comparator.comparingInt(HistoryCandidate::top).reversed()))
                .orElse(null);
    }

    private HistoryCandidate eblActionCandidate(List<HistoryText> texts, HistoryText actionText,
                                                 String direction, String volume, String today) {
        int centerY = (actionText.top + actionText.bottom) / 2;
        boolean directionFound = false;
        boolean volumeFound = false;
        LocalDateTime timestamp = null;
        int dateGap = Integer.MAX_VALUE;
        HistoryText identifier = null;
        int identifierGap = Integer.MAX_VALUE;
        for (HistoryText other : texts) {
            int otherCenter = (other.top + other.bottom) / 2;
            int delta = otherCenter - centerY;
            if (direction.equalsIgnoreCase(other.text)
                    && other.left >= actionText.right - px(8)
                    && Math.abs(delta) <= px(16)) {
                directionFound = true;
            }
            if (volume.equals(normalizeHistoryVolume(other.text))
                    && other.left >= actionText.right - px(8)
                    && Math.abs(delta) <= px(16)) {
                volumeFound = true;
            }
            if (delta > 0 && delta <= px(56) && delta < dateGap) {
                LocalDateTime parsed = historyTimestamp(other.text);
                if (parsed != null && today.equals(parsed.toLocalDate().toString())) {
                    dateGap = delta;
                    timestamp = parsed;
                }
            }
            if (other.left < actionText.left && delta < 0 && -delta <= px(56) && -delta < identifierGap) {
                identifierGap = -delta;
                identifier = other;
            }
        }
        if (!directionFound || !volumeFound || timestamp == null
                || identifier == null || identifier.text.isBlank()) {
            return null;
        }
        return new HistoryCandidate(identifier.text, timestamp, actionText.top);
    }

    private HistoryText eiehkIdentifierLeftOf(List<HistoryText> texts, HistoryText directionText) {
        int directionCenter = (directionText.top + directionText.bottom) / 2;
        HistoryText best = null;
        int bestRight = Integer.MIN_VALUE;
        for (HistoryText other : texts) {
            int otherCenter = (other.top + other.bottom) / 2;
            if (other.left < directionText.left
                    && directionText.left >= other.right - px(8)
                    && Math.abs(otherCenter - directionCenter) <= px(16)
                    && other.right > bestRight) {
                bestRight = other.right;
                best = other;
            }
        }
        return best;
    }

    private boolean eiehkVolumeOnIdentifierLine(List<HistoryText> texts, HistoryText identifier, String volume) {
        int identifierCenter = (identifier.top + identifier.bottom) / 2;
        for (HistoryText other : texts) {
            int otherCenter = (other.top + other.bottom) / 2;
            if (volume.equals(normalizeHistoryVolume(other.text))
                    && other.left > identifier.right
                    && Math.abs(otherCenter - identifierCenter) <= px(16)) {
                return true;
            }
        }
        return false;
    }

    private LocalDateTime eiehkActionTimestamp(List<HistoryText> texts, HistoryText identifier,
                                                String label, String today) {
        LocalDateTime timestamp = eiehkCaptionTimestamp(texts, identifier, label, today);
        if (timestamp != null) {
            return timestamp;
        }
        return eiehkCaptionTimestamp(visibleHistoryLabels(), identifier, label, today);
    }

    private LocalDateTime eiehkCaptionTimestamp(List<HistoryText> texts, HistoryText identifier,
                                                 String label, String today) {
        int identifierCenter = (identifier.top + identifier.bottom) / 2;
        int minDelta = eiehkCaptionMinDelta(label);
        int maxDelta = eiehkCaptionMaxDelta(label);
        for (HistoryText other : texts) {
            int otherCenter = (other.top + other.bottom) / 2;
            int delta = otherCenter - identifierCenter;
            if (!historyActionCaption(other.text, label) || other.left > identifier.right
                    || delta < minDelta || delta > maxDelta) {
                continue;
            }
            LocalDateTime parsed = historyTimestamp(historyDateTextOnSameLine(texts, other, today));
            if (parsed != null && today.equals(parsed.toLocalDate().toString())) {
                return parsed;
            }
        }
        return null;
    }

    private String historyDateTextOnSameLine(List<HistoryText> texts, HistoryText label, String today) {
        int labelCenter = (label.top + label.bottom) / 2;
        for (HistoryText other : texts) {
            int otherCenter = (other.top + other.bottom) / 2;
            if (other.left > label.left
                    && Math.abs(otherCenter - labelCenter) <= px(12)
                    && other.text.startsWith(today)) {
                return other.text;
            }
        }
        return null;
    }

    private LocalDateTime historyTimestamp(String text) {
        if (text == null || text.length() < 19) {
            return null;
        }
        try {
            return LocalDateTime.parse(text.substring(0, 19), HISTORY_TIMESTAMP);
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }

    private String expectedHistoryProductName() {
        String symbol = AppMarketsPage.tradeSymbol;
        if (symbol == null || symbol.isBlank()) {
            System.out.println("Portfolio history product code is missing");
            return null;
        }
        String name = abs.getProductName(symbol.trim());
        if (name == null || name.isBlank()
                || "symbol not found".equalsIgnoreCase(name)
                || name.equalsIgnoreCase(symbol.trim())) {
            System.out.println("Portfolio history product name is missing for " + symbol);
            return null;
        }
        return name;
    }

    private HistoryCodeResult latestHistoryShowsProductName(String name) {
        String volume = expectedHistoryVolume();
        if (volume == null || volume.isBlank()) {
            return new HistoryCodeResult(false, "Portfolio history volume is missing");
        }
        String symbol = trimmedHistorySymbol();
        if (symbol == null) {
            return new HistoryCodeResult(false, "Portfolio history product code is missing");
        }
        List<HistoryText> texts = visibleHistoryTexts();
        if (isEiehkHistory()) {
            return latestEiehkCardShowsName(texts, symbol, name, volume);
        }
        return latestEblRowsShowName(texts, symbol, name, volume);
    }

    private HistoryCodeResult latestEblRowsShowName(List<HistoryText> texts, String symbol,
                                                     String name, String volume) {
        List<HistoryFact> facts = eblSymbolFacts(texts, symbol, volume);
        HistoryFact open = latestFact(facts, "Open");
        HistoryFact close = latestFact(facts, "Close");
        if (open == null || close == null) {
            return new HistoryCodeResult(false,
                    "Latest portfolio history Open/Close rows were not visible for product name " + name);
        }
        if (close.timestamp.isBefore(open.timestamp)) {
            return new HistoryCodeResult(false,
                    "Latest history Close " + close.timestamp + " is before Open " + open.timestamp);
        }
        List<HistoryText> labels = visibleHistoryLabels();
        boolean openShowsName = rowShowsProductName(labels, open, name);
        boolean closeShowsName = rowShowsProductName(labels, close, name);
        if (openShowsName && closeShowsName) {
            return new HistoryCodeResult(true, null);
        }
        return new HistoryCodeResult(false,
                "Latest history rows did not show product name '" + name
                        + "' from getProductName. Open identifier '" + open.identifier
                        + "', Close identifier '" + close.identifier + "'");
    }

    private HistoryCodeResult latestEiehkCardShowsName(List<HistoryText> texts, String symbol,
                                                        String name, String volume) {
        EiehkCard latest = latestEiehkCard(eiehkSymbolCards(texts, symbol, volume));
        if (latest == null) {
            return new HistoryCodeResult(false,
                    "Latest EIEHK history card was not visible for product name " + name);
        }
        if (latest.closeTime == null) {
            return new HistoryCodeResult(false,
                    "Latest EIEHK history card has no Close for product name " + name);
        }
        if (latest.closeTime.isBefore(latest.openTime)) {
            return new HistoryCodeResult(false,
                    "Latest history Close " + latest.closeTime + " is before Open " + latest.openTime);
        }
        if (cardShowsProductName(visibleHistoryLabels(), latest, name)) {
            return new HistoryCodeResult(true, null);
        }
        return new HistoryCodeResult(false,
                "Latest history card did not show product name '" + name
                        + "' from getProductName. Identifier '" + latest.identifier + "'");
    }

    private HistoryCodeResult latestHistoryShowsDirection() {
        String symbol = trimmedHistorySymbol();
        String volume = expectedHistoryVolume();
        String openDirection = expectedHistoryDirection("Open");
        String closeDirection = expectedHistoryDirection("Close");
        if (symbol == null || volume == null || volume.isBlank()
                || openDirection.isBlank() || closeDirection.isBlank()) {
            return new HistoryCodeResult(false, "Portfolio history symbol, direction, or volume is missing");
        }
        List<HistoryText> texts = visibleHistoryTexts();
        if (isEiehkHistory()) {
            return latestEiehkDirection(texts, symbol, openDirection, closeDirection, volume);
        }
        return latestEblDirection(texts, symbol, openDirection, closeDirection, volume);
    }

    private HistoryCodeResult latestHistoryActionIs(String expectedAction) {
        String symbol = trimmedHistorySymbol();
        String volume = expectedHistoryVolume();
        if (symbol == null || volume == null || volume.isBlank()) {
            return new HistoryCodeResult(false, "Portfolio history symbol or volume is missing");
        }
        List<HistoryText> texts = visibleHistoryTexts();
        if (isEiehkHistory()) {
            return latestEiehkAction(texts, symbol, volume, expectedAction);
        }
        return latestEblAction(texts, symbol, volume, expectedAction);
    }

    private String trimmedHistorySymbol() {
        String symbol = AppMarketsPage.tradeSymbol;
        if (symbol == null || symbol.isBlank()) {
            return null;
        }
        return symbol.trim();
    }

    private HistoryCodeResult latestEblDirection(List<HistoryText> texts, String symbol,
                                                  String openDirection, String closeDirection, String volume) {
        List<HistoryFact> facts = eblSymbolFacts(texts, symbol, volume);
        HistoryFact open = latestFact(facts, "Open");
        HistoryFact close = latestFact(facts, "Close");
        if (open == null) {
            return new HistoryCodeResult(false,
                    "Latest portfolio history Open row was not visible for " + symbol);
        }
        boolean closed = close != null && !close.timestamp.isBefore(open.timestamp);
        HistoryFact target = closed ? close : open;
        String expected = closed ? closeDirection : openDirection;
        if (target.direction == null) {
            return new HistoryCodeResult(false,
                    "Latest history " + target.action + " direction was not readable for " + symbol);
        }
        if (expected.equalsIgnoreCase(target.direction)) {
            return new HistoryCodeResult(true, null);
        }
        return new HistoryCodeResult(false,
                "Latest history " + target.action + " direction is '" + target.direction
                        + "' instead of '" + expected + "'");
    }

    private HistoryCodeResult latestEblAction(List<HistoryText> texts, String symbol,
                                               String volume, String expectedAction) {
        List<HistoryFact> facts = eblSymbolFacts(texts, symbol, volume);
        HistoryFact latest = latestFact(facts, null);
        if (latest == null) {
            return new HistoryCodeResult(false,
                    "Latest portfolio history row was not visible for " + symbol);
        }
        if ("Close".equals(expectedAction)) {
            HistoryFact open = latestFact(facts, "Open");
            if (!"Close".equals(latest.action)) {
                return new HistoryCodeResult(false,
                        "Latest history action is '" + latest.action + "' instead of Close");
            }
            if (open == null) {
                return new HistoryCodeResult(false,
                        "Latest history Close has no Open row for " + symbol);
            }
            if (latest.timestamp.isBefore(open.timestamp)) {
                return new HistoryCodeResult(false,
                        "Latest history Close " + latest.timestamp + " is before Open " + open.timestamp);
            }
            return new HistoryCodeResult(true, null);
        }
        if ("Open".equals(latest.action)) {
            return new HistoryCodeResult(true, null);
        }
        return new HistoryCodeResult(false,
                "Latest history action is '" + latest.action + "' instead of Open");
    }

    private List<HistoryFact> eblSymbolFacts(List<HistoryText> texts, String symbol, String volume) {
        int listTop = listAreaTopY();
        String today = LocalDate.now().toString();
        List<HistoryFact> facts = new ArrayList<>();
        for (HistoryText actionText : texts) {
            if (actionText.top < listTop
                    || (!"Open".equals(actionText.text) && !"Close".equals(actionText.text))) {
                continue;
            }
            HistoryFact fact = eblSymbolFact(texts, actionText, symbol, volume, today);
            if (fact != null) {
                facts.add(fact);
            }
        }
        return facts;
    }

    private HistoryFact eblSymbolFact(List<HistoryText> texts, HistoryText actionText,
                                       String symbol, String volume, String today) {
        int centerY = (actionText.top + actionText.bottom) / 2;
        boolean volumeFound = false;
        LocalDateTime timestamp = null;
        int dateGap = Integer.MAX_VALUE;
        HistoryText identifier = null;
        int identifierGap = Integer.MAX_VALUE;
        String direction = null;
        for (HistoryText other : texts) {
            int otherCenter = (other.top + other.bottom) / 2;
            int delta = otherCenter - centerY;
            if (("BUY".equalsIgnoreCase(other.text) || "SELL".equalsIgnoreCase(other.text))
                    && other.left >= actionText.right - px(8)
                    && Math.abs(delta) <= px(16)) {
                direction = other.text.trim().toUpperCase();
            }
            if (volume.equals(normalizeHistoryVolume(other.text))
                    && other.left >= actionText.right - px(8)
                    && Math.abs(delta) <= px(16)) {
                volumeFound = true;
            }
            if (delta > 0 && delta <= px(56) && delta < dateGap) {
                LocalDateTime parsed = historyTimestamp(other.text);
                if (parsed != null && today.equals(parsed.toLocalDate().toString())) {
                    dateGap = delta;
                    timestamp = parsed;
                }
            }
            if (other.left < actionText.left && delta < 0 && -delta <= px(56) && -delta < identifierGap) {
                identifierGap = -delta;
                identifier = other;
            }
        }
        if (!volumeFound || timestamp == null || identifier == null || !symbol.equals(identifier.text)) {
            return null;
        }
        return new HistoryFact(actionText.text, identifier.text, direction, timestamp, actionText.top);
    }

    private HistoryFact latestFact(List<HistoryFact> facts, String action) {
        return facts.stream()
                .filter(fact -> action == null || action.equals(fact.action))
                .max(Comparator.comparing(HistoryFact::timestamp)
                        .thenComparing(Comparator.comparingInt(HistoryFact::top).reversed()))
                .orElse(null);
    }

    private HistoryCodeResult latestEiehkDirection(List<HistoryText> texts, String symbol,
                                                    String openDirection, String closeDirection, String volume) {
        EiehkCard latest = latestEiehkCard(eiehkSymbolCards(texts, symbol, volume));
        if (latest == null) {
            return new HistoryCodeResult(false,
                    "Latest EIEHK history card was not visible for " + symbol);
        }
        boolean closed = latest.closeTime != null && !latest.closeTime.isBefore(latest.openTime);
        String expected = closed ? closeDirection : openDirection;
        if (latest.direction == null) {
            return new HistoryCodeResult(false,
                    "Latest history card direction was not readable for " + symbol);
        }
        if (expected.equalsIgnoreCase(latest.direction)) {
            return new HistoryCodeResult(true, null);
        }
        return new HistoryCodeResult(false,
                "Latest history card direction is '" + latest.direction + "' instead of '" + expected + "'");
    }

    private HistoryCodeResult latestEiehkAction(List<HistoryText> texts, String symbol,
                                                 String volume, String expectedAction) {
        EiehkCard latest = latestEiehkCard(eiehkSymbolCards(texts, symbol, volume));
        if (latest == null) {
            return new HistoryCodeResult(false,
                    "Latest EIEHK history card was not visible for " + symbol);
        }
        boolean closed = latest.closeTime != null && !latest.closeTime.isBefore(latest.openTime);
        String actual = closed ? "Close" : "Open";
        if (expectedAction.equals(actual)) {
            return new HistoryCodeResult(true, null);
        }
        return new HistoryCodeResult(false,
                "Latest history action is '" + actual + "' instead of " + expectedAction);
    }

    private EiehkCard latestEiehkCard(List<EiehkCard> cards) {
        return cards.stream()
                .max(Comparator.comparing(EiehkCard::openTime)
                        .thenComparing(Comparator.comparingInt(EiehkCard::top).reversed()))
                .orElse(null);
    }

    private List<EiehkCard> eiehkSymbolCards(List<HistoryText> texts, String symbol, String volume) {
        int listTop = listAreaTopY();
        String today = LocalDate.now().toString();
        List<HistoryText> labels = visibleHistoryLabels();
        List<EiehkCard> cards = new ArrayList<>();
        for (HistoryText symbolText : texts) {
            if (symbolText.top < listTop || !symbol.equals(symbolText.text)) {
                continue;
            }
            LocalDateTime openTime = eiehkActionTimestamp(texts, symbolText, "Open:", today);
            if (openTime == null) {
                continue;
            }
            if (!eiehkVolumeOnIdentifierLine(texts, symbolText, volume)
                    && !eiehkValueInCard(labels, symbolText, volume)) {
                continue;
            }
            String direction = eiehkDirectionOnIdentifierLine(texts, symbolText);
            if (direction == null) {
                direction = eiehkDirectionInCard(labels, symbolText);
            }
            LocalDateTime closeTime = eiehkActionTimestamp(texts, symbolText, "Close:", today);
            cards.add(new EiehkCard(symbolText.text, direction, openTime, closeTime, symbolText.top));
        }
        return cards;
    }

    private String eiehkDirectionInCard(List<HistoryText> labels, HistoryText symbolText) {
        int bandTop = symbolText.top - px(8);
        int bandBottom = symbolText.bottom + px(56);
        for (HistoryText label : labels) {
            int center = (label.top + label.bottom) / 2;
            if (center < bandTop || center > bandBottom || label.left < symbolText.left) {
                continue;
            }
            String side = historySide(label.text);
            if (side != null) {
                return side;
            }
        }
        return null;
    }

    private boolean eiehkValueInCard(List<HistoryText> labels, HistoryText symbolText, String volume) {
        int bandTop = symbolText.top - px(8);
        int bandBottom = symbolText.bottom + px(56);
        for (HistoryText label : labels) {
            int center = (label.top + label.bottom) / 2;
            if (center < bandTop || center > bandBottom || label.left <= symbolText.left) {
                continue;
            }
            if (volume.equals(normalizeHistoryVolume(label.text))) {
                return true;
            }
        }
        return false;
    }

    private String historySide(String text) {
        if (text == null) {
            return null;
        }
        String value = text.trim();
        if ("BUY".equalsIgnoreCase(value)) {
            return "BUY";
        }
        if ("SELL".equalsIgnoreCase(value)) {
            return "SELL";
        }
        return null;
    }

    private String eiehkDirectionOnIdentifierLine(List<HistoryText> texts, HistoryText identifier) {
        int identifierCenter = (identifier.top + identifier.bottom) / 2;
        for (HistoryText other : texts) {
            int otherCenter = (other.top + other.bottom) / 2;
            if (("BUY".equalsIgnoreCase(other.text) || "SELL".equalsIgnoreCase(other.text))
                    && other.left >= identifier.right - px(8)
                    && Math.abs(otherCenter - identifierCenter) <= px(16)) {
                return other.text.trim().toUpperCase();
            }
        }
        return null;
    }

    private String expectedHistoryDirection(String action) {
        String openDirection = AppTradeView.selectedDirection == null
                ? ""
                : AppTradeView.selectedDirection.trim().toUpperCase();
        if ("Close".equals(action) && !isEiehkHistory()) {
            return switch (openDirection) {
                case "BUY" -> "SELL";
                case "SELL" -> "BUY";
                default -> openDirection;
            };
        }
        return openDirection;
    }

    private boolean isEiehkHistory() {
        return "EIEHK".equalsIgnoreCase(BaseTest.productEntity);
    }

    private String expectedHistoryVolume() {
        return normalizeHistoryVolume(AppInstrumentDetailsPage.lotSize);
    }

    private boolean historyRowMatches(String action, String symbol, String direction, String volume) {
        if (symbol == null || symbol.isBlank() || direction == null || direction.isBlank()
                || volume == null || volume.isBlank()) {
            return false;
        }
        if (isEiehkHistory()) {
            return eiehkHistoryRowMatches(action, symbol, direction, volume);
        }
        return eblHistoryRowMatches(action, symbol, direction, volume);
    }

    private boolean eblHistoryRowMatches(String action, String symbol, String direction, String volume) {
        int listTop = listAreaTopY();
        String today = LocalDate.now().toString();
        List<HistoryText> texts = visibleHistoryTexts();
        for (HistoryText actionText : texts) {
            if (!action.equals(actionText.text) || actionText.top < listTop) {
                continue;
            }
            int centerY = (actionText.top + actionText.bottom) / 2;
            boolean symbolFound = false;
            boolean directionFound = false;
            boolean volumeFound = false;
            boolean datedToday = false;
            for (HistoryText other : texts) {
                int otherCenter = (other.top + other.bottom) / 2;
                int delta = otherCenter - centerY;
                if (symbol.equals(other.text)
                        && other.left < actionText.left
                        && delta < 0
                        && -delta <= px(56)) {
                    symbolFound = true;
                }
                if (direction.equalsIgnoreCase(other.text)
                        && other.left >= actionText.right - px(8)
                        && Math.abs(delta) <= px(16)) {
                    directionFound = true;
                }
                if (volume.equals(normalizeHistoryVolume(other.text))
                        && other.left >= actionText.right - px(8)
                        && Math.abs(delta) <= px(16)) {
                    volumeFound = true;
                }
                if (other.text.startsWith(today)
                        && delta > 0
                        && delta <= px(56)) {
                    datedToday = true;
                }
            }
            if (symbolFound && directionFound && volumeFound && datedToday) {
                return true;
            }
        }
        return false;
    }

    private boolean eiehkHistoryRowMatches(String action, String symbol, String direction, String volume) {
        String label = "Close".equals(action) ? "Close:" : "Open:";
        int listTop = listAreaTopY();
        String today = LocalDate.now().toString();
        int minDelta = eiehkCaptionMinDelta(label);
        int maxDelta = eiehkCaptionMaxDelta(label);
        List<HistoryText> texts = visibleHistoryTexts();
        List<HistoryText> labels = visibleHistoryLabels();
        for (HistoryText symbolText : texts) {
            if (!symbol.equals(symbolText.text) || symbolText.top < listTop) {
                continue;
            }
            int symbolCenter = (symbolText.top + symbolText.bottom) / 2;
            boolean directionFound = false;
            boolean volumeFound = false;
            boolean actionDatedToday = false;
            for (HistoryText other : texts) {
                int otherCenter = (other.top + other.bottom) / 2;
                int delta = otherCenter - symbolCenter;
                if (direction.equalsIgnoreCase(other.text)
                        && other.left >= symbolText.right - px(8)
                        && Math.abs(delta) <= px(16)) {
                    directionFound = true;
                }
                if (volume.equals(normalizeHistoryVolume(other.text))
                        && other.left > symbolText.right
                        && Math.abs(delta) <= px(16)) {
                    volumeFound = true;
                }
                if (historyActionCaption(other.text, label)
                        && other.left <= symbolText.right
                        && delta >= minDelta
                        && delta <= maxDelta
                        && historyDateOnSameLine(texts, other, today)) {
                    actionDatedToday = true;
                }
            }
            if (!actionDatedToday) {
                actionDatedToday = eiehkCaptionDatedToday(labels, symbolText, label, today);
            }
            if (directionFound && volumeFound && actionDatedToday) {
                return true;
            }
        }
        return false;
    }

    private boolean eiehkCaptionDatedToday(List<HistoryText> nodes, HistoryText symbolText, String label,
                                            String today) {
        int symbolCenter = (symbolText.top + symbolText.bottom) / 2;
        int minDelta = eiehkCaptionMinDelta(label);
        int maxDelta = eiehkCaptionMaxDelta(label);
        for (HistoryText other : nodes) {
            int delta = (other.top + other.bottom) / 2 - symbolCenter;
            if (historyActionCaption(other.text, label)
                    && other.left <= symbolText.right
                    && delta >= minDelta
                    && delta <= maxDelta
                    && historyDateOnSameLine(nodes, other, today)) {
                return true;
            }
        }
        return false;
    }

    private boolean historyDateOnSameLine(List<HistoryText> texts, HistoryText label, String today) {
        int labelCenter = (label.top + label.bottom) / 2;
        for (HistoryText other : texts) {
            int otherCenter = (other.top + other.bottom) / 2;
            if (other.left > label.left
                    && Math.abs(otherCenter - labelCenter) <= px(12)
                    && other.text.startsWith(today)) {
                return true;
            }
        }
        return false;
    }

    private List<HistoryText> visibleHistoryTexts() {
        List<HistoryText> texts = new ArrayList<>();
        for (WebElement element : driver.findElements(By.className("android.widget.TextView"))) {
            try {
                int[] box = visibleBox(element);
                if (box == null) {
                    continue;
                }
                String text = elementAttribute(element, "text");
                if (text == null) {
                    continue;
                }
                texts.add(new HistoryText(text.trim(), box[0], box[1], box[2], box[3]));
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return texts;
    }

    private List<HistoryText> visibleHistoryLabels() {
        List<HistoryText> labels = new ArrayList<>();
        for (WebElement element : driver.findElements(By.className("android.widget.TextView"))) {
            try {
                int[] box = visibleBox(element);
                if (box == null) {
                    continue;
                }
                addHistoryLabel(labels, elementAttribute(element, "text"), box);
                String description = elementAttribute(element, "content-desc");
                String text = elementAttribute(element, "text");
                if (description != null && (text == null || !description.trim().equalsIgnoreCase(text.trim()))) {
                    addHistoryLabel(labels, description, box);
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return labels;
    }

    private void addHistoryLabel(List<HistoryText> labels, String value, int[] box) {
        if (value == null || value.isBlank()) {
            return;
        }
        labels.add(new HistoryText(value.trim(), box[0], box[1], box[2], box[3]));
    }

    private boolean rowShowsProductName(List<HistoryText> labels, HistoryFact row, String name) {
        int bandTop = row.top - px(56);
        int bandBottom = row.top + px(72);
        return productNameInBand(labels, name, bandTop, bandBottom);
    }

    private boolean cardShowsProductName(List<HistoryText> labels, EiehkCard card, String name) {
        int bandTop = card.top - px(56);
        int bandBottom = card.top + px(80);
        return productNameInBand(labels, name, bandTop, bandBottom);
    }

    private boolean productNameInBand(List<HistoryText> labels, String name, int bandTop, int bandBottom) {
        for (HistoryText label : labels) {
            int center = (label.top + label.bottom) / 2;
            if (center >= bandTop && center <= bandBottom && name.equalsIgnoreCase(label.text)) {
                return true;
            }
        }
        return false;
    }

    private String normalizeHistoryVolume(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        String text = value.replaceAll("(?i)\\s*lots?", "").trim().replace(",", "");
        if (!text.matches("\\d+(\\.\\d+)?")) {
            return null;
        }
        return new BigDecimal(text).setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private HistoryCodeResult latestHistoryTransactionTimeFormat() {
        String symbol = trimmedHistorySymbol();
        String volume = expectedHistoryVolume();
        String openDirection = expectedHistoryDirection("Open");
        String closeDirection = expectedHistoryDirection("Close");
        if (symbol == null || volume == null || volume.isBlank()
                || openDirection == null || openDirection.isBlank()
                || closeDirection == null || closeDirection.isBlank()) {
            return new HistoryCodeResult(false, "Portfolio history symbol, direction, or volume is missing");
        }
        List<HistoryText> texts = visibleHistoryTexts();
        LocalDate today = LocalDate.now();
        if (isEiehkHistory()) {
            return latestEiehkHistoryTimeFormat(texts, symbol, volume, openDirection, today);
        }
        return latestEblHistoryTimeFormat(texts, symbol, volume, openDirection, closeDirection, today);
    }

    private HistoryCodeResult latestEblHistoryTimeFormat(List<HistoryText> texts, String symbol, String volume,
                                                          String openDirection, String closeDirection,
                                                          LocalDate today) {
        int listTop = listAreaTopY();
        List<HistoryTimeSample> samples = new ArrayList<>();
        for (HistoryText actionText : texts) {
            if (actionText.top < listTop) {
                continue;
            }
            String action;
            String direction;
            if ("Open".equals(actionText.text)) {
                action = "Open";
                direction = openDirection;
            } else if ("Close".equals(actionText.text)) {
                action = "Close";
                direction = closeDirection;
            } else {
                continue;
            }
            if (!eblHistoryTimeRowMatches(texts, actionText, symbol, volume, direction)) {
                continue;
            }
            String displayed = eblDateLikeTimeBelow(texts, actionText);
            samples.add(new HistoryTimeSample(
                    action, displayed, strictTodayHistoryTimestamp(displayed, today), actionText.top));
        }
        return historyTimeFormatResult(samples, symbol, volume);
    }

    // Same symbol, volume, and direction windows as eblSymbolFact. Direction comes from expectedHistoryDirection.
    private boolean eblHistoryTimeRowMatches(List<HistoryText> texts, HistoryText actionText, String symbol,
                                              String volume, String direction) {
        int centerY = (actionText.top + actionText.bottom) / 2;
        boolean volumeFound = false;
        boolean directionFound = false;
        HistoryText identifier = null;
        int identifierGap = Integer.MAX_VALUE;
        for (HistoryText other : texts) {
            int otherCenter = (other.top + other.bottom) / 2;
            int delta = otherCenter - centerY;
            if (direction.equalsIgnoreCase(other.text)
                    && other.left >= actionText.right - px(8)
                    && Math.abs(delta) <= px(16)) {
                directionFound = true;
            }
            if (volume.equals(normalizeHistoryVolume(other.text))
                    && other.left >= actionText.right - px(8)
                    && Math.abs(delta) <= px(16)) {
                volumeFound = true;
            }
            if (other.left < actionText.left && delta < 0 && -delta <= px(56) && -delta < identifierGap) {
                identifierGap = -delta;
                identifier = other;
            }
        }
        return volumeFound && directionFound && identifier != null && symbol.equals(identifier.text);
    }

    // Nearest text under the action whose body contains a date separator and ':'.
    private String eblDateLikeTimeBelow(List<HistoryText> texts, HistoryText actionText) {
        int centerY = (actionText.top + actionText.bottom) / 2;
        HistoryText best = null;
        int bestDelta = Integer.MAX_VALUE;
        for (HistoryText other : texts) {
            int delta = (other.top + other.bottom) / 2 - centerY;
            if (delta > 0 && delta <= px(56) && delta < bestDelta && dateLikeHistoryTime(other.text)) {
                bestDelta = delta;
                best = other;
            }
        }
        return best == null ? null : best.text.trim();
    }

    private HistoryCodeResult latestEiehkHistoryTimeFormat(List<HistoryText> texts, String symbol, String volume,
                                                            String direction, LocalDate today) {
        int listTop = listAreaTopY();
        List<HistoryText> labels = visibleHistoryLabels();
        List<EiehkHistoryTimeCard> cards = new ArrayList<>();
        for (HistoryText symbolText : texts) {
            if (symbolText.top < listTop || !symbol.equals(symbolText.text)) {
                continue;
            }
            if (!eiehkVolumeOnIdentifierLine(texts, symbolText, volume)
                    && !eiehkValueInCard(labels, symbolText, volume)) {
                continue;
            }
            String cardDirection = eiehkDirectionOnIdentifierLine(texts, symbolText);
            if (cardDirection == null) {
                cardDirection = eiehkDirectionInCard(labels, symbolText);
            }
            if (cardDirection == null || !direction.equalsIgnoreCase(cardDirection)) {
                continue;
            }
            String openTime = eiehkRawActionTime(texts, labels, symbolText, "Open:");
            String closeTime = eiehkRawActionTime(texts, labels, symbolText, "Close:");
            cards.add(new EiehkHistoryTimeCard(
                    new HistoryTimeSample(
                            "Open", openTime, strictTodayHistoryTimestamp(openTime, today), symbolText.top),
                    new HistoryTimeSample(
                            "Close", closeTime, strictTodayHistoryTimestamp(closeTime, today), symbolText.top)));
        }
        EiehkHistoryTimeCard selected = selectEiehkHistoryTimeCard(cards);
        if (selected == null) {
            return historyTimeFormatResult(new ArrayList<>(), symbol, volume);
        }
        List<HistoryTimeSample> samples = new ArrayList<>();
        samples.add(selected.openSample);
        samples.add(selected.closeSample);
        return historyTimeFormatResult(samples, symbol, volume);
    }

    // One card supplies both times. A strict today Open uses latestValidHistoryTime ordering.
    private EiehkHistoryTimeCard selectEiehkHistoryTimeCard(List<EiehkHistoryTimeCard> cards) {
        if (cards == null || cards.isEmpty()) {
            return null;
        }
        List<HistoryTimeSample> opens = new ArrayList<>();
        for (EiehkHistoryTimeCard card : cards) {
            opens.add(card.openSample);
        }
        HistoryTimeSample strictOpen = latestValidHistoryTime(opens, "Open");
        if (strictOpen != null) {
            for (EiehkHistoryTimeCard card : cards) {
                if (card.openSample == strictOpen) {
                    return card;
                }
            }
        }
        EiehkHistoryTimeCard withOpenText = null;
        EiehkHistoryTimeCard smallest = null;
        for (EiehkHistoryTimeCard card : cards) {
            if (smallest == null || card.openSample.top < smallest.openSample.top) {
                smallest = card;
            }
            if (card.openSample.displayedTime != null && !card.openSample.displayedTime.isBlank()
                    && (withOpenText == null || card.openSample.top < withOpenText.openSample.top)) {
                withOpenText = card;
            }
        }
        return withOpenText != null ? withOpenText : smallest;
    }

    // Texts first, then visibleHistoryLabels, matching the eiehkActionTimestamp fallback.
    private String eiehkRawActionTime(List<HistoryText> texts, List<HistoryText> labels, HistoryText identifier,
                                       String caption) {
        String fromTexts = eiehkCaptionDateLikeTime(texts, identifier, caption);
        if (fromTexts != null) {
            return fromTexts;
        }
        return eiehkCaptionDateLikeTime(labels, identifier, caption);
    }

    private String eiehkCaptionDateLikeTime(List<HistoryText> nodes, HistoryText identifier, String caption) {
        int identifierCenter = (identifier.top + identifier.bottom) / 2;
        int minDelta = eiehkCaptionMinDelta(caption);
        int maxDelta = eiehkCaptionMaxDelta(caption);
        for (HistoryText other : nodes) {
            int delta = (other.top + other.bottom) / 2 - identifierCenter;
            if (!historyActionCaption(other.text, caption) || other.left > identifier.right
                    || delta < minDelta || delta > maxDelta) {
                continue;
            }
            String displayed = dateLikeTextRightOfCaption(nodes, other);
            if (displayed != null) {
                return displayed;
            }
        }
        return null;
    }

    private String dateLikeTextRightOfCaption(List<HistoryText> nodes, HistoryText caption) {
        int captionCenter = (caption.top + caption.bottom) / 2;
        HistoryText best = null;
        for (HistoryText other : nodes) {
            int otherCenter = (other.top + other.bottom) / 2;
            if (other.left > caption.left
                    && Math.abs(otherCenter - captionCenter) <= px(12)
                    && dateLikeHistoryTime(other.text)
                    && (best == null || other.left < best.left)) {
                best = other;
            }
        }
        return best == null ? null : best.text.trim();
    }

    private boolean dateLikeHistoryTime(String text) {
        if (text == null) {
            return false;
        }
        String value = text.trim();
        return (value.indexOf('-') >= 0 || value.indexOf('/') >= 0) && value.indexOf(':') >= 0;
    }

    // dateValidator rejects extra characters, a slash date, and a missing seconds field.
    private LocalDateTime strictTodayHistoryTimestamp(String displayed, LocalDate today) {
        if (displayed == null || displayed.isBlank() || today == null) {
            return null;
        }
        String trimmed = displayed.trim();
        if (!abs.dateValidator(trimmed)) {
            return null;
        }
        try {
            LocalDateTime parsed = LocalDateTime.parse(trimmed, HISTORY_TIMESTAMP);
            if (!today.equals(parsed.toLocalDate())) {
                return null;
            }
            return parsed;
        } catch (DateTimeParseException ignored) {
            return null;
        }
    }

    private HistoryCodeResult historyTimeFormatResult(List<HistoryTimeSample> samples, String symbol, String volume) {
        HistoryTimeSample open = latestValidHistoryTime(samples, "Open");
        HistoryTimeSample close = latestValidHistoryTime(samples, "Close");
        if (open != null && close != null) {
            if (close.parsedToday.isBefore(open.parsedToday)) {
                return new HistoryCodeResult(false,
                        "Latest history Close time '" + close.displayedTime
                                + "' is before Open time '" + open.displayedTime + "'");
            }
            return new HistoryCodeResult(true, null);
        }
        String openDetail = actionTimeProblem(samples, "Open", symbol, volume);
        String closeDetail = actionTimeProblem(samples, "Close", symbol, volume);
        if (openDetail == null) {
            return new HistoryCodeResult(false, closeDetail);
        }
        if (closeDetail == null) {
            return new HistoryCodeResult(false, openDetail);
        }
        return new HistoryCodeResult(false, openDetail + ". " + closeDetail);
    }

    // Latest parsed today time, then the smaller top. comparingInt(top).reversed() selects the smaller top, matching latestFact.
    private HistoryTimeSample latestValidHistoryTime(List<HistoryTimeSample> samples, String action) {
        return samples.stream()
                .filter(sample -> action.equals(sample.action) && sample.parsedToday != null)
                .max(Comparator.comparing((HistoryTimeSample sample) -> sample.parsedToday)
                        .thenComparing(Comparator.comparingInt((HistoryTimeSample sample) -> sample.top).reversed()))
                .orElse(null);
    }

    // A date-like candidate that is not a strict today timestamp is reported from the smallest top.
    private String actionTimeProblem(List<HistoryTimeSample> samples, String action, String symbol, String volume) {
        boolean seen = false;
        HistoryTimeSample bad = null;
        for (HistoryTimeSample sample : samples) {
            if (!action.equals(sample.action)) {
                continue;
            }
            seen = true;
            if (sample.parsedToday != null) {
                return null;
            }
            if (sample.displayedTime != null && !sample.displayedTime.isBlank()
                    && (bad == null || sample.top < bad.top)) {
                bad = sample;
            }
        }
        if (bad != null) {
            return "Portfolio history " + action + " time '" + bad.displayedTime
                    + "' is not a strict uuuu-MM-dd HH:mm:ss timestamp for today";
        }
        if (!seen) {
            return "Portfolio history " + action + " record was not visible for " + symbol
                    + " volume " + volume;
        }
        return "Portfolio history " + action + " time was not read for " + symbol + " volume " + volume;
    }

    private record HistoryText(String text, int left, int top, int right, int bottom) {
    }

    private record HistoryCandidate(String identifier, LocalDateTime timestamp, int top) {
    }

    private record HistoryCodeResult(boolean shown, String detail) {
    }

    private record HistoryFact(String action, String identifier, String direction, LocalDateTime timestamp, int top) {
    }

    private record EiehkCard(String identifier, String direction, LocalDateTime openTime,
                             LocalDateTime closeTime, int top) {
    }

    private record HistoryTimeSample(String action, String displayedTime, LocalDateTime parsedToday, int top) {
    }

    private record EiehkHistoryTimeCard(HistoryTimeSample openSample, HistoryTimeSample closeSample) {
    }
}
