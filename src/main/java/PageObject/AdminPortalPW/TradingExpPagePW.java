package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.io.IOException;

public class TradingExpPagePW {
    private final Page page;
    private final AbstractComponentsPW abs;
    public final Locator tradeExp;
    public final Locator investExp;
    public final Locator dropdownOption;
    public final Locator buttons;
    public final Locator reasonDropdown;
    public final Locator settlementCurrencyDropdown;
    public final Locator rejectReasonDropdown;
    public final Locator textField;

    public TradingExpPagePW(Page page)
    {
        this.page = page;
        abs = new AbstractComponentsPW(page);
        this.tradeExp = page.locator("#mui-component-select-fiveOrMorTransactionLastThreeYears");
        this.investExp = page.locator("#mui-component-select-haveOtherTrade");
        this.dropdownOption = page.getByRole(AriaRole.OPTION);
        this.buttons = page.getByRole(AriaRole.BUTTON);
        this.reasonDropdown = page.locator("#mui-component-select-verify");
        this.settlementCurrencyDropdown = page.locator("#mui-component-select-settlementCurrency");
        this.rejectReasonDropdown = page.locator("#mui-component-select-reason");
        this.textField = page.locator("input");
    }

    public void fillTradingExp() throws IOException {
        if (abs.userinfoList().get("entity").contains("EBL")){
            selectSettlement();
        }
        selectTradeExp();
        selectInvestExp();
    }

    public void selectTradeExp() throws IOException {
        tradeExp.click();
        dropdownOption.filter(new Locator.FilterOptions().setHas(page.getByText(abs.userinfoList().get("tradeEXP"),
                new Page.GetByTextOptions().setExact(true)))).click();
    }

    public void selectInvestExp() throws IOException {
        investExp.click();
        dropdownOption.filter(new Locator.FilterOptions().setHas(page.getByText(abs.userinfoList().get("investEXP"),
                new Page.GetByTextOptions().setExact(true)))).click();
    }

    public void clickButtonByText(String buttonName) {
        buttons.filter(new Locator.FilterOptions().setHasText(buttonName)).click();
    }

    public void selectReason(String reason)
    {
        reasonDropdown.click();
        dropdownOption.filter(new Locator.FilterOptions().setHasText(reason)).click();
    }

    public void selectRejectReason(String reason){
        rejectReasonDropdown.click();
        dropdownOption.filter(new Locator.FilterOptions().setHasText(reason)).click();
    }

    public void selectSettlement(){
        settlementCurrencyDropdown.click();
        dropdownOption.filter(new Locator.FilterOptions().setHasText("USD")).click();
    }

    public String getFieldValByLabel(String labelName) {
        return abs.getInputValueByAttribute(textField, "name", labelName);
    }

    public String editDropdownVal(String dropdownName){
        if (dropdownName.equalsIgnoreCase("settlementCurrency")){
            settlementCurrencyDropdown.click();
            abs.selectUnselectedDropdownOption();
            return getFieldValByLabel(dropdownName);
        }
        return dropdownName;
    }
}
