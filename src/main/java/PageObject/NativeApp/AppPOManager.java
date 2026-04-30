package PageObject.NativeApp;

import io.appium.java_client.AppiumDriver;

public class AppPOManager {

    private final AppLoginPage appLoginPage;
    private final AppWelcomePage appWelcomePage;
    private final AppHomePage appHomePage;
    private final AppSignupPage appSignupPage;
    private final AppMePage appMePage;
    private final AppMarketsPage appMarketsPage;
    private final AppFooter appFooter;
    private final AppClientAgreementPage appClientAgreementPage;
    private final AppPortfolioPage appPortfolioPage;
    private final AppTradeView appTradeView;
    private final AppInstrumentDetailsPage appInstrumentDetailsPage;
    private final AppEditPositionPage appEditPositionPage;
    private final AppClosePositionPage appClosePositionPage;
    private final AppModifyOrderPage appModifyOrderPage;

    public AppPOManager(AppiumDriver driver) {
        this.appLoginPage = new AppLoginPage(driver);
        this.appWelcomePage = new AppWelcomePage(driver);
        this.appHomePage = new AppHomePage(driver);
        this.appSignupPage = new AppSignupPage(driver);
        this.appMePage = new AppMePage(driver);
        this.appMarketsPage = new AppMarketsPage(driver);
        this.appFooter = new AppFooter(driver);
        this.appClientAgreementPage = new AppClientAgreementPage(driver);
        this.appPortfolioPage = new AppPortfolioPage(driver);
        this.appTradeView = new AppTradeView(driver);
        this.appInstrumentDetailsPage = new AppInstrumentDetailsPage(driver);
        this.appEditPositionPage = new AppEditPositionPage(driver);
        this.appClosePositionPage = new AppClosePositionPage(driver);
        this.appModifyOrderPage = new AppModifyOrderPage(driver);
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

    public AppMarketsPage getAppMarketsPage() {
        return appMarketsPage;
    }

    public AppFooter getAppFooter() {
        return appFooter;
    }

    public AppClientAgreementPage getAppClientAgreementPage() {
        return appClientAgreementPage;
    }

    public AppPortfolioPage getAppPortfolioPage() {
        return appPortfolioPage;
    }

    public AppTradeView getAppTradeView() {
        return appTradeView;
    }

    public AppInstrumentDetailsPage getAppInstrumentDetailsPage() {
        return appInstrumentDetailsPage;
    }

    public AppEditPositionPage getAppEditPositionPage() {
        return appEditPositionPage;
    }

    public AppClosePositionPage getAppClosePositionPage() {
        return appClosePositionPage;
    }

    public AppModifyOrderPage getAppModifyOrderPage() {
        return appModifyOrderPage;
    }

}
