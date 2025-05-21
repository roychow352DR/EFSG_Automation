package PageObject.NativeApp;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

public class AppLoginPage {
    AppiumDriver driver;
    BiometricsPage biometricsPage;

    public AppLoginPage(AppiumDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[5]")
    WebElement loginButton;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[1]/android.widget.EditText")
    WebElement emailField;

    @FindBy(xpath = "//android.widget.ScrollView/android.view.ViewGroup/android.view.ViewGroup/android.view.ViewGroup[2]/android.widget.EditText")
    WebElement passwordField;

    public BiometricsPage clickLogin()
    {
        loginButton.click();
        biometricsPage = new BiometricsPage(driver);
        return biometricsPage;
    }

    public void fillCredential(String email,String password)
    {
        emailField.sendKeys(email);
        passwordField.sendKeys(password);
    }


}
