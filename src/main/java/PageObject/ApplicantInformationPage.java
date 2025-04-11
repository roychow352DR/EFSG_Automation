package PageObject;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import AbstractComponent.AbstractComponents;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ApplicantInformationPage {
    WebDriver driver;
    AbstractComponents abs;
    PersonalInfoPage personalInfo;
    String email;

    public ApplicantInformationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.abs = new AbstractComponents(driver);
    }

    @FindBy(id = "mui-component-select-entity")
    WebElement entityDropdown;

    @FindBy(xpath = "//ul[@role='listbox']/li")
    List<WebElement> dropdownItems;

    @FindBy(name = "email")
    WebElement emailField;

    @FindBy(name = "mobile")
    WebElement phoneNumberField;

    @FindBy(name = "promoCode")
    WebElement promoCode;

    @FindBy(name = "upperIbAcc")
    WebElement referCode;

    @FindBy(id = "mui-component-select-mobileCountryCode")
    WebElement countryCodeDropdown;

    @FindBy(css = "button[class*='MuiButtonBase-root MuiButton-root MuiButton-contained']")
    WebElement nextButton;

    @FindBy(xpath = "//div[@class='Toastify__toast-body']/div[2]")
    WebElement toast;

    public void clickEntityDropdown() {
        entityDropdown.click();
        abs.staleElementRefExceptionHandle(dropdownItems,"",abs.userinfoList().get("entity"));
    }

    public void fillEmailAddress() {
        email = abs.userinfoList().get("email");
        emailField.sendKeys(email);
    }

    public void selectCountryCode()  {
        countryCodeDropdown.click();
        abs.staleElementRefExceptionHandle(dropdownItems,"",abs.userinfoList().get("countryCode"));
    }

    public void fillPhoneNumber() {
        phoneNumberField.sendKeys(abs.userinfoList().get("phoneNumber"));
    }


    public String submitApplicationInfo() {
        nextButton.click();
        abs.waitUtilElementFind(toast);
        return toast.getText();
    }

    public void fillInApplicantInfo()  {

        clickEntityDropdown();
        fillEmailAddress();
        selectCountryCode();
        fillPhoneNumber();
    }

    public void refill(String type) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        switch (type) {
            case "email":
                emailField.click();
                if (System.getProperty("os.name").contains("Mac")) {
                    emailField.sendKeys(Keys.COMMAND + "a");
                } else if (System.getProperty("os.name").contains("Windows")) {
                    emailField.sendKeys(Keys.CONTROL + "a");
                }
                emailField.sendKeys(Keys.DELETE);
                wait.until(driver -> Objects.requireNonNull(emailField.getDomAttribute("value")).isEmpty());
                fillEmailAddress();

            case "phone":
                phoneNumberField.click();
                if (System.getProperty("os.name").contains("Mac")) {
                    phoneNumberField.sendKeys(Keys.COMMAND + "a");
                } else if (System.getProperty("os.name").contains("Windows")) {
                    phoneNumberField.sendKeys(Keys.CONTROL + "a");
                }
                phoneNumberField.sendKeys(Keys.DELETE);
                wait.until(driver -> Objects.requireNonNull(phoneNumberField.getDomAttribute("value")).isEmpty());
                fillPhoneNumber();
        }

    }

    public PersonalInfoPage createPersonalInfo() {
        personalInfo = new PersonalInfoPage(driver);
        return personalInfo;
    }

    public String retrieveSubmittedEmail() {
        return email;
    }

}
