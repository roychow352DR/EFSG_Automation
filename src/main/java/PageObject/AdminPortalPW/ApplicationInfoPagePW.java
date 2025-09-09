package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import utils.BaseTest;
import utils.SetCondition;
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
    public final Locator labels;
    public final Locator usernameField;
    public final Locator textFields;
    String applicantEmail;
    public SetCondition setCondition;

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
        this.labels = page.locator(".css-9iedg7");
        this.usernameField = page.locator("input[name='username']");
        this.textFields = page.getByRole(AriaRole.TEXTBOX);
    }

    public void fillApplicationInfo(boolean isExistedEmail,boolean isExistedPhoneNumber,boolean isCrossEntity) throws IOException {
        selectEntity(isCrossEntity);
        if (abs.userinfoList().get("entity").contains("EBL")){
            fillRandomUsername();
        }
        fillEmail(isExistedEmail);
        fillPhoneNumber(isExistedPhoneNumber);
        submitApplicantInfo(isExistedEmail,isExistedPhoneNumber);
    }

    public void fillMandatory(boolean isExistedEmail,boolean isExistedPhoneNumber,boolean isCrossEntity) throws IOException {
        selectEntity(isCrossEntity);
        if (abs.userinfoList().get("entity").contains("EBL")){
            fillRandomUsername();
        }
        fillEmail(isExistedEmail);
        fillPhoneNumber(isExistedPhoneNumber);
    }


    public void selectEntity(boolean isCrossEntity) throws IOException {
        entityDropdown.click();
        if (!isCrossEntity) {
            listItems.filter(new Locator.FilterOptions().setHas(page.getByText(abs.userinfoList().get("entity"),
                    new Page.GetByTextOptions().setExact(true)))).click();
        }
        else if (!abs.userinfoList().get("entity").equalsIgnoreCase("Xpro")){
            listItems.filter(new Locator.FilterOptions().setHas(page.getByText("Xpro",
                    new Page.GetByTextOptions().setExact(true)))).click();
        }
        else {
           // listItems.filter(new Locator.FilterOptions().setHasText("EIEHK")).click();
            listItems.filter(new Locator.FilterOptions().setHas(page.getByText("EIEHK",
                    new Page.GetByTextOptions().setExact(true)))).click();
        }
    }

    public void selectEntity(boolean isCrossEntity,String entity) throws IOException {
        entityDropdown.click();
        listItems.filter(new Locator.FilterOptions().setHasText(entity)).click();
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

    public void fillEmail(String email) throws IOException {
        applicantEmail = email;
        emailField.fill(email);
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

    public void fillPhoneNumber(String phoneNumber) throws IOException {
        countryCodeField.click();
        listItems.filter(new Locator.FilterOptions().setHasText(abs.userinfoList().get("countryCode"))).click();
        phoneNumberField.fill(phoneNumber);

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

        } else if (errorText.contains("username")) {
            usernameField.fill("");
            usernameField.fill(abs.userinfoList().get("username"));
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
        return toastMsg.first();
    }

    public Locator getLabel(String labelText)
    {
        return labels.filter(new Locator.FilterOptions().setHasText(labelText));
    }

    public Locator getButtonByText(String buttonText){
        return buttons.filter(new Locator.FilterOptions().setHas(page.getByText(buttonText,new Page.GetByTextOptions().setExact(true))));
    }

    public void fillUsername(String username){
        usernameField.fill(username);
    }


    public void fillRandomUsername() throws IOException {
        usernameField.fill(abs.userinfoList().get("username"));
    }

    public void fillRandomUsername(int length) throws IOException {
        usernameField.fill(abs.randomString(length));
    }

    public void fillFullWidthRandomUsername(int length) throws IOException {
        usernameField.fill(abs.toFullWidth(abs.randomString(length)));
    }

    public Locator getTextField(String fieldName){
        return page.locator("input[name='"+fieldName+"']");
    }

}
