package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AppClientAgreementPage {

    private final AppiumDriver driver;
    private final MobileAbstractComponents abs;

    public AppClientAgreementPage(AppiumDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.abs = new MobileAbstractComponents(driver);
    }

    @FindBy(xpath = "//android.widget.TextView[@text=\"Client Agreement\"]")
    WebElement clientAgreementTitle;

    public WebElement getClientAgreementTitle() {
        abs.waitUtilElementFind(clientAgreementTitle);
        return clientAgreementTitle;
    }
}
