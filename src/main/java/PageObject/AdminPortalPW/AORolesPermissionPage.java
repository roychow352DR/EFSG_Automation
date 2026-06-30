package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.openqa.selenium.WebElement;
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
    public final Locator roleNameField;
    public final Locator checkbox;
    public final Locator errorText;
    public final Locator crossBtn;

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
        this.roleNameField = page.locator("//input[@name='roleName']");
        this.checkbox = page.locator(".css-1jaw3da");
        this.errorText = page.locator(".css-1wercf4");
        this.crossBtn = page.locator(".css-1yxmbwk");
    }

    public String getEntityRoleNum(String roleName) throws IOException {
        abs.getItemsByText(roleName, rolesCol, nextPageBtn, abs.userinfoList().get("entity"), entityCol);
        return rows.filter(new Locator.FilterOptions().setHasText(roleName)).filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("entity")))
                .locator(".css-4wwbtj").textContent();
    }

    public boolean getButtonIsEnable(String buttonText) {
        return abs.checkElementIsEnable(buttons.filter(new Locator.FilterOptions().setHasText(buttonText)));
    }

    public boolean getButtonByEntity(String buttonText) throws IOException {
        abs.waitForLocatorVisible(rows.first());
        return abs.checkElementIsEnable(rows.filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("entity")))
                .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName(buttonText)));
    }

    public void clickBtnByText(String buttonText) {
        Locator locator = buttons.filter(new Locator.FilterOptions().setHasText(buttonText)).last();
        abs.waitForLocatorVisible(locator);
  //      buttons.filter(new Locator.FilterOptions().setHasText(buttonText)).last().click();
        locator.click();
    }

    public Locator getDialogueHeader() {
        abs.waitForLocatorVisible(dialogue);
        return dialogue.filter(new Locator.FilterOptions().setHas(page.getByRole(AriaRole.HEADING))).locator(".css-1lnb07z");
    }

    public void clickButtonByRole(String roleName) throws IOException {
        abs.getItemsByText(roleName, rolesCol, nextPageBtn, abs.userinfoList().get("entity"), entityCol);
        rows.filter(new Locator.FilterOptions().setHas(page.getByText(roleName, new Page.GetByTextOptions().setExact(true))))
                .filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("entity")))
                .locator(buttons).filter(new Locator.FilterOptions().setHasText("Detail")).click();
    }

    public Locator getTextFieldByName(String textFieldName) {
        return page.locator("//input[@name='" + textFieldName + "']");
    }

    public Locator getAlert() {
        abs.waitForLocatorVisible(alert);
        return alert;
    }

    public void uncheckCheckboxByModule(String access, String module) {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Locator checkbox = rows.filter(new Locator.FilterOptions().setHasText(module))
                .locator(".css-er7ssv")
                .nth(getRoleColIndex(access)).locator("input");
        assertThat(checkbox).isChecked();
        checkbox.click();
    }

    public void clickCheckboxByModule(String access, String module) {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Locator checkbox = rows.filter(new Locator.FilterOptions().setHasText(module))
                .locator(".css-er7ssv")
                .nth(getRoleColIndex(access)).locator("input");
        Assert.assertFalse(checkbox.isChecked());
        checkbox.click();
    }

    public int getRoleColIndex(String access) {
        return switch (access) {
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

    public String fillAddRoleForm() throws IOException {
        String roleName = abs.userinfoList().get("roleName");
        roleNameField.fill(roleName);
        checkEntityBox();
        return roleName;
    }

    public int getIndex() throws IOException {
        return switch (abs.userinfoList().get("entity")) {
            case "XPro" -> 0;
            case "EGM" -> 1;
            case "EIEHK" -> 2;
            case "EBL_MT5" -> 3;
            default -> 0;
        };
    }

    public Locator getRoleName() {
        abs.waitForLocatorVisible(rows.first());
        return rolesCol.first();
    }

    public Locator getRoleOnList() throws IOException {
        abs.waitForLocatorVisible(rolesCol.first());
        return rows.filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("roleName"))).filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("entity")));
    }

    public void checkEntityBox() throws IOException {
        checkbox.nth(getIndex()).click();
    }

    public Locator getErrorText() {
        return errorText;
    }

    public Locator getButton(String buttonText) {
        return buttons.filter(new Locator.FilterOptions().setHasText(buttonText));
    }

    public void clickCrossBtn() {
        crossBtn.click();
    }

}
