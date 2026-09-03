package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.GetPageElement;

import java.math.BigDecimal;
import java.time.Duration;

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
        getPageElement.waitAndCaptureIfNeeded(By.xpath("//*[@text='Pending Order Details']"), 10);
        String uiLabel = getPageElement.mapUiLabel(label);
        String rawValue = getPageElement.readLabelValueFast(uiLabel);
        if (rawValue == null || rawValue.isBlank()) {
            throw new NoSuchElementException("Could not find value on Pending Order Details for label: " + uiLabel);
        }
        return normalizeDetailValue(label, rawValue);
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
