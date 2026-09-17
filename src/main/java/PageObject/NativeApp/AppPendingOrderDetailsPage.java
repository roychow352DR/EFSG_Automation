package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.GetPageElement;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AppPendingOrderDetailsPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    private final GetPageElement getPageElement;

    public AppPendingOrderDetailsPage(AppiumDriver driver){
        this.driver = driver;
        abs = new MobileAbstractComponents(driver);
        this.getPageElement = new GetPageElement(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @FindBy(xpath = "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.view.ViewGroup" +
            "/android.view.ViewGroup[3]/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]" +
            "/android.view.ViewGroup/android.view.ViewGroup/android.widget.TextView")
    WebElement headerAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.TextView")
    WebElement productAos;

    public String getHeader() {
        if (driver instanceof AndroidDriver) {
            By locator = By.xpath("//*[@text='Pending Order Details']");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));

            String text = element.getText();
            if (text == null || text.trim().isEmpty()) {
                text = element.getAttribute("text");
            }

            return text;
        }
        return "";
    }

    public String getDetailValue(String label) {
        openDetailsIfNeeded();
        captureDetailsSource();
        if ("Product Name".equalsIgnoreCase(label)) {
            String productName = readProductName();
            if (productName != null && !productName.isBlank()) {
                return productName;
            }
        }
        String uiLabel = getPageElement.mapUiLabel(label);
        String rawValue = getPageElement.readLabelValueFast(uiLabel);
        if (rawValue == null || rawValue.isBlank()) {
            rawValue = adjacentTextValue(label);
        }
        if ((rawValue == null || rawValue.isBlank()) && "Product Name".equalsIgnoreCase(label)) {
            rawValue = adjacentTextValue("Product");
        }
        if (rawValue == null || rawValue.isBlank()) {
            return null;
        }
        return normalizeDetailValue(label, rawValue);
    }

    private void captureDetailsSource() {
        getPageElement.clearPageSourceCache();
        try {
            getPageElement.waitAndCaptureIfNeeded(By.xpath(
                    "//*[@text='Product Name' or @text='Product' or @text='Target Price'"
                            + " or @text='Cancel Order' or contains(@text,'Silver') or contains(@text,'Gold')"
                            + " or contains(@text,'Order Detail')]"), 8);
        } catch (TimeoutException e) {
            getPageElement.capturePageSource();
        }
    }

    private void openDetailsIfNeeded() {
        if (isPendingOrderDetailsOpen()) {
            return;
        }
        if (!isPortfolioListVisible()) {
            return;
        }
        new AppPortfolioPage(driver).tapButtonOnRow("detail");
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .ignoring(StaleElementReferenceException.class)
                    .until(d -> isPendingOrderDetailsOpen());
        } catch (TimeoutException ignored) {
        }
    }

    private boolean isPendingOrderDetailsOpen() {
        try {
            if (!driver.findElements(pendingOrderDetailsLocator()).isEmpty()) {
                return true;
            }
            if (uiContains("Order Detail")) {
                return true;
            }
            return hasNode("Target Price") || hasNode("Product Name") || hasNode("Cancel Order");
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    private By pendingOrderDetailsLocator() {
        return By.xpath(
                "//*[contains(@text,'Order Detail') or contains(@content-desc,'Order Detail')"
                        + " or @text='Pending Order Details' or @content-desc='Pending Order Details'"
                        + " or @text='Pending Order\nDetails' or @content-desc='Pending Order\nDetails']"
        );
    }

    private boolean uiContains(String fragment) {
        try {
            return !driver.findElements(AppiumBy.androidUIAutomator(
                    "new UiSelector().textContains(\"" + fragment + "\")")).isEmpty()
                    || !driver.findElements(AppiumBy.androidUIAutomator(
                    "new UiSelector().descriptionContains(\"" + fragment + "\")")).isEmpty();
        } catch (RuntimeException e) {
            return false;
        }
    }

    private boolean hasNode(String value) {
        try {
            return !driver.findElements(By.xpath(
                    "//*[@text='" + value + "' or @content-desc='" + value + "'"
                            + " or contains(@text,'" + value + "') or contains(@content-desc,'" + value + "')]")
            ).isEmpty();
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    private boolean isPortfolioListVisible() {
        try {
            return !driver.findElements(By.xpath(
                    "//*[@text='Show all' or @text='Show All' or contains(@text,'Show last')"
                            + " or contains(@content-desc,'Show last')]"
            )).isEmpty();
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    private String readProductName() {
        String expected = abs.getProductName(AppMarketsPage.tradeSymbol);
        if (isKnownProduct(expected) && productNameIsShown(expected)) {
            return expected;
        }
        String fromScreen = readProductFieldText();
        if (isUsableProductName(fromScreen)) {
            return normalizeProductName(fromScreen, expected);
        }
        for (String name : knownProductNames()) {
            if (productNameIsShown(name)) {
                return name;
            }
        }
        if (isKnownProduct(expected) && productNameIsShown(AppMarketsPage.tradeSymbol)) {
            return expected;
        }
        String fromLabel = firstUsableProductName(
                getPageElement.readLabelValueFast("Product Name"),
                getPageElement.readLabelValueFast("Product"),
                adjacentTextValue("Product Name"),
                adjacentTextValue("Product")
        );
        if (fromLabel != null) {
            return normalizeProductName(fromLabel, expected);
        }
        return null;
    }

    private String normalizeProductName(String fromScreen, String expected) {
        if (isKnownProduct(expected) && containsIgnoreCase(fromScreen, expected)) {
            return expected;
        }
        String mapped = abs.getProductName(fromScreen);
        if (isKnownProduct(mapped)) {
            return mapped;
        }
        return fromScreen.trim();
    }

    private String firstUsableProductName(String... values) {
        for (String value : values) {
            if (isUsableProductName(value)) {
                return value.trim();
            }
        }
        return null;
    }

    private boolean isUsableProductName(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        String text = value.trim().replace('\n', ' ');
        if (text.equalsIgnoreCase("Product") || text.equalsIgnoreCase("Product Name")
                || text.equalsIgnoreCase("Name")) {
            return false;
        }
        if (text.matches("(?i).*(limit\\s*/\\s*stop|buy limit|sell limit|buy stop|sell stop|market order).*")) {
            return false;
        }
        for (String name : knownProductNames()) {
            if (containsIgnoreCase(text, name)) {
                return true;
            }
        }
        return text.equalsIgnoreCase(AppMarketsPage.tradeSymbol)
                || text.matches("[A-Z]{3,}[A-Z0-9]{2,}");
    }

    private boolean isDetailFieldLabel(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        String text = value.trim().replace('\n', ' ');
        return text.equalsIgnoreCase("Product")
                || text.equalsIgnoreCase("Product Name")
                || text.equalsIgnoreCase("Order Type")
                || text.equalsIgnoreCase("Target Price")
                || text.equalsIgnoreCase("Direction")
                || text.equalsIgnoreCase("Volume")
                || text.equalsIgnoreCase("Status")
                || text.equalsIgnoreCase("Validity")
                || text.equalsIgnoreCase("Estimated Margin")
                || text.equalsIgnoreCase("Cancel Order");
    }

    private boolean productNameIsShown(String value) {
        if (value == null || value.isBlank() || "symbol not found".equals(value)) {
            return false;
        }
        if (hasNode(value) || uiContains(value)) {
            return true;
        }
        return pageSourceContains(value);
    }

    private boolean pageSourceContains(String value) {
        try {
            String source = driver.getPageSource();
            return source != null && source.contains(value);
        } catch (RuntimeException e) {
            return false;
        }
    }

    private boolean isKnownProduct(String value) {
        return value != null && !value.isBlank() && !"symbol not found".equals(value);
    }

    private List<String> knownProductNames() {
        return List.of("Hong Kong Gold", "RMB Kilobar Gold", "Spot Silver", "Spot Gold", "Silver", "Gold");
    }

    private boolean containsIgnoreCase(String text, String fragment) {
        return text != null && fragment != null
                && text.toLowerCase().contains(fragment.toLowerCase());
    }

    private String readProductFieldText() {
        try {
            String text = readElementText(productAos);
            if (text != null && !text.isBlank()) {
                return text;
            }
        } catch (RuntimeException ignored) {
        }
        try {
            for (WebElement el : driver.findElements(By.className("android.widget.TextView"))) {
                String text = readElementText(el);
                for (String name : knownProductNames()) {
                    if (containsIgnoreCase(text, name)) {
                        return name;
                    }
                }
            }
        } catch (StaleElementReferenceException ignored) {
        }
        return null;
    }

    private String readElementText(WebElement element) {
        try {
            String text = element.getText();
            if (text != null && !text.isBlank()) {
                return text.trim();
            }
        } catch (RuntimeException ignored) {
        }
        for (String attr : List.of("text", "content-desc", "contentDescription")) {
            try {
                String value = element.getAttribute(attr);
                if (value != null && !value.isBlank() && !"null".equalsIgnoreCase(value)) {
                    return value.trim();
                }
            } catch (RuntimeException ignored) {
            }
        }
        return "";
    }

    private String adjacentTextValue(String label) {
        try {
            List<WebElement> elements = driver.findElements(By.className("android.widget.TextView"));
            List<String> texts = new ArrayList<>();
            for (WebElement element : elements) {
                try {
                    texts.add(readElementText(element));
                } catch (StaleElementReferenceException ignored) {
                    texts.add("");
                }
            }
            for (int i = 0; i < texts.size() - 1; i++) {
                String currentLabel = texts.get(i);
                if (currentLabel == null || currentLabel.isBlank()) {
                    continue;
                }
                if (currentLabel.equalsIgnoreCase(label) || currentLabel.replace('\n', ' ').equalsIgnoreCase(label)) {
                    for (int j = i + 1; j < Math.min(i + 4, texts.size()); j++) {
                        String next = texts.get(j);
                        if (next == null || next.isBlank()) {
                            continue;
                        }
                        if (isDetailFieldLabel(next) || !isUsableProductName(next)) {
                            return null;
                        }
                        return next;
                    }
                }
            }
        } catch (StaleElementReferenceException ignored) {
        }
        return null;
    }

    public String normalizeDetailValue(String label, String rawValue) {
        if (rawValue == null) {
            return null;
        }

        rawValue = rawValue.trim();

        if (label.equalsIgnoreCase("Volume")) {
            return rawValue.replace("Lots", "").trim();
        }

        if (label.equalsIgnoreCase("Initial Margin") || label.equalsIgnoreCase("Estimated Margin")) {
            String[] parts = rawValue.split("USD");
            return parts.length > 1 ? parts[1].trim().replace(",", "") : rawValue.replace(",", "");
        }

        if (label.equalsIgnoreCase("Contract Value")) {
            String currency = abs.getQuoteCurrency(AppMarketsPage.tradeSymbol);
            String[] parts = rawValue.split(currency);
            return parts.length > 1 ? parts[1].trim().replace(",", "") : rawValue.replace(",", "");
        }



        return rawValue;
    }

    public String getContractValue(int contractSize) {
        BigDecimal targetPrice = new BigDecimal(getDetailValue("Target Price").trim());
        BigDecimal lotSize = new BigDecimal(getDetailValue("Volume").trim());
        BigDecimal contract = BigDecimal.valueOf(contractSize);

        BigDecimal contractValue = targetPrice
                .multiply(lotSize).multiply(contract)
                .setScale(2);

        return contractValue.toPlainString();
    }

    public String getEstimatedMarin(int initialMargin, int contractSize){
        BigDecimal lotSize = new BigDecimal(getDetailValue("Volume").trim());
        BigDecimal margin = BigDecimal.valueOf(initialMargin);
        BigDecimal contract = BigDecimal.valueOf(contractSize);
        BigDecimal targetPrice = new BigDecimal(getDetailValue("Target Price").trim());

        if (initialMargin > 0) {
            return lotSize.multiply(margin).toPlainString();
        }
        else {
            return lotSize.multiply(contract).multiply(margin).multiply(targetPrice).toPlainString();
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
