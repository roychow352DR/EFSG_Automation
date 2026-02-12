package PageObject.MIOadmin;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class DepositManagementPage {

    private final Page page;
    private final AbstractComponentsPW abs;

    public Locator dropdown;
    public Locator dropdownOptions;

    public DepositManagementPage(Page page){
        this.page = page;
        this.abs = new AbstractComponentsPW(page);
        this.dropdown = page.locator(".css-1821gv5");
        this.dropdownOptions = page.getByRole(AriaRole.OPTION);
    }

    public void clickDropdown(String dropdownName){
        dropdown.filter(new Locator.FilterOptions().setHasText(dropdownName)).locator(".css-uge3vf").first().click();
        dropdownOptions.filter(new Locator.FilterOptions().setHas(page.getByText("Deposit",new Page.GetByTextOptions().setExact(true)))).click();
        page.pause();
    }

}
