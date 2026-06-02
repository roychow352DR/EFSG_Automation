package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.math.BigDecimal;
import java.time.Duration;
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

    public String getHeader() {
        if (driver instanceof AndroidDriver) {
            abs.waitUtilElementFind(headerAos);
            return headerAos.getText();
        }
        return "";
    }


    public String getDetailValue(String value) {
        final int MAX_ATTEMPTS = 3;

        for (int attempt = 1; true; attempt++) {
            try {
                List<WebElement> texts = driver.findElements(By.className("android.widget.TextView"));

                // up to size - 2 so i+1 is always valid
                for (int i = 0; i < texts.size() - 1; i++) {
                    WebElement labelElement = texts.get(i);
                    String labelText = labelElement.getText();   // read once

                    if (labelText.equalsIgnoreCase(value)) {
                        WebElement valueElement = texts.get(i + 1);
                        String rawValue = valueElement.getText(); // read once

                        if (value.equalsIgnoreCase("Volume")) {
                            return rawValue.split("Lots")[0].trim();
                        } else if (value.equalsIgnoreCase("Initial Margin")) {
                            return rawValue.split("USD")[1].trim().replace(",", "");
                        } else if (value.equalsIgnoreCase("Contract Value")) {
                            return rawValue.split(abs.getQuoteCurrency(AppMarketsPage.tradeSymbol))[1].trim().replace(",", "");
                            }
                        else {
                            return rawValue;
                        }
                    }
                }

                // not found
                return null;

            } catch (StaleElementReferenceException e) {
                if (attempt == MAX_ATTEMPTS) {
                    throw e;  // rethrow after final attempt
                }
            }
        }
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


}
