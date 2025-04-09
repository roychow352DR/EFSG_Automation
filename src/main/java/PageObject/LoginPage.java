package PageObject;

import AbstractComponent.AbstractComponents;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    WebDriver driver;
    public ApplicationPage applicationPage;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = ":Rijakqdal6:")
    WebElement userName;

    @FindBy(id = ":Rilakqdal6:")
    WebElement userPassword;

    @FindBy(css = "button[class*='MuiButtonBase-root MuiButton-root']")
    WebElement signInButton;

    @FindBy(xpath = "//div[text()='Invalid username or password.']")
    WebElement loginErrorText;

    @FindBy(xpath = "//div[text()='User account is suspended! Please contact administration']")
    WebElement suspendText;

    public void loginApplication(String username, String password) throws InterruptedException {
        userName.click();
        userName.sendKeys(username);
        userPassword.click();
        userPassword.sendKeys(password);
    }

    public String loginErrorValidation() {
        AbstractComponents abs = new AbstractComponents(driver);
        abs.waitUtilElementFind(loginErrorText);
        return loginErrorText.getText();
    }

    public String suspendErrorValidation() {
        AbstractComponents abs = new AbstractComponents(driver);
        abs.waitUtilElementFind(suspendText);
        return suspendText.getText();
    }

    public ApplicationPage clickSignIn() {
        signInButton.click();
        applicationPage = new ApplicationPage(driver);
        return applicationPage;
    }

    public boolean ctaButtonStatus() {
        AbstractComponents abs = new AbstractComponents(driver);
        abs.waitUtilElementClickable(signInButton);
        return signInButton.isEnabled();
    }

    public WebElement ctaButton() {
        return signInButton;
    }

}
