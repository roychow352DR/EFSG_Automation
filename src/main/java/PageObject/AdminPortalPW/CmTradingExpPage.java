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
    public CmTradingExpPage(Page page)
    {
        this.page = page;
        abs = new AbstractComponentsPW(page);
        tradeExp = page.locator("#mui-component-select-fiveOrMorTransactionLastThreeYears");
        investExp = page.locator("#mui-component-select-haveOtherTrade");
        dropdownOption = page.getByRole(AriaRole.OPTION);
        buttons = page.getByRole(AriaRole.BUTTON);
        reasonField = page.locator("input[name='reason']");
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

    public void fillReason(String reason)
    {
        reasonField.fill(reason);
    }
}
