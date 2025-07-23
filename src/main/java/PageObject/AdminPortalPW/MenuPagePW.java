package PageObject.AdminPortalPW;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class MenuPagePW {
    private final Page page;
    public final Locator menuButtons;

    public MenuPagePW(Page page) {
        this.page = page;
        menuButtons = page.getByRole(AriaRole.BUTTON);
    }

    public void clickMenu(String buttonName) {
        menuButtons.filter(new Locator.FilterOptions().setHasText(buttonName)).click();
    }
}
