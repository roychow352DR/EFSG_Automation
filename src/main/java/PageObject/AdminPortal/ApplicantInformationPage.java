package PageObject.AdminPortal;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import AbstractComponent.AbstractComponents;

import java.util.List;

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
    WebElement promoCodeField;

    @FindBy(name = "upperIbAcc")
    WebElement referCodeField;

    @FindBy(id = "mui-component-select-mobileCountryCode")
    WebElement countryCodeDropdown;

    @FindBy(css = "button[class*='MuiButtonBase-root MuiButton-root MuiButton-contained']")
    WebElement nextButton;

    @FindBy(xpath = "//div[@class='Toastify__toast-body']/div[2]")
    WebElement toast;

    @FindBy(css = "span[class*='css-1wercf4']")
    WebElement errorText;

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

        switch (type) {
            case "email":
                abs.clearField(emailField);
                fillEmailAddress();

            case "phone":
                abs.clearField(phoneNumberField);
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

    public PersonalInfoPage clickNext()
    {
        nextButton.click();
        personalInfo = new PersonalInfoPage(driver);
        return personalInfo;
    }

    public void fillCode(String type,String code)
    {
        if (type.equalsIgnoreCase("promo"))
        {
            promoCodeField.sendKeys(code);
        }
        else if (type.equalsIgnoreCase("referral"))
        {
            referCodeField.sendKeys(code);
        }
    }

    public String errorValidation()
    {
        abs.waitUtilElementFind(errorText);
        return errorText.getText();
    }

    public void fillExistingInfo(String info)
    {
        if (!info.contains(".com")) {
            abs.clearField(phoneNumberField);
            phoneNumberField.sendKeys(info);
        }
        else
        {
            abs.clearField(emailField);
            emailField.sendKeys(info);
        }
    }

}
