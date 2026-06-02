package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.List;

public class AppClosePositionPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;

    public AppClosePositionPage(AppiumDriver driver){
        this.driver = driver;
        this.abs = new MobileAbstractComponents(driver);
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


    public String getEditFieldVal(){
        if (driver instanceof AndroidDriver) {
            abs.waitUtilElementVisible(editFieldAos);
            return editFieldAos.getText();
        }
        return "No edit field found";
    }

    public void clickBtn(String btnName){
        if (driver instanceof AndroidDriver) {
            switch (btnName) {
                case "-":
                    abs.waitUtilElementClickable(minusBtnAos);
                    minusBtnAos.click();
                    break;
                case "+":
                    abs.waitUtilElementClickable(plusBtnAos);
                    plusBtnAos.click();
                    break;
                case "All":
                    abs.waitUtilElementClickable(allBtnAos);
                    allBtnAos.click();
                    break;
            }
        }
    }

    public boolean getHeader(){
        if (driver instanceof AndroidDriver) {
            abs.waitUtilElementFind(headerAos);
            return headerAos.isDisplayed();
        }
        return false;
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
                    return text.get(i + 1).getText().split(" ")[0].trim();
                }
                return text.get(i + 1).getText();
            }
        }
        return null;
    }

    public String getFloatingPnL(int contractSize){
        BigDecimal currentPrice = new BigDecimal(getConfirmationValue("Current Price").trim());
        BigDecimal openPrice = new BigDecimal(getConfirmationValue("Open Price").trim());
        BigDecimal lotSize = new BigDecimal(AppInstrumentDetailsPage.lotSize.trim());
        BigDecimal contract = BigDecimal.valueOf(contractSize);

        BigDecimal pnl = currentPrice
                .subtract(openPrice)
                .multiply(lotSize)
                .multiply(contract)
                .setScale(2, RoundingMode.HALF_UP);

        return pnl.toPlainString();

    }
}
