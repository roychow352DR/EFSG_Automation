package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

public class ApplicationListPagePW {
    private final Page page;
    public final Locator menuTitle;
    public final Locator buttons;
    public final Locator radioButtons;
    public final Locator submitButton;
    public final Locator column;
    public final Locator row;
    public final Locator statusCol;
    private final AbstractComponentsPW abs;
    public Locator email;
    public ApplicationListPagePW(Page page)
    {
        this.page = page;
        abs = new AbstractComponentsPW(page);
        this.menuTitle = page.getByText("Menu");
        this.buttons = page.getByRole(AriaRole.BUTTON);
        this.radioButtons = page.locator("label");
        this.submitButton = page.locator(".css-1s50f5r");
        this.column = page.locator(".css-vh3dxd");
        this.row = page.locator("tbody tr");
        this.statusCol = page.locator("td:nth-child(6)");
    }

    public Locator getMenuText()
    {
        return this.menuTitle;
    }

    public void clickButton(String buttonName){
        buttons.filter(new Locator.FilterOptions().setHasText(buttonName)).click();
    }

    public void clickRadioButton(String radioBtnLabel){
        radioButtons.filter(new Locator.FilterOptions().setHasText(radioBtnLabel)).click();
    }

    public void clickBtnOnCreate(String buttonName){
        submitButton.filter(new Locator.FilterOptions().setHas(page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName(buttonName)))).click();
    }

    public Locator getApplicationStatus(String email){
        Locator applicationStatus = row.filter(new Locator.FilterOptions().setHasText(email)).locator(".css-4soh8v").nth(1);
        abs.waitForLocatorVisible(applicationStatus);
        return applicationStatus;
    }

    public void clickDetailBtn(String applicationStatus)
    {
        email = row.filter(new Locator.FilterOptions().setHasText(applicationStatus)).first().locator(".css-ff6t81").nth(1);
        row.filter(new Locator.FilterOptions().setHasText(applicationStatus)).getByRole(AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Detail")).first().click();
    }

    public Locator getClickDetailEmail(){
        return email;
    }

}
