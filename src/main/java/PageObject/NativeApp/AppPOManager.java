package PageObject.NativeApp;

import io.appium.java_client.AppiumDriver;

public class AppPOManager {

    private final AppLoginPage appLoginPage;
    private final AppWelcomePage appWelcomePage;
    private final AppHomePage appHomePage;
    private final AppSignupPage appSignupPage;
    private final AppMePage appMePage;

    public AppPOManager(AppiumDriver driver) {
        this.appLoginPage = new AppLoginPage(driver);
        this.appWelcomePage = new AppWelcomePage(driver);
        this.appHomePage = new AppHomePage(driver);
        this.appSignupPage = new AppSignupPage(driver);
        this.appMePage = new AppMePage(driver);
    }

    public AppLoginPage getAppLoginPage() {
        return appLoginPage;
    }

    public AppWelcomePage getAppWelcomePage() {
        return appWelcomePage;
    }

    public AppHomePage getAppHomePage() {
        return appHomePage;
    }

    public AppSignupPage getAppSignupPage() {
        return appSignupPage;
    }

    public AppMePage getAppMePage() {
        return appMePage;
    }

}
