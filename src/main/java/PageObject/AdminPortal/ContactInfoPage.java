package PageObject.AdminPortal;

import AbstractComponent.AbstractComponents;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class ContactInfoPage {
    WebDriver driver;
    AbstractComponents abs;
    EmployeeFinancialPage employeeFinance;
    public ContactInfoPage(WebDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver,this);
        abs = new AbstractComponents(driver);
    }


    @FindBy (name = "addressLine1")
    WebElement addressLine1;

    @FindBy (name = "city")
    WebElement city;

    @FindBy (xpath = "//button[contains(@class,'css-1m4mrb3-root-contained-root-contained')]")
    WebElement nextButton;

    @FindBy(css = ".css-wmickx")
    List<WebElement> ctaButtons;

    public void fillContactInformation()
    {
        addressLine1.sendKeys(abs.userinfoList().get("addressLine1"));
        city.sendKeys(abs.userinfoList().get("city"));
    }

    public EmployeeFinancialPage submitContactInfo()
    {
        nextButton.click();
        employeeFinance = new EmployeeFinancialPage(driver);
        return employeeFinance;
    }

    public void clickCTA(String buttonName) {
        abs.clickButtonByText(ctaButtons,buttonName);
    }
}
