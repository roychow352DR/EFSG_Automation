package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import PageObject.AdminPortal.PersonalInfoPage;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.io.IOException;

public class PersonalInfoPagePW {
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
    public final Locator checkbox;

    public PersonalInfoPagePW(Page page) {
        this.page = page;
        abs = new AbstractComponentsPW(page);
        this.lastNameField = page.locator("input[name='legalLastNameEn']");
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
        this.checkbox = page.locator(".css-1jaw3da");

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

    public void fillMandatory(boolean isBelow18,boolean isExpired, String expiredCondition, boolean isEdd) throws IOException {
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
        }
        else {
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
                    ,new Page.GetByTextOptions().setExact(true)))).click();
        } else if (!expiredCondition.contains("before")) {
            datePickerArrow.click();
            dayItems.filter(new Locator.FilterOptions().setHas(page.getByText(abs.userinfoList().get("expiryDay")
                    ,new Page.GetByTextOptions().setExact(true)))).first().click();
        } else {
            dayItems.filter(new Locator.FilterOptions().setHas(page.getByText(String.valueOf(Integer.parseInt(abs.userinfoList().get("expiryDay")) - 1)
                    ,new Page.GetByTextOptions().setExact(true)))).click();
        }
    }

    public void uncheckBox(String label) {
        checkbox.filter(new Locator.FilterOptions().setHas(page.getByLabel(label))).click();
    }

    public void fillBlacklistName(String firstname, String lastname) {
        firstNameField.fill(firstname);
        lastNameField.fill(lastname);
    }

    public void fillSpecificId(String id) throws IOException {
        idNoField.fill(id);
    }

   public void selectIdType(String idType)
   {
       idTypeDropdown.click();
       dropdownOption.filter(new Locator.FilterOptions().setHasText(idType)).click();
   }


}
