package PageObject.MIOadmin;

import AbstractComponent.AbstractComponentsPW;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;

public class MIOLoginPagePW {

    private final Page page;
    private final AbstractComponentsPW abs;
    public final Locator usernameField;
    public final Locator passwordField;
    public final Locator signInButton;

    public MIOLoginPagePW(Page page){
        this.page = page;
        abs = new AbstractComponentsPW(page);
        usernameField = page.locator(".css-1x5jdmq");
        passwordField = page.locator(".css-1uvydh2");
        signInButton = page.locator(".css-1hjma9j");
    }

    public void signInETE(String username,String password){
        filLCredential(username,password);
        clickSignIn();
    }

    public void filLCredential(String username,String password)
    {
        usernameField.fill(username);
        passwordField.fill(password);
    }

    public void clickSignIn(){
        signInButton.click();
    }
}
