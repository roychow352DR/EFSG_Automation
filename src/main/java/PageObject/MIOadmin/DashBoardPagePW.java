package PageObject.MIOadmin;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class DashBoardPagePW {

    private final Page page;
    public final Locator profile;
    public DashBoardPagePW(Page page){
        this.page = page;
        profile = page.locator(".css-1y3ojfh");
    }

    public Locator getProfileName(String name){
        return profile.filter(new Locator.FilterOptions().setHasText(name));
    }
}
