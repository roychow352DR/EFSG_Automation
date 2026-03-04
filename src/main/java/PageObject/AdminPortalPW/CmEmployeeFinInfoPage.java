package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.io.IOException;

public class CmEmployeeFinInfoPage {

    private final Page page;
    private final AbstractComponentsPW abs;
    public final Locator employeeStatus;
    public final Locator annualIncome;
    public final Locator liquidNetWorth;
    public final Locator sourceOfFunds;
    public final Locator taxJurisdiction;
    public final Locator dropdownOption;
    public final Locator industrial;
    public final Locator nextBtn;
    public final Locator buttons;
    public final Locator dropdownField;
    public final Locator checkbox;

    public CmEmployeeFinInfoPage(Page page) {
        this.page = page;
        abs = new AbstractComponentsPW(page);
        this.employeeStatus = page.locator("#mui-component-select-employmentStatus");
        this.annualIncome = page.locator("#mui-component-select-annualIncome");
        this.liquidNetWorth = page.locator("#mui-component-select-liquidNetworth");
        this.sourceOfFunds = page.locator("#mui-component-select-sourceOfFunds");
        this.taxJurisdiction = page.locator("#mui-component-select-jurisdictionOfTaxResidence");
        this.dropdownOption = page.getByRole(AriaRole.OPTION);
        this.industrial = page.locator("#mui-component-select-industrial");
        this.nextBtn = page.getByRole(AriaRole.BUTTON);
        this.buttons = page.getByRole(AriaRole.BUTTON);
        this.dropdownField = page.locator(".css-1tz4v7m div");
        this.checkbox = page.locator("input[type='checkbox']");
    }

    public void fillEmployeeFinInfo() throws IOException {
        selectEmployeeStatus();
        selectIndustrial();
        selectAnnualIncome();
        selectliquidNetWorth();
        selectSourceOfFunds();
        selectTaxJurisdiction();
        clickNext();
    }

    public void selectEmployeeStatus() throws IOException {
        employeeStatus.click();
        dropdownOption.filter(new Locator.FilterOptions().setHas(page.getByText(abs.userinfoList().get("employStatus"),
                new Page.GetByTextOptions().setExact(true)))).click();

    }

    public void selectIndustrial() throws IOException {
        industrial.click();
        dropdownOption.filter(new Locator.FilterOptions().setHas(page.getByText(abs.userinfoList().get("industrial"),
                new Page.GetByTextOptions().setExact(true)))).click();
    }

    public void selectAnnualIncome() throws IOException {
        annualIncome.click();
        dropdownOption.filter(new Locator.FilterOptions().setHas(page.getByText(abs.userinfoList().get("annualIncome"),
                new Page.GetByTextOptions().setExact(true)))).click();

    }

    public void selectliquidNetWorth() throws IOException {
        liquidNetWorth.click();
        dropdownOption.filter(new Locator.FilterOptions().setHas(page.getByText(abs.userinfoList().get("netWorth"),
                new Page.GetByTextOptions().setExact(true)))).click();

    }

    public void selectSourceOfFunds() throws IOException {
        sourceOfFunds.click();
        dropdownOption.filter(new Locator.FilterOptions().setHas(page.getByText(abs.userinfoList().get("tradeFunds"),
                new Page.GetByTextOptions().setExact(true)))).click();

    }

    public void selectTaxJurisdiction() throws IOException {
        taxJurisdiction.click();
        dropdownOption.filter(new Locator.FilterOptions().setHas(page.getByText(abs.userinfoList().get("taxCountry"),
                new Page.GetByTextOptions().setExact(true)))).click();

    }

    public void clickNext() {
        nextBtn.filter(new Locator.FilterOptions().setHasText("Next To Trading Experience")).click();
    }

    public void clickButtonByText(String buttonText) {
        buttons.filter(new Locator.FilterOptions().setHasText(buttonText)).click();
    }

    public boolean checkElementIsClickable() {
        return abs.checkElementIsEnable(dropdownField) &&
                abs.checkElementIsEnable(checkbox);
    }
}
