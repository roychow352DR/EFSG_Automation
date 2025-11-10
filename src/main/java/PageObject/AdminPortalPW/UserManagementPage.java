package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.io.IOException;

public class UserManagementPage {

    private final Page page;
    private final AbstractComponentsPW abs;

    public final Locator buttons;
    public final Locator rows;
    public final Locator usernameCol;
    public final Locator nextPageBtn;
    public final Locator statusDropdown;
    public final Locator dropdownOption;
    public final Locator entityField;
    public final Locator entityRoleDropdown;
    public final Locator inputField;
    public final Locator roleField;


    public UserManagementPage(Page page) {
        this.page = page;
        this.abs = new AbstractComponentsPW(page);
        this.buttons = page.getByRole(AriaRole.BUTTON);
        this.rows = page.locator("tbody tr");
        this.usernameCol = page.locator(".css-cbf8jv:nth-child(2)");
        this.nextPageBtn = page.locator("button[aria-label='Go to next page']");
        this.statusDropdown = page.locator("#mui-component-select-status");
        this.dropdownOption = page.getByRole(AriaRole.OPTION);
        this.entityField = page.locator(".css-11rrq3l .css-1x5jdmq");
        this.entityRoleDropdown = page.locator("#mui-component-select-entity_role");
        this.inputField = page.locator("input");
        this.roleField = page.locator(".css-4462pi");

    }

    public Locator getButton(String buttonText) {
        return buttons.filter(new Locator.FilterOptions().setHasText(buttonText));
    }

    public void clickDetailByUsername(String username) {
        abs.getItemsByText(username, usernameCol, nextPageBtn);
        rows.filter(new Locator.FilterOptions().setHasText(username)).getByRole(AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Detail")).first().click();
    }

    public void editDropdown(String dropdownName, String option) {
        if (dropdownName.equalsIgnoreCase("status")) {
            statusDropdown.click();
        }
        dropdownOption.filter(new Locator.FilterOptions().setHas(page.getByText(option, new Page.GetByTextOptions().setExact(true)))).click();
    }

    public void clickBtnByText(String buttonText) {
        buttons.filter(new Locator.FilterOptions().setHasText(buttonText)).click();
    }

    public Locator getDropdownVal(String dropdownFieldName) {
        if (dropdownFieldName.equalsIgnoreCase("status")) {
            return page.locator("#mui-component-select-status");
        }
        return null;
    }

    public Locator getTextFieldState(String textFieldName) {
        return page.locator("input[name='" + textFieldName + "']");
    }

    public boolean getEntityState() {
        return abs.checkElementIsEditable(entityField);
    }

    public String setSelectedRole() throws IOException {
        int index = switch (abs.userinfoList().get("entity")) {
            case "EBL_MT5" -> 0;
            case "EIEHK" -> 1;
            case "XPro" -> 2;
            default -> 0;
        };
        entityRoleDropdown.nth(index).click();
        abs.selectUnselectedDropdownOption();
        return entityRoleDropdown.nth(index).textContent();
    }

    public void setSelectedRole(String roleVal) throws IOException {
        int index = switch (abs.userinfoList().get("entity")) {
            case "EBL_MT5" -> 0;
            case "EIEHK" -> 1;
            case "XPro" -> 2;
            default -> 0;
        };
        entityRoleDropdown.nth(index).click();
        dropdownOption.filter(new Locator.FilterOptions().setHasText(roleVal)).click();
    }

    public String getTextFieldVal(String textFieldName) throws IOException {
        int index = switch (abs.userinfoList().get("entity")) {
            case "EBL_MT5" -> 0;
            case "EIEHK" -> 1;
            case "XPro" -> 2;
            default -> 0;
        };
        return page.locator("//input[@name='" + textFieldName + "']/parent::div").nth(index).textContent();
    }

    public void clickEntityCheckbox(String entity) {
        page.locator("//input[@name='" + entity + "']").click();
    }

    public boolean filteredEntityVal(String entity) {
        boolean nextBtnIsEnable = nextPageBtn.isEnabled();
        boolean pageRecordsMatched = true;
        while (nextBtnIsEnable && pageRecordsMatched) {
            pageRecordsMatched = abs.getFilteredVal(entity, rows, page.locator(".css-er7ssv").nth(0));
            nextPageBtn.click();
            nextBtnIsEnable = nextPageBtn.isEnabled();
        }
        return pageRecordsMatched;
    }
}
