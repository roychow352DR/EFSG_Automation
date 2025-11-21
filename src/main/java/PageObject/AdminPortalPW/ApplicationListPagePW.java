package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.io.IOException;

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
    public String email;
    public String entity;
    public final Locator nextPageBtn;
    public final Locator status;
    public final Locator entityRow;
    public final Locator emailRow;
    public final Locator filterInputField;
    public final Locator searchField;

    public ApplicationListPagePW(Page page) throws IOException {
        this.page = page;
        abs = new AbstractComponentsPW(page);
        this.menuTitle = page.getByText("Menu");
        this.buttons = page.getByRole(AriaRole.BUTTON);
        this.radioButtons = page.locator("label");
        this.submitButton = page.locator(".css-1s50f5r");
        this.column = page.locator(".css-vh3dxd");
        this.row = page.locator("tbody tr");
        this.statusCol = page.locator("td:nth-child(6)");
        this.nextPageBtn = page.locator("button[aria-label='Go to next page']");
        this.status = page.locator(".css-9iedg7");
        this.entityRow = page.locator(".css-ff6t81:nth-child(1)");
        this.entity = abs.userinfoList().get("entity");
        this.emailRow = page.locator(".css-ff6t81:nth-child(2)");
        this.filterInputField = page.locator(".css-1svhit8");
        this.searchField = page.locator(".css-1ixds2g");
    }

    public Locator getMenuText() {
        return this.menuTitle;
    }

    public void createIndividual() {
        clickButton("Create Account");
        clickRadioButton("Individual");
        clickButton("Submit");
    }

    public void clickButton(String buttonName) {
        buttons.filter(new Locator.FilterOptions().setHasText(buttonName)).click();
    }

    public void clickRadioButton(String radioBtnLabel) {
        radioButtons.filter(new Locator.FilterOptions().setHasText(radioBtnLabel)).click();
    }

    public void clickBtnOnCreate(String buttonName) {
        submitButton.filter(new Locator.FilterOptions().setHas(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName(buttonName)))).click();
    }

    public Locator getApplicationStatus(String email,Locator firstRow) {;
        abs.waitForLocatorVisible(firstRow);
        abs.getItemsByText(email, emailRow, nextPageBtn, entity, entityRow);
        Locator applicationStatus = row.filter(new Locator.FilterOptions().setHasText(email)).locator(".css-4soh8v").nth(1);
        abs.waitForLocatorVisible(applicationStatus);
        return applicationStatus;
    }

    public void clickDetailBtn(String applicationStatus) throws IOException {
        getStatusEmail(applicationStatus);
        row.filter(new Locator.FilterOptions().setHasText(email)).getByRole(AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Detail")).first().click();
    }

    public String getStatusEmail(String applicationStatus) throws IOException {
        abs.getItemsByText(applicationStatus, status, nextPageBtn, entity, entityRow);
        email = row.filter(new Locator.FilterOptions().setHasText(applicationStatus)).first().locator(".css-ff6t81").nth(1).textContent();
        return email;
    }

    public void clickDetailBtn() {
        abs.getItemsByText(email, emailRow, nextPageBtn, entity, entityRow);
        row.filter(new Locator.FilterOptions().setHasText(email)).getByRole(AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Detail")).first().click();
    }

    public void clickClientRecordDetailBtn(String email) {
        this.email = email;
        abs.getItemsByText(email, emailRow, nextPageBtn, entity, entityRow);
        row.filter(new Locator.FilterOptions().setHasText(email)).getByRole(AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Detail")).first().click();
    }

    public void inputFilterValue(String filterVal, String filterField) {
        page.locator("input[name='" + filterField + "']").fill(filterVal);
    }

    public boolean filteredVal(String col, String filterVal) {
        if (col.equalsIgnoreCase("Email")) {
            return abs.getFilteredVal(filterVal, row, page.locator(".css-ff6t81").nth(1));
        }
        return false;
    }

    public void fillSearchVal(String value) {
        searchField.fill(value);
    }

    public Locator getButton(String buttonText) {
        return buttons.filter(new Locator.FilterOptions().setHasText(buttonText));
    }

    public void clickEntityCheckbox(String entity) {
        page.locator("//input[@name='" + entity + "']").click();
    }

    public boolean filteredEntityVal(String entity) {
        boolean nextBtnIsEnable = nextPageBtn.isEnabled();
        boolean pageRecordsMatched = true;
        while (nextBtnIsEnable && pageRecordsMatched) {
            pageRecordsMatched = abs.getFilteredVal(entity, row, page.locator(".css-ff6t81").nth(0));
            nextPageBtn.click();
            nextBtnIsEnable = nextPageBtn.isEnabled();
        }
        return pageRecordsMatched;
    }

    public Locator getFirstRow() {
        return row.first();
    }
}
