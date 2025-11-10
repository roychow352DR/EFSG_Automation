package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import org.testng.Assert;

import java.io.IOException;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AORolesPermissionPage {

    private final Page page;
    private final AbstractComponentsPW abs;

    public final Locator rows;
    public final Locator rolesCol;
    public final Locator entityCol;
    public final Locator nextPageBtn;
    public final Locator buttons;
    public final Locator dialogue;
    public final Locator alert;

    public AORolesPermissionPage(Page page) {
        this.page = page;
        this.abs = new AbstractComponentsPW(page);
        this.rows = page.locator("tbody tr");
        this.rolesCol = page.locator(".css-er7ssv:nth-child(2)");
        this.entityCol = page.locator(".css-er7ssv:nth-child(1)");
        this.nextPageBtn = page.locator("button[aria-label='Go to next page']");
        this.buttons = page.getByRole(AriaRole.BUTTON);
        this.dialogue = page.getByRole(AriaRole.DIALOG);
        this.alert = page.locator(".Toastify__toast-body");
    }

    public String getEntityRoleNum(String roleName) throws IOException {
        abs.getItemsByText(roleName, rolesCol, nextPageBtn, abs.userinfoList().get("entity"), entityCol);
        return rows.filter(new Locator.FilterOptions().setHasText(roleName)).filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("entity")))
                .locator(".css-4wwbtj").textContent();
    }

    public boolean getButtonsByText(String buttonText) {
        return abs.checkElementIsEnable(buttons.filter(new Locator.FilterOptions().setHasText(buttonText)));
    }

    public boolean getButtonByEntity(String buttonText) throws IOException {
        return abs.checkElementIsEnable(rows.filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("entity")))
                .getByRole(AriaRole.BUTTON,new Locator.GetByRoleOptions().setName(buttonText)));
    }

    public void clickBtnByText(String buttonText) {
        buttons.filter(new Locator.FilterOptions().setHasText(buttonText)).first().click();
    }

    public Locator getDialogueHeader() {
        return dialogue.filter(new Locator.FilterOptions().setHas(page.getByRole(AriaRole.HEADING))).locator(".css-1lnb07z");
    }

    public void clickButtonByRole(String roleName) throws IOException {
        abs.getItemsByText(roleName, rolesCol, nextPageBtn, abs.userinfoList().get("entity"), entityCol);
        rows.filter(new Locator.FilterOptions().setHasText(roleName)).filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("entity")))
                .locator(buttons).filter(new Locator.FilterOptions().setHasText("Detail")).click();
    }

    public Locator getTextFieldByName(String textFieldName) {
        return page.locator("//input[@name='" + textFieldName + "']");
    }

    public Locator getAlert() {
        abs.waitForLocatorVisible(alert);
        return alert;
    }

    public void uncheckCheckboxByModule(String access, String module){
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Locator checkbox = rows.filter(new Locator.FilterOptions().setHasText(module))
                .locator(".css-er7ssv")
                .nth(getRoleColIndex(access)).locator("input");
        assertThat(checkbox).isChecked();
        checkbox.click();
    }

    public void clickCheckboxByModule(String access, String module){
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Locator checkbox = rows.filter(new Locator.FilterOptions().setHasText(module))
                .locator(".css-er7ssv")
                .nth(getRoleColIndex(access)).locator("input");
        Assert.assertFalse(checkbox.isChecked());
        checkbox.click();
    }

    public int getRoleColIndex(String access){
        return switch(access) {
            case "Write" -> 1;
            case "Create" -> 2;
            case "Export" -> 3;
            case "Delete" -> 4;
            case "Verify" -> 5;
            case "Approve" -> 6;
            case "Reject" -> 7;
            default -> 0;
        };
    }

}
