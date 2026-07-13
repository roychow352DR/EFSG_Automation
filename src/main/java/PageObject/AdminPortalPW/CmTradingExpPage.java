package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.io.IOException;

public class CmTradingExpPage {
    private final Page page;
    private final AbstractComponentsPW abs;
    public final Locator tradeExp;
    public final Locator investExp;
    public final Locator dropdownOption;
    public final Locator buttons;
    public final Locator reasonField;
    public final Locator toastMsg;
    public final Locator settlementCurrencyDropdown;
    public final Locator textField;
    public final Locator dialogue;
    public final Locator closeBtn;

    public CmTradingExpPage(Page page) {
        this.page = page;
        abs = new AbstractComponentsPW(page);
        this.tradeExp = page.locator("#mui-component-select-fiveOrMorTransactionLastThreeYears");
        this.investExp = page.locator("#mui-component-select-haveOtherTrade");
        this.dropdownOption = page.getByRole(AriaRole.OPTION);
        this.buttons = page.getByRole(AriaRole.BUTTON);
        this.reasonField = page.locator("input[name='reason']");
        this.toastMsg = page.locator(".Toastify__toast-body div").nth(1);
        this.settlementCurrencyDropdown = page.locator("#mui-component-select-settlementCurrency");
        this.textField = page.locator("input");
        this.dialogue = page.locator(".Toastify__toast-body");
        this.closeBtn = page.locator(".css-1yxmbwk");
    }

    public void fillTradingExp() throws IOException {
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

    public void fillReason(String reason) {
        reasonField.fill(reason);
    }

    public Locator getDialogueText() {
        return toastMsg;
    }


    public void selectSettlement() {
        settlementCurrencyDropdown.click();
        dropdownOption.filter(new Locator.FilterOptions().setHasText("USD")).click();
    }

    public String getFieldValByLabel(String labelName) {
        return abs.getInputValueByAttribute(textField, "name", labelName);
    }

    public String editDropdownVal(String dropdownName) {
        if (dropdownName.equalsIgnoreCase("settlementCurrency")) {
            settlementCurrencyDropdown.click();
            abs.selectUnselectedDropdownOption();
            return getFieldValByLabel(dropdownName);
        }
        else {
            return "Empty Input Value return";
        }
    }

    public Locator getDropdown(String fieldName) {
        return page.locator("input[name='" + fieldName + "']");
    }

    public Locator getDialogue() {
        abs.waitForLocatorVisible(dialogue);
        return dialogue;
    }

    public void clickCloseBtn(){
        abs.waitForLocatorVisible(closeBtn);
        closeBtn.click();
    }
}
