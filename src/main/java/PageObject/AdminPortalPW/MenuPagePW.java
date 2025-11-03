package PageObject.AdminPortalPW;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class MenuPagePW {
    private final Page page;
    public final Locator menuButtons;
    public final Locator logoutBtn;
    public final Locator profileName;

    public MenuPagePW(Page page) {
        this.page = page;
        this.menuButtons = page.getByRole(AriaRole.BUTTON);
        this.logoutBtn = page.locator(".css-1ernfb2 svg:nth-child(2)");
        this.profileName = page.locator(".css-772rr4").last();
    }

    public void clickMenu(String buttonName) {
        menuButtons.filter(new Locator.FilterOptions().setHasText(buttonName)).click();
    }

    public void clickLogout() {
        logoutBtn.click();
    }

    public Locator getProfile() {
        return profileName;
    }

}
