package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.io.IOException;

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
    public final Locator clientType;
    public final Locator entityRow;
    public final Locator dialogue;
    public String entity;

    public CustomerManagementPage(Page page) throws IOException {
        this.page = page;
        abs = new AbstractComponentsPW(page);
        this.rows = page.locator("tbody tr");
        this.statusCol = page.locator(".css-9iedg7");
        this.nextPageBtn = page.locator("button[aria-label='Go to next page']");
        this.cmEmail = page.locator(".css-10morg3");
        this.buttons = page.getByRole(AriaRole.BUTTON);
        this.status = page.locator(".css-9iedg7");
        this.clientType = page.locator(".css-ff6t81:nth-child(1)");
        this.entityRow = page.locator(".css-ff6t81:nth-child(2)");
        this.dialogue = page.locator(".Toastify__toast-body");
        this.entity = abs.userinfoList().get("entity");

    }

    public Locator getStatusRow(String statusText, String l3Email) {
        Locator cmStatus;
        getEmail(statusText, l3Email, cmEmail, cmEmail);
        cmStatus = rows.filter(new Locator.FilterOptions().setHasText(email)).first().locator(statusCol);
        return cmStatus;
    }

    public void getStatusEmail(String cmStatusText,String clientTypeText) throws IOException {
        getEmail(cmStatusText, cmStatusText, status, cmEmail, clientTypeText);
    }

    public void clickDetailBtn(String cmStatus, String clientTypeText) throws IOException {
        getEmail(cmStatus, cmStatus, status, cmEmail, clientTypeText);
        rows.filter(new Locator.FilterOptions().setHasText(email)).getByRole(AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Detail")).first().click();
    }

    public void clickDetailBtn() throws IOException {
        email = getEntityEmail(entity);
        abs.getItemsByText(email, cmEmail, nextPageBtn,entity,entityRow);
        rows.filter(new Locator.FilterOptions().setHasText(email)).getByRole(AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Detail")).first().click();
    }

//    public void clickDetailBtn(String entity){
//        email = getEntityEmail(entity);
//        abs.getItemsByText(this.email, cmEmail, nextPageBtn);
//        rows.filter(new Locator.FilterOptions().setHasText(this.email)).getByRole(AriaRole.BUTTON,
//                new Locator.GetByRoleOptions().setName("Detail")).first().click();
//    }

    public void getEmail(String statusText, String filterText, Locator getItemlocator, Locator filterLocator, String clientTypeText) throws IOException {
        abs.getItemsByText(filterText, clientTypeText, entity, getItemlocator, clientType, entityRow, nextPageBtn);
        email = rows.filter(new Locator.FilterOptions().setHasText(statusText)).filter(new Locator.FilterOptions().setHasText(clientTypeText)).
                filter(new Locator.FilterOptions().setHasText(entity)).first().locator(filterLocator).textContent();
    }

    public void getEmail(String statusText, String filterText, Locator getItemlocator, Locator filterLocator) {
        abs.getItemsByText(filterText, getItemlocator, nextPageBtn,entity,entityRow);
        email = rows.filter(new Locator.FilterOptions().setHasText(statusText)).first().locator(filterLocator).textContent();
    }

    public String getEntityEmail(String entity){
        return switch(entity){
            case "EIEHK" -> "qaeiecmtest@yopmail.com";
            case "XPRO" -> "qaxprocmtest@yopmail.com";
            case "EBL_MT5" -> "uatapproved@yopmail.com";
            //"qaeblcmtest@yopmail.com";
            default -> null;
        };
    }

    public Locator getDialogue(){
        abs.waitForLocatorVisible(dialogue);
        return dialogue;
    }


}
