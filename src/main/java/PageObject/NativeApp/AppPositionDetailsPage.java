package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.GetPageElement;

import java.math.BigDecimal;
import java.time.Duration;

public class AppPositionDetailsPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    private final GetPageElement getPageElement;

    public AppPositionDetailsPage(AppiumDriver driver) {
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
            By locator = By.xpath("//*[@text='Position Details']");

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
        abs.waitUntilElementVisible(By.xpath("//*[@text='Position Details']"));
        String uiLabel = getPageElement.mapPositionDetailsLabel(label);
        String rawValue = getPageElement.resolveLabelValue(uiLabel);
        if (rawValue == null || rawValue.isBlank()) {
            throw new NoSuchElementException("Could not find value on Position Details for label: " + uiLabel);
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

        if (label.equalsIgnoreCase("Initial Margin")) {
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
        String openPriceText = getDetailValue("Open Price");
        String volumeText = getDetailValue("Volume");
        if (openPriceText == null || volumeText == null) {
            throw new NoSuchElementException("Could not read Open Price or Volume on Position Details");
        }
        BigDecimal openPrice = new BigDecimal(openPriceText.trim());
        BigDecimal lotSize = new BigDecimal(volumeText.trim());
        BigDecimal contract = BigDecimal.valueOf(contractSize);

        System.out.println("open price: " + openPrice);
        System.out.println("lot size: " + lotSize);
        System.out.println("contract: " + contract);

        BigDecimal contractValue = openPrice
                .multiply(lotSize).multiply(contract)
                .setScale(2);

        return contractValue.toPlainString();
    }

    public String getDefaultInitialMargin() {
        BigDecimal initialMargin = new BigDecimal(getDetailValue("Initial Margin").trim());
        BigDecimal lotSize = new BigDecimal(getDetailValue("Volume").trim());

        BigDecimal defaultInitialMargin = initialMargin
                .divide(lotSize)
                .setScale(2);

        return defaultInitialMargin.toPlainString();
    }

    public String getValidationValue(String label) {
        return switch (label) {
            // case "Stop Loss Price", "Stop Loss" -> stopLossPrice;
            //  case "Take Profit Price", "Take Profit" -> takeProfitPrice;
            case "Direction" -> AppTradeView.selectedDirection;
            //  case "Volume", "Lots" -> lotSize;
            //  case "Stop Order Price" -> stopOrderPrice;
            //  case "Validity" -> validity;
            //   case "Est. Margin", "Estimated Margin" -> estMargin;
            case "Product" -> AppMarketsPage.tradeSymbol;
            case "Status" -> "Open";
            case "Product Name" -> abs.getProductName(AppMarketsPage.tradeSymbol);
            default -> null;
        };
    }

    public boolean isOpenPositionDateValid(){
        return abs.dateValidator(getDetailValue("Open Position Time"));
    }
}
