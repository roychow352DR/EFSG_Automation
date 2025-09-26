package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.io.IOException;

public class CmApplicationInfoPage {
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
    public final Locator inputFieldSession;
    public final Locator textField;
    String applicantEmail;

    public CmApplicationInfoPage(Page page) {
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
        this.inputFieldSession = page.locator(".css-hp68mp");
        this.textField = page.locator("input");
    }

    public void clickButtonByText(String buttonText) throws InterruptedException {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        page.waitForTimeout(500);
        buttons.filter(new Locator.FilterOptions().setHasText(buttonText)).click();
    }
    public Locator getLabel(String labelText)
    {
        return labels.filter(new Locator.FilterOptions().setHasText(labelText));
    }

    public Locator getButtonByText(String buttonText){
        return buttons.filter(new Locator.FilterOptions().setHas(page.getByText(buttonText,new Page.GetByTextOptions().setExact(true))));
    }

    public boolean checkTextFieldIsEditable() {
        return abs.checkElementIsEditable(textField);
    }
}
