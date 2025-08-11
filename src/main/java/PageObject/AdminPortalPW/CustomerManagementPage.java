package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CustomerManagementPage {
    private final Page page;
    private final AbstractComponentsPW abs;
    public final Locator rows;
    public final Locator statusCol;
    public final Locator nextPageBtn;
    public final Locator cmEmail;
    public String email;
    public final Locator buttons;
    public final Locator status;

    public CustomerManagementPage(Page page){
        this.page = page;
        abs = new AbstractComponentsPW(page);
        this.rows = page.locator("tbody tr");
        this.statusCol = page.locator(".css-9iedg7");
        this.nextPageBtn = page.locator("button[aria-label='Go to next page']");
        this.cmEmail = page.locator(".css-10morg3");
        this.buttons = page.getByRole(AriaRole.BUTTON);
        this.status = page.locator(".css-9iedg7");
    }

    public Locator getStatusRow(String statusText, String l3Email){
        Locator cmStatus;
        getEmail(statusText,l3Email,cmEmail,cmEmail);
        cmStatus = rows.filter(new Locator.FilterOptions().setHasText(email)).first().locator(statusCol);
        return cmStatus;
    }

    public void clickDetailBtn(String cmStatus){
        getEmail(cmStatus,cmStatus,status,cmEmail);
        rows.filter(new Locator.FilterOptions().setHasText(cmStatus)).getByRole(AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Detail")).first().click();
    }

    public void getEmail(String statusText, String filterText, Locator getItemlocator,Locator filterLocator){
        abs.getItemsByText(filterText,getItemlocator,nextPageBtn);
        email = rows.filter(new Locator.FilterOptions().setHasText(statusText)).first().locator(filterLocator).textContent();
    }
}
