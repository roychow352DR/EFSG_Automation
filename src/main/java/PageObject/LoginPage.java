package PageObject;

import AbstractComponent.AbstractComponents;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    WebDriver driver;
    public LoginPage(WebDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver,this);
    }

    @FindBy (id = ":Rijakqdal6:")
    WebElement userName;

    @FindBy (id = ":Rilakqdal6:")
    WebElement userPassword;

    @FindBy (css = "button[class*='MuiButtonBase-root MuiButton-root']")
    WebElement signInButton;

    @FindBy (xpath = "//div[text()='Invalid username or password.']")
    WebElement loginErrorText;


    public void loginApplication(String username, String password)
    {
        userName.sendKeys(username);
        userPassword.sendKeys(password);
       // signInButton.click();
    }

    public void landOnLoginPage()
    {
        driver.get("https://d3lyp6p86bdjbb.cloudfront.net/login");
    }

    public String errorValidation()
    {
        AbstractComponents abs = new AbstractComponents(driver);
        abs.waitUtilElementFind(loginErrorText);
        return loginErrorText.getText();
    }

    public void clickSignIn()
    {
        signInButton.click();
    }

    public boolean ctaButtonStatus()
    {
        AbstractComponents abs = new AbstractComponents(driver);
        abs.waitUtilElementClickable(signInButton);
        return signInButton.isEnabled();
    }

    public WebElement ctaButton()
    {
        return signInButton;
    }

}
