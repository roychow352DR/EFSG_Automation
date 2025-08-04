package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class CustomerManagementPage {
    private final Page page;
    private final AbstractComponentsPW abs;
    public final Locator rows;
    public final Locator statusCol;
    public final Locator nextPageBtn;
    public final Locator cmEmail;
    public String email;
    public Locator cmStatus;

    public CustomerManagementPage(Page page){
        this.page = page;
        abs = new AbstractComponentsPW(page);
        this.rows = page.locator("tbody tr");
        this.statusCol = page.locator(".css-9iedg7");
        this.nextPageBtn = page.locator("button[aria-label='Go to next page']");
        this.cmEmail = page.locator(".css-10morg3");
    }

    public Locator getStatusRow(String status, String l3Email){
        abs.getItemsByText(l3Email,cmEmail,nextPageBtn);
        email = rows.filter(new Locator.FilterOptions().setHasText(status)).first().locator(cmEmail).textContent();
        cmStatus = rows.filter(new Locator.FilterOptions().setHasText(email)).first().locator(statusCol);
        return cmStatus;
    }
}
