package PageObject.AdminPortalPW;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

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
    public final Locator nextPageBtn;
    public final Locator status;

    public ApplicationListPagePW(Page page) {
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
    }

    public Locator getMenuText() {
        return this.menuTitle;
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

    public Locator getApplicationStatus(String email) {
        Locator applicationStatus = row.filter(new Locator.FilterOptions().setHasText(email)).locator(".css-4soh8v").nth(1);
        abs.waitForLocatorVisible(applicationStatus);
        return applicationStatus;
    }

    public void clickDetailBtn(String applicationStatus) {
        getStatusByText(applicationStatus);
        email = row.filter(new Locator.FilterOptions().setHasText(applicationStatus)).first().locator(".css-ff6t81").nth(1).textContent();
        row.filter(new Locator.FilterOptions().setHasText(applicationStatus)).getByRole(AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Detail")).first().click();
    }

    public String getStatusEmail(String applicationStatus) {
        getStatusByText(applicationStatus);
        email = row.filter(new Locator.FilterOptions().setHasText(applicationStatus)).first().locator(".css-ff6t81").nth(1).textContent();
        return email;
    }

    public void findApplicationRow(String applicationStatus) {
        email = row.filter(new Locator.FilterOptions().setHasText(applicationStatus)).first().locator(".css-ff6t81").nth(1).textContent();
        row.filter(new Locator.FilterOptions().setHasText(applicationStatus)).getByRole(AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Detail")).first().click();
    }

    // click next page til the record display at application list based on status text
    public void getStatusByText(String statusText) {
        boolean isFound = false;
        abs.waitForLocatorVisible(status.first());
        while (true) {
            int count = status.count();
            for (int i = 0; i < count; i++) {
                Locator locator = status.nth(i);
                String text = locator.textContent();
                if (text != null && text.equalsIgnoreCase(statusText)) {
                    isFound = true;
                    break;
                }
            }
            if (isFound) {
                break;
            }
            if (nextPageBtn.isEnabled()) {
                nextPageBtn.click();
                abs.waitForLocatorVisible(status.first());
            } else {
                System.out.println("No more pages. " + statusText + " not found.");
                break;
            }
        }
    }

}
