package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.io.IOException;

public class BlackListPage {
    private final Page page;
    private final AbstractComponentsPW abs;
    public final Locator buttons;
    public final Locator row;
    public final Locator entityDropdown;
    public final Locator dropdownOption;
    public final Locator firstNameField;
    public final Locator lastNameField;
    public final Locator identifyField;
    public final Locator identityType;
    public final Locator categoryDropdown;
    public final Locator nationalityDropdown;
    public final Locator emailField;
    public final Locator countryCodeDropdown;
    public final Locator mobileNoField;
    private String email;
    public final Locator entityCol;
    public final Locator nextPageBtn;
    public final Locator emailCol;
    public final Locator statusCol;
    public final Locator label;

    public BlackListPage(Page page) {
        this.page = page;
        this.abs = new AbstractComponentsPW(page);
        this.buttons = page.getByRole(AriaRole.BUTTON);
        this.row = page.locator("tbody tr");
        this.entityDropdown = page.locator("#mui-component-select-entity");
        this.dropdownOption = page.getByRole(AriaRole.OPTION);
        this.firstNameField = page.locator("input[name='legalFirstAndMiddleNameEn']");
        this.lastNameField = page.locator("input[name='legalLastNameEn']");
        this.identifyField = page.locator("input[name='identificationNo']");
        this.identityType = page.locator("#mui-component-select-identificationType");
        this.categoryDropdown = page.locator("#mui-component-select-reason");
        this.nationalityDropdown = page.locator("#mui-component-select-nationality");
        this.emailField = page.locator("input[name='email']");
        this.countryCodeDropdown = page.locator("#mui-component-select-mobileCountryCode");
        this.mobileNoField = page.locator("input[name='mobile']");
        this.entityCol = page.locator(".css-ff6t81:nth-child(1)");
        this.nextPageBtn = page.locator("button[aria-label='Go to next page']");
        this.emailCol = page.locator(".css-ff6t81:nth-child(2)");
        this.statusCol = page.locator(".css-4soh8v div");
        this.label = page.locator("label");
    }

    public void fillMandatory() throws IOException {
        selectEntity();
        fillFirstLastName();
        fillIdentificationNo("ID");
        selectCategory();
        selectNationality();
        fillMobileNo();
        fillEmail();
    }

    public void clickButton(String buttonText) {
        buttons.filter(new Locator.FilterOptions().setHasText(buttonText)).click();
    }

    public void selectEntity() throws IOException {
        entityDropdown.click();
        dropdownOption.filter(new Locator.FilterOptions().setHasText(abs.blacklistInfoList().get("entity"))).click();
    }

    public void fillFirstLastName() throws IOException {
        firstNameField.fill(abs.blacklistInfoList().get("firstName"));
        lastNameField.fill(abs.blacklistInfoList().get("lastName"));
    }

    public void fillIdentificationNo(String identificationType) throws IOException {
        identityType.click();
        if (identificationType.equalsIgnoreCase("ID")) {
            dropdownOption.filter(new Locator.FilterOptions().setHasText("ID Card")).click();
            identifyField.fill(abs.blacklistInfoList().get("id"));
        } else {
            dropdownOption.filter(new Locator.FilterOptions().setHasText("Passport")).click();
            identifyField.fill(abs.blacklistInfoList().get("passwordNo"));
        }
    }

    public void selectCategory() throws IOException {
        categoryDropdown.click();
        dropdownOption.filter(new Locator.FilterOptions().setHasText(abs.blacklistInfoList().get("category"))).click();
    }

    public void selectNationality() throws IOException {
        nationalityDropdown.click();
        dropdownOption.filter(new Locator.FilterOptions().setHasText(abs.blacklistInfoList().get("nationality"))).click();
    }

    public void fillMobileNo() throws IOException {
        countryCodeDropdown.click();
        dropdownOption.filter(new Locator.FilterOptions().setHasText(abs.blacklistInfoList().get("countryCode"))).click();
        mobileNoField.fill(abs.blacklistInfoList().get("phoneNumber"));
    }

    public void fillEmail() throws IOException {
        email = abs.blacklistInfoList().get("email");
        emailField.fill(email);
    }

    public Locator getBlacklistRecordRow() throws IOException {
        page.waitForTimeout(2000);
        abs.getItemsByText(email, emailCol, nextPageBtn, abs.blacklistInfoList().get("entity"), entityCol);
        return row.filter(new Locator.FilterOptions().setHasText(email));
    }

    public void clickDetailBtn(String blacklistStatus) throws IOException {
        abs.getItemsByText(blacklistStatus, statusCol, nextPageBtn, abs.blacklistInfoList().get("entity"), entityCol);
        email = row.filter(new Locator.FilterOptions().setHas(page.getByText(blacklistStatus, new Page.GetByTextOptions().setExact(true)))).first().locator(emailCol).textContent();
        row.filter(new Locator.FilterOptions().setHasText(email)).getByRole(AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Detail")).first().click();
    }

    public void clickRadioButton(String labelText) {
        label.filter(new Locator.FilterOptions().setHasText(labelText)).click();
    }

    public String getModifiedRowStatus() {
        page.waitForTimeout(2000);
        return row.filter(new Locator.FilterOptions().setHasText(email)).locator(statusCol).textContent();
    }

    public Locator getButton(String buttonName) {
        return buttons.filter(new Locator.FilterOptions().setHasText(buttonName));
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
}
