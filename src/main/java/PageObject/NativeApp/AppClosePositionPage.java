package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.GetPageElement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AppClosePositionPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;
    private final GetPageElement getPageElement;

    public AppClosePositionPage(AppiumDriver driver){
        this.driver = driver;
        this.abs = new MobileAbstractComponents(driver);
        this.getPageElement = new GetPageElement(driver);
        PageFactory.initElements(new AppiumFieldDecorator(driver, Duration.ofSeconds(10)), this);
    }

    @FindBy(className = "android.widget.EditText")
    WebElement editFieldAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[1]")
    WebElement minusBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[2]")
    WebElement plusBtnAos;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup[2]/android.view.ViewGroup[3]")
    WebElement allBtnAos;

    @FindBy(xpath = "(//android.widget.TextView[@text=\"Close Position\"])[1]")
    WebElement headerAos;

    @FindBy(xpath = "//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]/android.view.ViewGroup[11]")
    WebElement closeBtnConfirmAos;


    public String getEditFieldVal(){
        if (driver instanceof AndroidDriver) {
            abs.waitUntilElementVisible(editFieldAos);
            return editFieldAos.getText();
        }
        return "No edit field found";
    }

    public void clickBtn(String btnName){
        if (driver instanceof AndroidDriver) {
            switch (btnName) {
                case "-":
                    abs.waitUntilElementClickable(minusBtnAos);
                    minusBtnAos.click();
                    break;
                case "+":
                    abs.waitUntilElementClickable(plusBtnAos);
                    plusBtnAos.click();
                    break;
                case "All":
                    abs.waitUntilElementClickable(allBtnAos);
                    allBtnAos.click();
                    break;
            }
        }
    }

    public boolean getHeader(){
        if (!(driver instanceof AndroidDriver)) {
            return false;
        }
        return waitForClosePositionHeader() != null;
    }

    public String getConfirmationValue(String value) {
        List<WebElement> text = driver.findElements(By.className("android.widget.TextView"));
        for (int i = 0; i < text.size(); i++) {
            if (text.get(i).getText().equalsIgnoreCase(value)) {
                if (value.equalsIgnoreCase("Volume")) {
                    return text.get(i + 1).getText().split("Lots")[0].trim();
                } else if (value.equalsIgnoreCase("Contract Value")) {
                    return text.get(i + 1).getText().split("USD")[1].trim().replace(",", "");
                }
                else if (value.equalsIgnoreCase("Floating P/L")) {
                    String token = text.get(i + 1).getText().split(" ")[0].trim();
                    return token.startsWith("+") ? token.substring(1) : token;
                }
                return text.get(i + 1).getText();
            }
        }
        return null;
    }

    public String getFloatingPnL(int contractSize){
        BigDecimal currentPrice = new BigDecimal(getDetailValue("Current Price").trim());
        BigDecimal openPrice = new BigDecimal(getDetailValue("Open Price").trim());
        BigDecimal lotSize = new BigDecimal(AppInstrumentDetailsPage.lotSize.trim());
        BigDecimal contract = BigDecimal.valueOf(contractSize);

        BigDecimal pnl = currentPrice
                .subtract(openPrice)
                .multiply(lotSize)
                .multiply(contract)
                .setScale(2, RoundingMode.HALF_UP);

        return pnl.toPlainString();

    }

    public String normalizeConfirmationValue(String label, String rawValue) {
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

        if (label.equalsIgnoreCase("Floating P/L")) {
            String token = rawValue.split(" ")[0].trim();
            return token.startsWith("+") ? token.substring(1) : token;
        }

        return rawValue;
    }

    public String getDetailValue(String label) {
        getPageElement.waitAndCaptureIfNeeded(
                By.xpath("//android.view.ViewGroup[@resource-id='RNE__Overlay']"), 10);
        String uiLabel = getPageElement.mapUiLabel(label);
        String rawValue = getPageElement.readLabelValueFast(uiLabel);
        if (rawValue == null || rawValue.isBlank()) {
            throw new NoSuchElementException("Could not find value on Close Position for label: " + uiLabel);
        }
        return normalizeConfirmationValue(label, rawValue);
    }

    public void confirmPositionClose() {
        abs.waitUntilElementFind(closeBtnConfirmAos);
        closeBtnConfirmAos.click();
    }

    public String getHeaderText() {
        WebElement header = waitForClosePositionHeader();
        if (header == null) {
            return "";
        }
        String text = header.getText();
        if (text == null || text.trim().isEmpty()) {
            text = header.getAttribute("text");
        }
        return text == null ? "" : text;
    }

    private WebElement waitForClosePositionHeader() {
        if (!(driver instanceof AndroidDriver)) {
            return null;
        }
        By overlay = By.xpath("//android.view.ViewGroup[@resource-id=\"RNE__Overlay\"]");
        By header = By.xpath("//*[@text='Close Position']");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.ignoring(StaleElementReferenceException.class);
        wait.until(d -> d.findElements(overlay).stream().noneMatch(el -> {
            try {
                return el.isDisplayed();
            } catch (StaleElementReferenceException e) {
                return false;
            }
        }));
        return wait.until(ExpectedConditions.visibilityOfElementLocated(header));
    }
}
