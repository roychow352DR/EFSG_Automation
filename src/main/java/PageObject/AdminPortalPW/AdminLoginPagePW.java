package PageObject.AdminPortalPW;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;

public class AdminLoginPagePW {

    private final Page page;
    public final Locator userNameField;
    public final Locator passwordField;
    public final Locator loginButton;
    public final Locator invalidErrorText;
    public final Locator suspendErrorText;
    public AdminLoginPagePW(Page page)
    {
        this.page = page;
        this.userNameField = page.locator(".css-1x5jdmq");
        this.passwordField = page.locator(".css-1uvydh2");
        this.loginButton = page.locator(".css-1m4mrb3-root-contained-root-contained");
        this.invalidErrorText = page.getByText("Invalid username or password.", new Page.GetByTextOptions().setExact(true));
        this.suspendErrorText = page.getByText("User account is suspended! Please contact administration", new Page.GetByTextOptions().setExact(true));
    }
    public void fillCredential(String username,String password) {
        userNameField.fill(username);
        passwordField.fill(password);
    }

    public void clickLogin() {

        loginButton.click();

    }

    public Locator getLoginButton()
    {
        return loginButton;
    }

    public Locator loginErrorValidation(String errorText)
    {
        if (errorText.contains("Invalid")){
            return invalidErrorText;
        }
        else if (errorText.contains("suspended")){
            return suspendErrorText;
        }
        return null;
    }

    public void loginETE(String username,String password)
    {
        fillCredential(username,password);
        clickLogin();
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

}
