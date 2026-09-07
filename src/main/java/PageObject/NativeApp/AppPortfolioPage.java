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
            abs.waitUntilElementFind(confirmBtnAos);
            confirmBtnAos.click();
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
        abs.waitUntilElementFind(titleAos);
        return titleAos.getText();
    }

    public String getCheckedProduct() {
        if (driver instanceof AndroidDriver) {
            return driver.findElement(By.xpath("//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup" +
                    "/parent::android.view.ViewGroup/android.widget.TextView")).getText();
        }
        return "No checked product found";
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
        if (driver instanceof AndroidDriver) {
            switch (buttonName) {
                case "arrow" -> {
                    abs.waitUntilElementFind(arrowBtnAos);
                    arrowBtnAos.click();
                }
                case "close" -> {
                    abs.waitUntilElementFind(closeBtnAos);
                    closeBtnAos.click();
                }
                case "edit" -> {
                    abs.waitUntilElementFind(editBtnAos);
                    editBtnAos.click();
                }
                case "cancel" -> {
                    abs.waitUntilElementFind(cancelBtnAos);
                    cancelBtnAos.click();
                }
                case "detail" -> {
                    abs.waitUntilElementClickable(detailBtnAos);
                    detailBtnAos.click();
                }
            }
        }
    }

    public boolean confirmationDialogueIsDisplayed() {
        if (driver instanceof AndroidDriver) {
            return confirmationDialogueAos.isDisplayed();
        }
        return false;
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
            confirmBtnAos.click();
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
