package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.io.IOException;

public class CmUserInformationPage {
    private final Page page;
    private final AbstractComponentsPW abs;

    public final Locator buttons;
    public final Locator toastMsg;
    public final Locator mobileField;
    public final Locator usernameField;

    public CmUserInformationPage(Page page) {
        this.page = page;
        this.abs = new AbstractComponentsPW(page);
        this.buttons = page.getByRole(AriaRole.BUTTON);
        this.toastMsg = page.locator(".Toastify__toast-body div").nth(1);
        this.mobileField = page.locator("input[name='mobile']");
        this.usernameField = page.locator("input[name='username']");
    }

    public void clickBtnByText(String buttonText) {
        buttons.filter(new Locator.FilterOptions().setHasText(buttonText)).click();
    }

    public void fillValToField(String value, String textFieldName) {
        page.locator("//input[@name='"+textFieldName+"']").fill(value);
    }

    public Locator getToastMsg() {
        abs.waitForLocatorVisible(toastMsg);
        return toastMsg;
    }

    public void fillMobile() throws IOException {
        mobileField.fill(abs.userinfoList().get("phoneNumber"));
    }

    public void fillUsername() throws IOException {
        usernameField.fill(abs.userinfoList().get("username"));
    }
}
