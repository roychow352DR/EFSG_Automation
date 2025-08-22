package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import java.io.IOException;

public class ContactInfoPagePW {
    private final Page page;
    private final AbstractComponentsPW abs;
    public final Locator addressField;
    public final Locator cityField;
    public final Locator nextBtn;
    public final Locator buttons;

    public ContactInfoPagePW(Page page) {
        this.page = page;
        abs = new AbstractComponentsPW(page);
        this.addressField = page.locator("input[name='addressLine1']");
        this.cityField = page.locator("input[name='city']");
        this.nextBtn = page.getByRole(AriaRole.BUTTON);
        this.buttons = page.getByRole(AriaRole.BUTTON);
    }

    public void fillContactInfo() throws IOException {
        fillAddress();
        clickNext();
    }

    public void fillMandatory() throws IOException {
        fillAddress();
    }

    public void fillAddress() throws IOException {
        addressField.fill(abs.userinfoList().get("addressLine1"));
        cityField.fill(abs.userinfoList().get("city"));
    }

    public void clickNext() {
        nextBtn.filter(new Locator.FilterOptions().setHasText("Next To Employee and Financial Information")).click();
    }

    public void clickButtonByText(String buttonText) {
        buttons.filter(new Locator.FilterOptions().setHasText(buttonText)).click();
    }

}
