package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.io.IOException;

public class CmPersonalInfoPage {
    private final Page page;
    private final AbstractComponentsPW abs;
    public final Locator lastNameField;
    public final Locator firstNameField;
    public final Locator genderRadio;
    public final Locator countryDropdown;
    public final Locator dropdownOption;
    public final Locator calendarButton;
    public final Locator calendarExtendBtn;
    public final Locator yearItems;
    public final Locator dayItems;
    public final Locator nationalityDropdown;
    public final Locator idTypeDropdown;
    public final Locator idNoField;
    public final Locator buttons;
    public final Locator errorText;
    public final Locator datePickerArrow;
    public final Locator checkboxSession;
    public final Locator mobileField;
    public final Locator historyBtn;
    public final Locator emailField;
    public final Locator historyDialogue;
    public final Locator textField;
    public final Locator label;
    public final Locator toastMsg;
    public final Locator usernameField;
    public String changeValue;
    public final Locator dropdownField;
    public final Locator checkbox;
    public final Locator radioButtons;

    public CmPersonalInfoPage(Page page) {
        this.page = page;
        abs = new AbstractComponentsPW(page);
        lastNameField = page.locator("input[name='legalLastNameEn']");
        this.firstNameField = page.locator("input[name='legalFirstAndMiddleNameEn']");
        this.genderRadio = page.locator(".css-1jaw3da");
        this.countryDropdown = page.locator("#mui-component-select-jurisdictionOfResidence");
        this.dropdownOption = page.getByRole(AriaRole.OPTION);
        this.calendarButton = page.locator(".css-slyssw");
        this.calendarExtendBtn = page.locator(".css-1wjkg3");
        this.yearItems = page.getByRole(AriaRole.BUTTON);
        this.dayItems = page.locator(".css-1vcqvsc");
        this.nationalityDropdown = page.locator("div[id='mui-component-select-jurisdiction']");
        this.idTypeDropdown = page.locator("#mui-component-select-identificationType");
        this.idNoField = page.locator("input[name='identificationNo']");
        this.buttons = page.getByRole(AriaRole.BUTTON);
        this.errorText = page.locator(".css-1wercf4");
        this.datePickerArrow = page.locator("button[title='Next month']");
        //this.checkbox = page.getByRole(AriaRole.CHECKBOX);
        this.checkboxSession = page.locator(".css-15j76c0");
        this.mobileField = page.locator("input[name='mobile']");
        this.historyBtn = page.locator(".css-1xktw9");
        this.emailField = page.locator("input[name='email']");
        this.historyDialogue = page.getByRole(AriaRole.DIALOG, new Page.GetByRoleOptions().setName("History"));
        this.textField = page.locator("input");
        this.label = page.locator("label");
        this.toastMsg = page.locator(".Toastify__toast-body div").nth(1);
        this.usernameField = page.locator("input[name='username']");
        this.dropdownField = page.locator(".css-1tz4v7m div");
        this.checkbox = page.locator("input[type='checkbox']");
        this.radioButtons = page.locator("input[type='radio']");
    }

    public void fillPersonalInfo(boolean isBelow18, boolean isExpired, String expiredCondition, boolean isEdd) throws IOException {
        fillName();
        selectGender();
        selectCountry(isEdd);
        fillDob(isBelow18);
        selectNationality();
        selectIdType();
        fillRandomId();
        selectExpiryDate(isExpired, expiredCondition);
        clickNext();
    }

    public void fillMandatory(boolean isBelow18, boolean isExpired, String expiredCondition, boolean isEdd) throws IOException {
        fillName();
        selectGender();
        selectCountry(isEdd);
        fillDob(isBelow18);
        selectNationality();
        selectIdType();
        fillRandomId();
        selectExpiryDate(isExpired, expiredCondition);
    }

    public void fillName() throws IOException {
        lastNameField.fill(abs.userinfoList().get("lastName"));
        firstNameField.fill(abs.userinfoList().get("firstName"));
    }

    public void selectGender() throws IOException {
        genderRadio.filter(new Locator.FilterOptions().setHas(page.locator("input[value=" + abs.userinfoList().get("gender") + "]"))).click();
    }

    public void selectCountry(boolean isEdd) throws IOException {
        countryDropdown.click();
        if (!isEdd) {
            dropdownOption.filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("country"))).click();
        } else {
            dropdownOption.filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("eddCountry"))).click();
        }
    }

    public void fillDob(boolean isBelow18) throws IOException {
        calendarButton.first().click();
        calendarExtendBtn.click();
        if (!isBelow18) {
            yearItems.filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("dateOfBirthYear"))).click();
        } else {
            yearItems.filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("dateOfBirthYearBelow18"))).click();
        }
        dayItems.filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("dateOfBirthDay"))).click();
    }

    public void selectNationality() throws IOException {
        nationalityDropdown.click();
        dropdownOption.filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("nationality"))).click();
    }

    public void selectIdType() throws IOException {
        idTypeDropdown.click();
        dropdownOption.filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("idType"))).click();
    }

    public void fillRandomId() throws IOException {
        idNoField.fill(abs.userinfoList().get("id"));
    }

    public void clickNext() {
        buttons.filter(new Locator.FilterOptions().setHasText("Next To Contact Information")).click();
    }

    public void clickButtonByText(String buttonText) {
        buttons.filter(new Locator.FilterOptions().setHasText(buttonText)).click();
    }

    public Locator errorValidation() {
        abs.waitForLocatorVisible(errorText);
        return errorText.first();
    }

    public void selectExpiryDate(boolean isExpired, String expiredCondition) throws IOException {
        calendarButton.last().click();
        if (!isExpired) {
            calendarExtendBtn.click();
            yearItems.filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("validExpiryYear"))).click();
            dayItems.filter(new Locator.FilterOptions().setHas(page.getByText(abs.userinfoList().get("expiryDay")
                    , new Page.GetByTextOptions().setExact(true)))).click();
        } else if (!expiredCondition.contains("before")) {
            datePickerArrow.click();
            dayItems.filter(new Locator.FilterOptions().setHas(page.getByText(abs.userinfoList().get("expiryDay")
                    , new Page.GetByTextOptions().setExact(true)))).first().click();
        } else {
            dayItems.filter(new Locator.FilterOptions().setHas(page.getByText(String.valueOf(Integer.parseInt(abs.userinfoList().get("expiryDay")) - 1)
                    , new Page.GetByTextOptions().setExact(true)))).click();
        }
    }

    public void uncheckBox(String label) {
        checkboxSession.filter(new Locator.FilterOptions().setHas(page.locator("input[name='" + label + "']"))).click();
    }

    public void fillBlacklistName(String firstname, String lastname) {
        firstNameField.fill(firstname);
        lastNameField.fill(lastname);
    }

    public void fillSpecificId(String id) throws IOException {
        idNoField.fill(id);
    }

    public void selectIdType(String idType) {
        idTypeDropdown.click();
        dropdownOption.filter(new Locator.FilterOptions().setHasText(idType)).click();
    }

    public void fillMobile() throws IOException {
        mobileField.fill(abs.userinfoList().get("phoneNumber"));
    }

    public void fillMobile(String mobile) throws IOException {
        mobileField.fill(mobile);
    }

    public Locator getHistoryButton() {
        return historyBtn;
    }

    public Locator locatorValidation(String buttonName) {
        if (buttonName.contains("History")) {
            return getHistoryButton();
        }
        return null;
    }

    public String getEmail() {
        return emailField.inputValue();
    }

    public Locator getHistoryDialogue() {
        historyBtn.click();
        return historyDialogue;
    }

    public Locator getHistoryBtn() {
        return historyBtn;
    }

    public void getFieldTextByLabel(String labelName) {
        changeValue = abs.getInputValueByAttribute(textField, "name", labelName);
    }

    public Locator getToastMsg() {
        return toastMsg;
    }

    public void fillUsername(String username) {
        usernameField.fill(username);
    }

    public void fillUsername() throws IOException {
        usernameField.fill(abs.userinfoList().get("username"));
    }

    public String getTextFieldValue(String textFieldName) {
        if (textFieldName.equalsIgnoreCase("username")) {
            return usernameField.inputValue();
        }
        return textFieldName;
    }

    public void fillEmail(String email) throws IOException {
        emailField.fill(email);
    }

    public Locator getTextField(String textFieldName) {
        return page.locator("input[name='" + textFieldName + "']");
    }

    public void fillInputFieldByName(String value, String textFieldName) {
        page.locator("input[name='" + textFieldName + "']").fill(value);
    }

    public boolean checkTextFieldIsEditable() {
        return abs.checkElementIsEditable(textField);
    }

    public boolean checkElementIsClickable() {
        return abs.checkElementIsEnable(dropdownField) &&
                abs.checkElementIsEnable(checkbox) &&
                abs.checkElementIsEnable(radioButtons) &&
                abs.checkElementIsEnable(calendarButton);
    }
}
