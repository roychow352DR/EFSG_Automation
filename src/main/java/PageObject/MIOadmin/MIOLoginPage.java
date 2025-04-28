package PageObject.MIOadmin;

import AbstractComponent.AbstractComponents;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MIOLoginPage {
    WebDriver driver;
    public AbstractComponents abs;
    public DashboardPage dashboardPage;

    public MIOLoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        abs = new AbstractComponents(driver);
    }

    @FindBy (name = "username")
    WebElement usernameField;

    @FindBy (name = "password")
    WebElement passwordField;

    @FindBy (xpath = "//button[contains(@class,'css-1hjma9j')]")
    WebElement signInButton;


    public void fillCredential(String username,String password)
    {
        usernameField.click();
        usernameField.sendKeys(username);
        passwordField.click();
        passwordField.sendKeys(password);
    }

    public DashboardPage clickSignIn()
    {
        signInButton.click();
        return new DashboardPage(driver);

    }

    public boolean signInButtonStatus()
    {
        abs.waitUtilElementClickable(signInButton);
        return signInButton.isEnabled();
    }
}
