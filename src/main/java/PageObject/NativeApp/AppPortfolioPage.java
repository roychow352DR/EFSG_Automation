package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
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

    @AndroidFindBy(accessibility = "Open\n" +
            "Positions")
    WebElement positionTabAos;

    @AndroidFindBy(accessibility = "Pending\n" +
            "Orders")
    WebElement pendingOrderTabAos;


    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup[1]")
    WebElement arrowBtnAos;

    @AndroidFindBy(accessibility = "History")
    WebElement historyTabAos;

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

    private void waitForPortfolioPage() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(15))
                    .until(d -> !d.findElements(By.xpath(
                            "//*[@text='Open Positions' or @text='Pending Orders' or @text='Portfolio'"
                                    + " or contains(@text,'Show all') or contains(@text,'Show All')]"
                    )).isEmpty());
        } catch (TimeoutException ignored) {
        }
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

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                WebElement tab = switch (tabName) {
                    case "Open Positions" -> positionTabAos;
                    case "Pending Orders" -> pendingOrderTabAos;
                    case "History" -> historyTabAos;
                    default -> null;
                };

                if (tab == null) {
                    return false;
                }

                String selected = tab.getDomAttribute("selected");
                if (selected == null) {
                    selected = tab.getDomProperty("selected");
                }

                return "true".equalsIgnoreCase(selected);
            } catch (StaleElementReferenceException e) {
                System.out.println("Stale element when checking tab selection for: " + tabName);
            }
        }

        return false;
    }

    public void selectTab(String tabName) {
        if (driver instanceof AndroidDriver) {
            WebElement tab = switch (tabName) {
                case "Open Positions" -> positionTabAos;
                case "Pending Orders" -> pendingOrderTabAos;
                case "History" -> historyTabAos;
                default -> null;
            };

            if (tab != null) {
                abs.waitUntilElementFind(tab);
                tab.click();
            }
        }
    }

    public void tapButtonOnRow(String buttonName) {
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        waitForPortfolioPage();
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
        int[] row = firstPositionRowBounds();
        int listTop = row != null ? row[1] : listAreaTopY();
        int footerTop = row != null ? row[1] + row[3] : footerTopY();
        int minLeft = (int) (driver.manage().window().getSize().getWidth() * 0.58);
        List<int[]> all = new ArrayList<>();
        for (WebElement el : clickableRowCandidates()) {
            try {
                int[] box = visibleBox(el);
                if (!isPortfolioRowCta(box, listTop, footerTop, minLeft)) {
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
        int rowTop = all.getFirst()[1];
        List<int[]> clustered = new ArrayList<>();
        for (int[] box : all) {
            if (Math.abs(box[1] - rowTop) <= 80) {
                clustered.add(box);
            }
        }
        return dedupeByX(clustered);
    }

    private int[] firstPositionRowBounds() {
        int listTop = listAreaTopY();
        int footerTop = footerTopY();
        Dimension window = driver.manage().window().getSize();
        int[] best = null;
        int bestY = Integer.MAX_VALUE;
        List<By> markers = new ArrayList<>();
        String symbol = AppMarketsPage.tradeSymbol;
        if (symbol != null && !symbol.isBlank()) {
            markers.add(By.xpath("//*[@text='" + symbol + "']"));
        }
        markers.add(By.xpath("//*[@text='BUY' or @text='SELL']"));
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
                        int top = Math.max(listTop, box[1] - 28);
                        int bottom = Math.min(footerTop - 8, box[3] + 96);
                        best = new int[]{0, top, window.getWidth(), Math.max(88, bottom - top)};
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
        if (width < 16 || width > 240 || height < 16 || height > 300) {
            return false;
        }
        if (box[0] < minLeft) {
            return false;
        }
        if (box[1] < listTop - 20 || box[3] > footerTop - 8) {
            return false;
        }
        return true;
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
        double ratio = switch (name) {
            case "close", "cancel" -> 0.70;
            case "edit" -> 0.82;
            default -> 0.93;
        };
        int[] row = firstPositionRowBounds();
        int y;
        if (row != null) {
            y = row[1] + Math.max(36, row[3] / 2);
        } else {
            y = listAreaTopY() + Math.max(90, (int) (window.getHeight() * 0.04));
        }
        return new Point((int) (window.getWidth() * ratio), y);
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
            if (Math.abs(center - lastCenter) <= 24) {
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
                By.xpath("//*[@content-desc='Newest to Oldest' or @text='Newest to Oldest']")
        )) {
            for (WebElement el : driver.findElements(locator)) {
                int[] box = parseBounds(elementAttribute(el, "bounds"));
                if (box != null && box[3] > box[1]) {
                    return box[3] + 8;
                }
            }
        }
        return (int) (driver.manage().window().getSize().getHeight() * 0.52);
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
        return window.getHeight() - 140;
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
