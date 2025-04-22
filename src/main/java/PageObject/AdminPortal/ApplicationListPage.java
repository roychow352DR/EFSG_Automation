package PageObject.AdminPortal;

import AbstractComponent.AbstractComponents;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class ApplicationListPage {

    WebDriver driver;
    public ApplicantInformationPage applicantInformationPage;
    AbstractComponents abs;
    public String email;

    public ApplicationListPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.abs = new AbstractComponents(driver);
    }

    @FindBy(css = "button[class*='MuiButtonBase-root MuiButton-root MuiButton-outlined']")
    List<WebElement> buttons;

    @FindBy(xpath = "//h4[text()='Menu']")
    WebElement menu;

    @FindBy(css = ".css-1lnb07z")
    WebElement createAccountPromptTitle;

    @FindBy(xpath = "//span[text()='Submit']")
    WebElement submitButton;

    @FindBy(css = "span[class*='css-1m7w4ao']>input[value='Company']")
    WebElement companyRadio;

    @FindBy(css = "span[class*='css-1m7w4ao']>input[value='Individual']")
    WebElement individualRadio;

    @FindBy(css = ".css-1xhj18k")
    List<WebElement> label;

    @FindBy(xpath = "//span[text()='Company']")
    WebElement companyRadioLabel;

    @FindBy(xpath = "//span[text()='Individual']")
    WebElement individualRadioLabel;

    @FindBy(css = ".MuiTableRow-root.css-erkyuf")
    List<WebElement> applicationRecords;

    @FindBy(xpath = "//tr[position()=1]/td[2]")
    WebElement emailValidation;

    @FindBy(xpath = "//tr[position()=1]/td[6]/div")
    WebElement statusValidation;


    public void clickButton(String clickButtonName) {
        for (WebElement button : buttons) {
            String buttonName = button.getText();
            if (buttonName.equalsIgnoreCase(clickButtonName)) {
                button.click();
            }
        }
    }

    public WebElement menuTitle() {
        return menu;
    }

    public String createAccountPromptValidation() {
        AbstractComponents abs = new AbstractComponents(driver);
        abs.waitUtilElementFind(createAccountPromptTitle);
        return createAccountPromptTitle.getText();
    }

    public void clickRadioButton(String string) {
        if (string.equalsIgnoreCase("Individual")) {
            individualRadio.click();
        } else if (string.equalsIgnoreCase("Company")) {
            companyRadio.click();
        }
    }

    public String getRadioLabel(String string) {
        if (string.equalsIgnoreCase("Company")) {
            return companyRadioLabel.getText();
        } else if (string.equalsIgnoreCase("Individual")) {
            return individualRadioLabel.getText();
        }
        return string;
    }

    public void clickSubmitButton() {
        submitButton.click();
    }

    public String getNewRecordEmail() throws InterruptedException {
        return emailValidation.getText();
    }

    public String getNewRecordStatus() {
        return statusValidation.getText();
    }

    public ApplicantInformationPage clickActionButtonBasedOnStatus(String status) {
        WebElement rec = abs.findRecordBasedOnElement(applicationRecords, status, By.cssSelector("span"));
        email = rec.findElement(By.cssSelector("td:nth-child(2)")).getText();
        rec.findElement(By.cssSelector("td:nth-child(8)")).click();
        applicantInformationPage = new ApplicantInformationPage(driver);
        return applicantInformationPage;
    }

    public String getStatusChangeEmail()
    {
       return email;
    }

    public String getEmailStatus()
    {
        abs.waitUtilElementFind(menu);
        return abs.findRecordBasedOnElement(applicationRecords,getStatusChangeEmail(),By.cssSelector("td:nth-child(2)")).findElement(By.cssSelector("td:nth-child(6)")).getText();

    }


}
