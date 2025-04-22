package PageObject.AdminPortal;

import AbstractComponent.AbstractComponents;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class EmployeeFinancialPage {
    WebDriver driver;
    AbstractComponents abs;
    TradingExperiencePage tradingExperiencePage;

    public EmployeeFinancialPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        abs = new AbstractComponents(driver);
    }

    @FindBy(id = "mui-component-select-employmentStatus")
    WebElement employeeStatusDropdown;

    @FindBy(xpath = "//li[contains(@class,'css-sudvrv')]")
    List<WebElement> dropdownItems;

    @FindBy(id = "mui-component-select-industrial")
    WebElement industrial;

    @FindBy(id = "mui-component-select-annualIncome")
    WebElement annualIncome;

    @FindBy(id = "mui-component-select-liquidNetworth")
    WebElement liquidNetWorth;

    @FindBy(id = "mui-component-select-sourceOfFunds")
    WebElement sourceOfFunds;

    @FindBy(id = "mui-component-select-jurisdictionOfTaxResidence")
    WebElement taxCountry;

    @FindBy(xpath = "//button[contains(@class,'css-1m4mrb3-root-contained-root-contained')]")
    WebElement nextButton;

    @FindBy(css = ".css-wmickx")
    List<WebElement> ctaButtons;

    public void fillEmployeeFinancial() {
        selectEmployeeStatus();
        selectIndustrial();
        selectEmployeeStatus();
        selectAnnualIncome();
        selectLiquidNetWorth();
        selectSourceOfFunds();
        selectTaxCountry();
    }


    public void selectEmployeeStatus() {
        employeeStatusDropdown.click();
        abs.staleElementRefExceptionHandle(dropdownItems, "data-value", abs.userinfoList().get("employStatus"));

    }

    public void selectIndustrial() {
        industrial.click();
        abs.staleElementRefExceptionHandle(dropdownItems, "data-value", abs.userinfoList().get("industrial"));
    }

    public void selectAnnualIncome() {
        annualIncome.click();
        abs.staleElementRefExceptionHandle(dropdownItems, "data-value", abs.userinfoList().get("annualIncome"));
    }

    public void selectLiquidNetWorth() {
        liquidNetWorth.click();
        abs.staleElementRefExceptionHandle(dropdownItems, "data-value", abs.userinfoList().get("netWorth"));
    }

    public void selectSourceOfFunds() {
        sourceOfFunds.click();
        abs.staleElementRefExceptionHandle(dropdownItems, "data-value", abs.userinfoList().get("tradeFunds"));
    }

    public void selectTaxCountry() {
        taxCountry.click();
        abs.staleElementRefExceptionHandle(dropdownItems, "data-value", abs.userinfoList().get("taxCountry"));
    }

    public TradingExperiencePage submitEmployeeFinancial() {
        nextButton.click();
        tradingExperiencePage = new TradingExperiencePage(driver);
        return tradingExperiencePage;
    }

    public void clickCTA(String buttonName) {
        abs.clickButtonByText(ctaButtons,buttonName);
    }

}
