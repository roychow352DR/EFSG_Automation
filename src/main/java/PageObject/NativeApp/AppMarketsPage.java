package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.nativekey.KeyEventMetaModifier;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.GetPageElement;

import java.time.Duration;
import java.util.List;

public class AppMarketsPage {

    private static final By INSTRUMENT_TICKET = By.xpath(
            "//*[@text='Lot Size' or @content-desc='Lot Size'"
                    + " or @text='Buy at' or @content-desc='Buy at'"
                    + " or @text='Sell at' or @content-desc='Sell at']");

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    private final GetPageElement getPageElement;

    public static String tradeSymbol;

    public AppMarketsPage(AppiumDriver driver) {
        this.driver = driver;
        this.abs = new MobileAbstractComponents(driver);
        this.getPageElement = new GetPageElement(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout//android.view.ViewGroup[1]//android.widget.FrameLayout//android.view.ViewGroup[1]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup")
    WebElement applicationButtonAos;

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]//android.view.ViewGroup[2]/android.view.View/android.view.View[2]/android.view.ViewGroup")
    WebElement marketFooterBtnAos;

    @FindBy(xpath = "//android.widget.TextView[@text=\"XAUUSD\"]/parent::android.view.ViewGroup")
    WebElement marketXAUUSDAos;



    public void tapButtonOnMarketsPage(String buttonName) {
        if (driver instanceof AndroidDriver) {
            switch (buttonName) {
                case "Open a Live Trading Accounts" -> {
                    abs.waitUntilElementFind(applicationButtonAos);
                    applicationButtonAos.click();
                }
                default -> System.out.println("Button not found");
            }
        }
    }

    public void tapSymbol(String symbol) {
        tradeSymbol = symbol;
        if (!(driver instanceof AndroidDriver)) {
            return;
        }
        waitForMarketsList();
        TimeoutException lastError = null;
        for (int pass = 0; pass < 4; pass++) {
            for (By locator : symbolLocators(symbol)) {
                try {
                    abs.tapVisible(locator, pass == 0 ? 10 : 4);
                    return;
                } catch (TimeoutException e) {
                    lastError = e;
                }
            }
            scrollSymbolIntoView(symbol);
        }
        throw lastError != null
                ? lastError
                : new TimeoutException("Symbol was not visible: " + symbol);
    }

    private void waitForMarketsList() {
        if (isMarketsListVisible(4)) {
            return;
        }
        try {
            new AppFooter(driver).tapFooterButton("Markets");
        } catch (TimeoutException ignored) {
        }
        if (!isMarketsListVisible(12)) {
            throw new TimeoutException("Markets page was not visible");
        }
    }

    private boolean isMarketsListVisible(int seconds) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(seconds)).until(d ->
                    !d.findElements(By.xpath(
                            "//*[@text='Watchlist' or @text='Product' or @text='XAUUSD']"
                    )).isEmpty());
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private void scrollSymbolIntoView(String symbol) {
        try {
            driver.findElement(AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                            + "new UiSelector().text(\"" + symbol + "\"))"
            ));
        } catch (Exception e) {
            abs.swipeUp(driver);
        }
    }

    private List<By> symbolLocators(String symbol) {
        return List.of(
                By.xpath("//android.widget.TextView[@text=\"" + symbol + "\"]"),
                By.xpath("//*[@text=\"" + symbol + "\"]"),
                By.xpath("//android.widget.TextView[contains(@text,\"" + symbol + "\")]"),
                By.xpath("//*[contains(@text,\"" + symbol + "\")]"),
                By.xpath("//android.widget.TextView[@text=\"" + symbol + "\"]/parent::android.view.ViewGroup")
        );
    }

    public void tapSearchButton() {
        requireAndroid();
        waitForMarketsList();
        TimeoutException lastError = null;
        for (By locator : searchButtonLocators()) {
            try {
                abs.tapVisible(locator, 6);
                waitForSearchField();
                getPageElement.logInfo("Tapped the markets search button");
                return;
            } catch (TimeoutException e) {
                lastError = e;
            }
        }
        throw lastError != null
                ? lastError
                : new TimeoutException("Search button was not visible on the markets page");
    }

    public void fillSearchField(String value) {
        requireAndroid();
        requireSymbol(value);
        AndroidDriver androidDriver = (AndroidDriver) driver;
        TimeoutException lastError = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            typeIntoSearchField(androidDriver, waitForSearchField(), value);
            try {
                new WebDriverWait(driver, Duration.ofSeconds(4))
                        .ignoring(StaleElementReferenceException.class)
                        .until(d -> value.equals(currentSearchFieldValue()));
                getPageElement.logInfo("Filled markets search field with " + value);
                return;
            } catch (TimeoutException e) {
                lastError = e;
                getPageElement.logInfo("Markets search field was '" + currentSearchFieldValue()
                        + "' after typing '" + value + "', attempt " + attempt);
            }
        }
        throw new TimeoutException(
                "Search field did not show '" + value + "'. Last value: '" + currentSearchFieldValue() + "'",
                lastError);
    }

    public String searchFieldValue() {
        requireAndroid();
        WebElement field = findDisplayedSearchField();
        if (field == null) {
            throw new TimeoutException("Search field was not visible on the markets page");
        }
        String value = fieldText(field);
        if (value.isBlank()) {
            throw new TimeoutException("Search field value was blank");
        }
        return value;
    }

    public boolean searchResultRowIsVisible(String symbol) {
        requireAndroid();
        requireSymbol(symbol);
        try {
            new WebDriverWait(driver, Duration.ofSeconds(12))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> displayedResultRow(symbol) != null);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean instrumentTicketIsVisible() {
        requireAndroid();
        for (WebElement element : driver.findElements(INSTRUMENT_TICKET)) {
            try {
                if (element.isDisplayed()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return false;
    }

    private void typeIntoSearchField(AndroidDriver androidDriver, WebElement field, String value) {
        abs.tapElement(field);
        // Wait until the field has focus. Pressing the first key during that wait drops it.
        abs.sleep(250);
        clearSearchField(androidDriver, field);
        for (int i = 0; i < value.length(); i++) {
            androidDriver.pressKey(keyEventFor(value.charAt(i)));
        }
    }

    private void clearSearchField(AndroidDriver androidDriver, WebElement field) {
        try {
            field.clear();
        } catch (RuntimeException ignored) {
        }
        String remaining;
        try {
            remaining = fieldText(field);
        } catch (StaleElementReferenceException e) {
            return;
        }
        int deletes = Math.min(remaining.length(), 32);
        for (int i = 0; i < deletes; i++) {
            androidDriver.pressKey(new KeyEvent(AndroidKey.DEL));
        }
    }

    private KeyEvent keyEventFor(char c) {
        if (c >= 'A' && c <= 'Z') {
            return new KeyEvent(AndroidKey.valueOf(String.valueOf(c)))
                    .withMetaModifier(KeyEventMetaModifier.SHIFT_ON);
        }
        if (c >= 'a' && c <= 'z') {
            return new KeyEvent(AndroidKey.valueOf(String.valueOf(Character.toUpperCase(c))));
        }
        if (c >= '0' && c <= '9') {
            return new KeyEvent(AndroidKey.valueOf("DIGIT_" + c));
        }
        throw new IllegalArgumentException("Unsupported search character: " + c);
    }

    private List<By> searchButtonLocators() {
        // EIEHK Markets watchlist on emulator-5554. The magnifying glass has no text or content-desc.
        // It is the first clickable sibling after the Watchlist title. The settings icon is the next sibling.
        return List.of(
                By.xpath("//android.widget.TextView[@text='Watchlist']"
                        + "/following-sibling::android.view.ViewGroup[@clickable='true'][1]")
        );
    }

    private WebElement waitForSearchField() {
        try {
            return new WebDriverWait(driver, Duration.ofSeconds(8))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> findDisplayedSearchField());
        } catch (TimeoutException e) {
            throw new TimeoutException("Search field was not visible on the markets page", e);
        }
    }

    private WebElement findDisplayedSearchField() {
        for (WebElement element : driver.findElements(By.xpath("//android.widget.EditText"))) {
            try {
                if (element.isDisplayed()) {
                    return element;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return null;
    }

    private String currentSearchFieldValue() {
        WebElement field = findDisplayedSearchField();
        if (field == null) {
            return "";
        }
        try {
            return fieldText(field);
        } catch (StaleElementReferenceException e) {
            return "";
        }
    }

    private String fieldText(WebElement element) {
        String text = normalized(element.getAttribute("text"));
        if (!text.isEmpty() && !isSearchHint(text)) {
            return text;
        }
        String described = normalized(element.getAttribute("content-desc"));
        if (!described.isEmpty() && !isSearchHint(described)) {
            return described;
        }
        return "";
    }

    private boolean isSearchHint(String value) {
        return "search".equalsIgnoreCase(value);
    }

    private WebElement displayedResultRow(String symbol) {
        for (WebElement element : driver.findElements(By.xpath("//*[@text or @content-desc]"))) {
            try {
                if (isSearchField(element) || !element.isDisplayed()) {
                    continue;
                }
                if (symbol.equals(normalized(element.getAttribute("text")))
                        || symbol.equals(normalized(element.getAttribute("content-desc")))) {
                    return element;
                }
            } catch (StaleElementReferenceException ignored) {
            }
        }
        return null;
    }

    private boolean isSearchField(WebElement element) {
        String className = normalized(element.getAttribute("class"));
        if (className.isEmpty()) {
            className = normalized(element.getAttribute("className"));
        }
        return className.contains("EditText");
    }

    private String normalized(String value) {
        if (value == null || "null".equalsIgnoreCase(value)) {
            return "";
        }
        return value.trim();
    }

    private void requireSymbol(String symbol) {
        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Symbol was blank");
        }
        if (symbol.indexOf('\'') >= 0 || symbol.indexOf('"') >= 0) {
            throw new IllegalArgumentException("Symbol contains a quote: " + symbol);
        }
    }

    private void requireAndroid() {
        if (!(driver instanceof AndroidDriver)) {
            throw new UnsupportedOperationException("Markets search is implemented for Android");
        }
    }

}
