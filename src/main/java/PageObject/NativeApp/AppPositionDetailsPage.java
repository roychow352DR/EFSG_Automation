package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AppPositionDetailsPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;

    public AppPositionDetailsPage(AppiumDriver driver) {
        this.driver = driver;
        abs = new MobileAbstractComponents(driver);
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
            abs.waitUntilElementFind(headerAos);
            return headerAos.getText();
        }
        return "";
    }


    public String getDetailValue(String label) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        wait.ignoring(StaleElementReferenceException.class);

        try {
            return wait.until(d -> {
                List<WebElement> elements = d.findElements(By.className("android.widget.TextView"));
                List<String> texts = new ArrayList<>();

                for (WebElement element : elements) {
                    texts.add(element.getText());
                }

                for (int i = 0; i < texts.size() - 1; i++) {
                    String currentLabel = texts.get(i);

                    if (currentLabel != null && currentLabel.equalsIgnoreCase(label)) {
                        if (currentLabel.equalsIgnoreCase("Product")){
                            return productAos.getText();
                        }
                        else {
                            return normalizeDetailValue(label, texts.get(i + 1));
                        }
                    }
                }
                return "";
            });
        } catch (TimeoutException e) {
            return null;
        }
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
        BigDecimal openPrice = new BigDecimal(getDetailValue("Open Price").trim());
        BigDecimal lotSize = new BigDecimal(getDetailValue("Volume").trim());
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
