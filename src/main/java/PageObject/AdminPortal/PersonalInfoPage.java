package PageObject.AdminPortal;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import AbstractComponent.AbstractComponents;

import java.util.List;

public class PersonalInfoPage {
    WebDriver driver;
    AbstractComponents abs;
    ContactInfoPage contactInfoPage;

    public PersonalInfoPage(WebDriver driver) {
        this.driver = driver;
        this.abs = new AbstractComponents(driver);
        PageFactory.initElements(driver, this);

    }

    @FindBy(name = "legalLastNameEn")
    WebElement lastName;

    @FindBy(name = "legalFirstAndMiddleNameEn")
    WebElement firstName;

    @FindBy(xpath = "//div[@class='css-1xhj18k']/label/span/input")
    List<WebElement> genderGroup;

    @FindBy(id = "mui-component-select-jurisdictionOfResidence")
    WebElement countryDropdown;

    @FindBy(id = "mui-component-select-jurisdiction")
    WebElement nationalityDropdown;

    @FindBy(xpath = "//ul[@role='listbox']/li")
    List<WebElement> countryList;

    @FindBy(id = "mui-component-select-identificationType")
    WebElement idType;

    @FindBy(css = "ul[class*='css-r8u8y9']>li")
    List<WebElement> idTypeList;

    @FindBy(xpath = "//input[@name='identificationNo']")
    WebElement identificationTextbox;

    @FindBy(xpath = "//button[contains(@class,'css-1m4mrb3-root-contained-root-contained')]")
    WebElement nextButton;

    @FindBy(xpath = "//button[contains(@class,'css-slyssw')]")
    WebElement datePicker;

    @FindBy(xpath = "//button[contains(@class,'css-1wjkg3')]")
    WebElement yearDropdown;

    @FindBy(xpath = "//div[contains(@class,'css-j9zntq')]")
    List<WebElement> yearDropdownItems;

    @FindBy(xpath = "//button[contains(@class,'css-1vcqvsc')]")
    List<WebElement> days;

    @FindBy(css = ".css-wmickx")
    List<WebElement> ctaButtons;

    @FindBy(css = ".css-1wercf4")
    WebElement errorText;

    @FindBy(css = ".css-h3o6is:nth-child(12) .css-slyssw")
    WebElement expiryDate;

    @FindBy(css = ".css-fp6ecq")
    WebElement datePickerArrow;


    public void fillInPersonalInfo(boolean below18) throws InterruptedException {

        fillName();
        selectGender();
        selectCountry();
        selectIdType();
        selectNationality();
        fillIdentification();
        if (!below18) {
            selectDatePicker();
        }
        else {
            selectDateBelow18();
        }
    }

    public void fillName() {
        lastName.sendKeys(abs.userinfoList().get("lastName"));
        firstName.sendKeys(abs.userinfoList().get("firstName"));
    }

    public void selectGender() {
        abs.selectDropdownItemsByAttribute(genderGroup, "value", abs.userinfoList().get("gender"));
    }

    public void selectCountry() throws InterruptedException {
        countryDropdown.click();
        abs.staleElementRefExceptionHandle(countryList, "data-value", abs.userinfoList().get("country"));
    }

    public void selectNationality() throws InterruptedException {
        nationalityDropdown.click();
        abs.staleElementRefExceptionHandle(countryList, "data-value", abs.userinfoList().get("nationality"));

    }

    public void selectIdType() {
        idType.click();
        abs.selectDropdownItemsByAttribute(idTypeList, "data-value", abs.userinfoList().get("idType"));
    }

    public void fillIdentification() {
        identificationTextbox.sendKeys(abs.userinfoList().get("id"));
    }

    public ContactInfoPage submitPersonalInfo() {
        nextButton.click();
        contactInfoPage = new ContactInfoPage(driver);
        return contactInfoPage;
    }

    public void selectDatePicker() {
        datePicker.click();
        yearDropdown.click();
        abs.staleElementRefExceptionHandle(yearDropdownItems, "", abs.userinfoList().get("dateOfBirthYear"));
        abs.selectDropdownItemsByText(days, abs.userinfoList().get("dateOfBirthDay"));

    }

    public ContactInfoPage clickCTA(String buttonName) {
        abs.clickButtonByText(ctaButtons, buttonName);
        contactInfoPage = new ContactInfoPage(driver);
        return contactInfoPage;

    }
    public void selectDateBelow18()
    {
        datePicker.click();
        yearDropdown.click();
        abs.staleElementRefExceptionHandle(yearDropdownItems, "", abs.userinfoList().get("dateOfBirthYearBelow18"));

    }

    public String errorValidation() {
        abs.waitUtilElementFind(errorText);
        return errorText.getText();
    }

    public void selectExpiryDate(int conditionDays) throws InterruptedException {
        expiryDate.click();
        datePickerArrow.click();
        Thread.sleep(2000);
      //  abs.staleElementRefExceptionHandle(yearDropdownItems, "", abs.userinfoList().get("expiryDay"));
        abs.selectDropdownItemsByText(days, String.valueOf(Integer.parseInt(abs.userinfoList().get("expiryDay"))+conditionDays));
    }

}
