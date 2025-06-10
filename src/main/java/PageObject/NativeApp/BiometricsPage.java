package PageObject.NativeApp;

import AbstractComponent.MobileAbstractComponents;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BiometricsPage {
    AppiumDriver driver;
    MobileAbstractComponents abs;
    AppHomePage appHomePage;

    public BiometricsPage(AppiumDriver driver){

        this.driver = driver;
        PageFactory.initElements(driver,this);
        abs = new MobileAbstractComponents(driver);
    }

    @FindBy(xpath = "//android.widget.TextView[@text='Biometrics Login']")
    WebElement biometricLoginHeadingAndroid;

    @FindBy(xpath = "//android.widget.TextView[@text='Skip now and activate later in “Setting”']")
    WebElement skipBiometricAndroid;

    @FindBy(xpath = "//XCUIElementTypeOther[@name='Skip now and activate later in “Setting”']")
    WebElement skipBiometricIos;

    @FindBy(xpath = "(//XCUIElementTypeOther[@name='Biometrics Login'])[6]")
    WebElement biometricLoginHeadingIos;


    public boolean biometricPageValidation()
    {
        if (driver instanceof AndroidDriver) {
            abs.waitUtilElementFind(skipBiometricAndroid);
            return skipBiometricAndroid.isEnabled();
        }
        else {
            abs.waitUtilElementFind(skipBiometricIos);
            return skipBiometricIos.isDisplayed();
        }
    }

    public AppHomePage skipBiometric()
    {
        if (driver instanceof AndroidDriver) {
            skipBiometricAndroid.click();
        }
        else {
            skipBiometricIos.click();
        }
        return new AppHomePage(driver);

    }
}
