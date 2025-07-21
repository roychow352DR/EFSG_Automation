package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.io.IOException;

public class ApplicationInfoPagePW {
    private final Page page;
    private final AbstractComponentsPW abs;
    public final Locator entityDropdown;
    public final Locator listItems;
    public final Locator emailField;
    public final Locator countryCodeField;
    public final Locator phoneNumberField;
    //public final Locator nextButton;
    public final Locator errorText;
    public final Locator toastMsg;
    public final Locator buttons;
    public final Locator reasonDropdown;
    public final Locator dropdownOptions;
    public final Locator promoCodeField;
    public final Locator referralCodeField;
    String applicantEmail;

    public ApplicationInfoPagePW(Page page) {
        this.page = page;
        abs = new AbstractComponentsPW(page);
        this.entityDropdown = page.locator("#mui-component-select-entity");
        this.listItems = page.locator(".css-sudvrv");
        this.emailField = page.locator("input[name='email']");
        this.countryCodeField = page.locator("#mui-component-select-mobileCountryCode");
        this.phoneNumberField = page.locator("input[name='mobile']");
       // this.nextButton = page.locator(".css-15j76c0");
        this.errorText = page.locator(".css-1wercf4").first();
        this.toastMsg = page.locator(".Toastify__toast-body div").nth(1);
        this.buttons = page.getByRole(AriaRole.BUTTON);
        this.reasonDropdown = page.locator("#mui-component-select-reason");
        this.dropdownOptions = page.getByRole(AriaRole.OPTION);
        this.promoCodeField = page.locator("input[name='promoCode']");
        this.referralCodeField = page.locator("input[name='upperIbAcc']");

    }

    public void fillApplicationInfo(boolean isExistedEmail,boolean isExistedPhoneNumber) throws IOException {
        selectEntity();
        fillEmail(isExistedEmail);
        fillPhoneNumber(isExistedPhoneNumber);
        submitApplicantInfo(isExistedEmail,isExistedPhoneNumber);
    }

    public void fillMandatory(boolean isExistedEmail,boolean isExistedPhoneNumber) throws IOException {
        selectEntity();
        fillEmail(isExistedEmail);
        fillPhoneNumber(isExistedPhoneNumber);
    }


    public void selectEntity() throws IOException {
        entityDropdown.click();
        listItems.filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("entity"))).click();
    }

    public void fillEmail(boolean isExistedEmail) throws IOException {
        if (!isExistedEmail) {
            applicantEmail = abs.userinfoList().get("email");
        }
        else {
            applicantEmail = abs.userinfoList().get("existedEmail");
        }
        emailField.fill(applicantEmail);
    }

    public void fillPhoneNumber(boolean isExistedPhoneNumber) throws IOException {
        countryCodeField.click();
        listItems.filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("countryCode"))).click();
        if (!isExistedPhoneNumber) {
            phoneNumberField.fill(abs.userinfoList().get("phoneNumber"));
        }
        else {
            phoneNumberField.fill(abs.userinfoList().get("existedPhoneNumber"));
        }
    }

    public String submittedApplicantEmail() {
        return applicantEmail;
    }

    public Locator errorValidation() {
        abs.waitForLocatorVisible(errorText);
        return errorText;
    }

    public void refill(String errorText,boolean isExistedEmail,boolean isExistedPhoneNumber) throws IOException {
        if (errorText.contains("email") && !isExistedEmail) {
            emailField.fill("");
            applicantEmail = abs.userinfoList().get("email");
            emailField.fill(applicantEmail);

        } else if (errorText.contains("phone") && !isExistedPhoneNumber) {
            phoneNumberField.fill("");
            phoneNumberField.fill(abs.userinfoList().get("phoneNumber"));
        }
    }

    public void clickNext() {
        buttons.filter(new Locator.FilterOptions().setHasText("Next To Personal Information")).click();
    }

    public void submitApplicantInfo(boolean isExistedEmail,boolean isExistedPhoneNumber) throws IOException {
        String toastMsg;
        do {
            buttons.filter(new Locator.FilterOptions().setHasText("Next To Personal Information")).click();
            toastMsg = getToastMsg().textContent();
            if (toastMsg.contains("in use")) {
                refill(toastMsg,isExistedEmail,isExistedPhoneNumber);
                if (!isExistedEmail && !isExistedPhoneNumber) {
                    buttons.filter(new Locator.FilterOptions().setHasText("Next To Personal Information")).click();
                }
                toastMsg = getToastMsg().textContent();
            }

        } while (toastMsg == null);
    }

    public void clickButtonByText(String buttonText) {
        buttons.filter(new Locator.FilterOptions().setHasText(buttonText)).click();
    }

    public void selectReason(String reason) {
        reasonDropdown.click();
        dropdownOptions.filter(new Locator.FilterOptions().setHasText(reason)).click();
    }

    public void fillPromoCode(String promoCode)
    {
        promoCodeField.fill(promoCode);
    }

    public void fillReferralCode(String referralCode){
        referralCodeField.fill(referralCode);
    }

    public Locator getToastMsg()
    {
        abs.waitForLocatorVisible(toastMsg);
        return toastMsg;
    }



}
