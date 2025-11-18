package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.io.IOException;
import java.util.Random;

public class CompanyAccountPage {
    private final Page page;
    private final AbstractComponentsPW abs;

    // Contact Info
    public final Locator entityDropdown;
    public final Locator listItems;
    public final Locator emailInput;
    public final Locator countryCodeField;
    public final Locator phoneNumberField;
    public final Locator promoCodeField;
    public final Locator referralCodeField;
    public final Locator usernameField;
    public final Locator settlementCurrencyDropdown;

    // Company Info
    public final Locator legalNameEnInput;
    public final Locator legalEntityNameInput;
    public final Locator hkBusinessRegistrationInput;
    public final Locator certificateOfIncorporationInput;

    // Controlling Person Info
    public final Locator firstNameEnInput;
    public final Locator lastNameEnInput;
    public final Locator firstNameInput;
    public final Locator lastNameInput;
    public final Locator idNoField;
    public final Locator idTypeDropdown;
    public final Locator dateOfBirthInput;
    public final Locator calendarButton;
    public final Locator calendarExtendBtn;
    public final Locator yearItems;
    public final Locator dayItems;
    public final Locator idExpiryDateInput;
    public final Locator dropdownOption;

    // Business Address
    public final Locator businessAddressLine1Input;
    public final Locator businessAddressLine2Input;
    public final Locator businessCityInput;
    public final Locator businessProvinceInput;
    public final Locator businessCountryDropdown;
    public final Locator businessPostcodeInput;

    // Mailing Address
    public final Locator mailAddressLine1Input;
    public final Locator mailAddressLine2Input;
    public final Locator mailCityInput;
    public final Locator mailProvinceInput;
    public final Locator mailCountryDropdown;
    public final Locator mailPostcodeInput;

    // Actions
    public final Locator confirmationDialog;
    public final Locator submitButton;
    public final Locator buttons;

    // Verify detail
    public final Locator detailEditCompanyAccountText;
    public final Locator tabTitle;
    public final Locator accountStatus;
    public final Locator verifyDropdown;
    public final Locator labels;
    public final Locator errorMsg;
    public final Locator toastMsg;

    String applicantEmail;

    public CompanyAccountPage(Page page) {
        this.page = page;
        this.abs = new AbstractComponentsPW(page);

        // Contact Info
        entityDropdown = page.locator("#mui-component-select-entity");
        listItems = page.locator(".css-sudvrv");
        emailInput = page.locator("input[name='email']");
        countryCodeField = page.locator("#mui-component-select-mobileCountryCode");
        phoneNumberField = page.locator("input[name='mobile']");
        promoCodeField = page.locator("input[name='promoCode']");
        referralCodeField = page.locator("input[name='upperIbAcc']");
        usernameField = page.locator("input[name='username']");
        settlementCurrencyDropdown = page.locator("#mui-component-select-settlementCurrency");

        // Company Info
        legalNameEnInput = page.locator("input[name='legalEntityNameEn']");
        legalEntityNameInput = page.locator("input[name='legalEntityName']");
        hkBusinessRegistrationInput = page.locator("input[name='hkBusinessRegistrationNo']");
        certificateOfIncorporationInput = page.locator("input[name='registrationNoOfIncorporation']");

        // Controlling Person Info
        firstNameEnInput = page.locator("input[name='firstAndMiddleNameOfControllingPersonEn']");
        lastNameEnInput = page.locator("input[name='lastNameOfControllingPersonEn']");
        firstNameInput = page.locator("input[name='firstAndMiddleNameOfControllingPerson']");
        lastNameInput = page.locator("input[name='lastNameOfControllingPerson']");
        idTypeDropdown = page.locator("#mui-component-select-identificationType");
        idNoField = page.locator("input[name='identificationNo']");
        dateOfBirthInput = page.locator("input[name='dateOfBirth']");
        calendarButton = page.locator(".css-slyssw");
        calendarExtendBtn = page.locator(".css-1wjkg3");
        yearItems = page.getByRole(AriaRole.BUTTON);
        dayItems = page.locator(".css-1vcqvsc");
        idExpiryDateInput = page.locator("input[name='identificationExpiryDate']");
        dropdownOption = page.getByRole(AriaRole.OPTION);

        // Business Address
        businessAddressLine1Input = page.locator("input[name='businessAddressLine1']");
        businessAddressLine2Input = page.locator("input[name='businessAddressLine2']");
        businessCityInput = page.locator("input[name='businessCity']");
        businessProvinceInput = page.locator("input[name='businessProvince']");
        businessCountryDropdown = page.locator("#mui-component-select-businessCountry");
        businessPostcodeInput = page.locator("input[name='businessPostcode']");

        // Mailing Address
        mailAddressLine1Input = page.locator("input[name='mailAddressLine1']");
        mailAddressLine2Input = page.locator("input[name='mailAddressLine2']");
        mailCityInput = page.locator("input[name='mailCity']");
        mailProvinceInput = page.locator("input[name='mailProvince']");
        mailCountryDropdown = page.locator("#mui-component-select-mailCountry");
        mailPostcodeInput = page.locator("input[name='mailPostcode']");

        // Actions
        confirmationDialog = page.locator(".css-25mri5");
        submitButton = page.locator("button[type='submit']");
        buttons = page.getByRole(AriaRole.BUTTON);

        // Verify detail
        detailEditCompanyAccountText = page.locator("text=Detail/Edit Company Account");
        tabTitle = page.locator(".css-1kslv7y");
        accountStatus = page.locator(".css-9iedg7");
        verifyDropdown = page.locator("#mui-component-select-verify");
        labels = page.locator(".css-gg4vpm");
        errorMsg = page.locator(".css-1wercf4");
        toastMsg = page.locator(".Toastify__toast-body div").nth(1);

    }

    // Utility Methods
    public void fillMandatory(boolean isExistedEmail, boolean isExistedPhoneNumber, boolean isBelow18) throws IOException {
        selectEntity();
        if (abs.userinfoList().get("entity").contains("EBL")) {
            fillRandomUsername();
            selectSettlement();
        }
        fillEmail(isExistedEmail);
        fillPhoneNumber(isExistedPhoneNumber);
        fillLegalName();
        fillCertificateNumber();
        fillControllingPersonName();
        fillIdDetails();
        fillDateOfBirth(isBelow18);
        fillBusinessAddress();
    }

    public void selectEntity() throws IOException {
        entityDropdown.click();
        listItems.filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("entity"))).click();
    }

    public void fillEmail(boolean isExistedEmail) throws IOException {
        if (!isExistedEmail) {
            applicantEmail = abs.userinfoList().get("companyEmail");
        } else {
            applicantEmail = abs.userinfoList().get("existedEmail");
        }
        emailInput.fill(applicantEmail);
    }

    public void fillPhoneNumber(boolean isExistedPhoneNumber) throws IOException {
        countryCodeField.click();
        listItems.filter(new Locator.FilterOptions().setHas(page.getByText(abs.userinfoList().get("countryCode"), new Page.GetByTextOptions().setExact(true)))).click();
        if (!isExistedPhoneNumber) {
            phoneNumberField.fill(abs.userinfoList().get("phoneNumber"));
        } else {
            phoneNumberField.fill(abs.userinfoList().get("existedPhoneNumber"));
        }
    }

    public String generateRandomLegalName() {
        return "Entity_" + System.currentTimeMillis();
    }

    public void fillCertificateNumber() {
        String randomNumber = generateCertificateNumber();
        certificateOfIncorporationInput.fill(randomNumber);
    }

    private String generateCertificateNumber() {
        // Example format: COI-20250807-839274
        String prefix = "COI";
        String datePart = java.time.LocalDate.now().toString().replace("-", "");
        int randomDigits = new Random().nextInt(900000) + 100000; // 6-digit number
        return prefix + "-" + datePart + "-" + randomDigits;
    }

    public void fillControllingPersonName() {
        String firstName = generateRandomFirstName();
        String lastName = generateRandomLastName();
        firstNameEnInput.fill(firstName);
        lastNameEnInput.fill(lastName);
    }

    private String generateRandomFirstName() {
        String[] firstNames = {"Alex", "Jamie", "Taylor", "Jordan", "Morgan", "Casey", "Riley", "Drew", "Quinn", "Skyler"};
        return firstNames[new Random().nextInt(firstNames.length)];
    }

    private String generateRandomLastName() {
        String[] lastNames = {"Smith", "Johnson", "Lee", "Brown", "Garcia", "Martinez", "Davis", "Clark", "Lewis", "Walker"};
        return lastNames[new Random().nextInt(lastNames.length)];
    }

    public void fillIdDetails() {
        String randomId = generateRandomIdNumber();
        idNoField.fill(randomId);

        idTypeDropdown.click();
        page.locator("text=ID Card").click(); // Select "ID" from dropdown
    }

    private String generateRandomIdNumber() {
        // Example format: ID-839274
        int randomDigits = new Random().nextInt(900000) + 100000; // 6-digit number
        return "ID-" + randomDigits;
    }

    public void fillDateOfBirth(boolean isBelow18) throws IOException {
        calendarButton.first().click();
        calendarExtendBtn.click();
        if (!isBelow18) {
            yearItems.filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("dateOfBirthYear"))).click();
        } else {
            yearItems.filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("dateOfBirthYearBelow18"))).click();
        }
        dayItems.filter(new Locator.FilterOptions().setHas(page.getByText(abs.userinfoList().get("dateOfBirthDay"), new Page.GetByTextOptions().setExact(true)))).click();

    }

    public void fillBusinessAddress() {
        String randomAddress = generateRandomHKBusinessAddress();
        String city = "Kowloon";
        String country = "Hong Kong, China";

        businessAddressLine1Input.fill(randomAddress);
        businessCityInput.fill(city);

        businessCountryDropdown.click();
        page.locator("text=" + country).click();
    }

    private String generateRandomHKBusinessAddress() {
        String[] streetNames = {
                "Nathan Road", "Canton Road", "Argyle Street", "Prince Edward Road", "Jordan Road"
        };
        int buildingNumber = new Random().nextInt(200) + 1;
        String street = streetNames[new Random().nextInt(streetNames.length)];
        return buildingNumber + " " + street;
    }

    public boolean isConfirmationDialogVisible() {
        return confirmationDialog.isVisible();
    }

    public void clickSubmit(String buttonName) {
        abs.waitForLocatorVisible(submitButton);
        submitButton.click();
    }

    public void clickButtonByText(String buttonText) {
        buttons.filter(new Locator.FilterOptions().setHasText(buttonText)).click();
    }

    public Locator getTitle() {
        return tabTitle.last();
    }

    public Locator getDetailEditCompanyAccountText() {
        return detailEditCompanyAccountText;
    }

    public String submittedApplicantEmail() {
        return applicantEmail;
    }

    public void fillLegalName() {
        legalNameEnInput.fill(generateRandomLegalName());
    }

    public void fillRandomUsername() throws IOException {
        usernameField.fill(abs.userinfoList().get("username"));
    }

    public Locator getAccountStatus() {
        return accountStatus;
    }

    public void selectSettlement() throws IOException {
        settlementCurrencyDropdown.click();
        dropdownOption.filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("settlementCurrency"))).click();
    }

    public void selectReason(String reason) {
        verifyDropdown.click();
        dropdownOption.filter(new Locator.FilterOptions().setHasText(reason)).click();
    }

    public void fillTextFieldVal(String value, String fieldName) {
        page.locator("input[name='" + fieldName + "']").fill(value);
    }

    public void emptyField(String fieldName) {
        page.locator("input[name='" + fieldName + "']").fill("");
    }

    public Locator getErrorMsg() {
        return errorMsg;
    }

    public Locator getToastMsg() {
        abs.waitForLocatorVisible(toastMsg);
        return toastMsg;
    }

}
