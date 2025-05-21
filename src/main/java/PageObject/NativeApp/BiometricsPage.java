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
    WebElement biometricLoginHeading;

    @FindBy(xpath = "//android.widget.TextView[@text='Skip now and activate later in “Setting”']")
    WebElement skipBiometric;


    public boolean biometricPageValidation()
    {
        abs.waitUtilElementFind(biometricLoginHeading);
        return biometricLoginHeading.isDisplayed();
    }

    public AppHomePage skipBiometric()
    {
        skipBiometric.click();
        return new AppHomePage(driver);

    }
}
