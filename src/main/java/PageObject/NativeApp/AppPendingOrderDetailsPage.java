package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AppPendingOrderDetailsPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;

    public AppPendingOrderDetailsPage(AppiumDriver driver){
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

    public String getHeader(){
        if (driver instanceof AndroidDriver) {
            abs.waitUtilElementFind(headerAos);
            return headerAos.getText();
        }
        return "";
    }

    public String getDetailValue(String label) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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
                return null;
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

        System.out.println("open price: " + targetPrice);
        System.out.println("lot size: " + lotSize);
        System.out.println("contract: " + contract);

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
