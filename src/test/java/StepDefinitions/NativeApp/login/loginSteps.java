package StepDefinitions.NativeApp.login;

import Data.AppCredential;
import Data.TradeSymbolConfig;
import PageObject.NativeApp.*;
import io.appium.java_client.AppiumDriver;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import utils.BaseTest;

import java.io.IOException;

public class loginSteps extends BaseTest {
    public static AppPOManager appPOManager;
    public static AppiumDriver driver;

    @Given("the user launch the app")
    public void the_user_launch_the_app() throws IOException, InterruptedException {
        driver = initAppDriver();
        appPOManager= new AppPOManager(driver);
        Assert.assertTrue(appPOManager.getAppHomePage().bottomButtonIsTapped());
    }

    @Given("the user lands on the app login page")
    public void the_user_lands_on_the_app_login_page() throws InterruptedException {
        appPOManager.getAppHomePage().navigateToSignupPage();
        appPOManager.getAppSignupPage().navigateToLoginPage();
        Assert.assertTrue(appPOManager.getAppLoginPage().loginPageValidation());
    }

    @Given("the user lands on the app sign up page")
    public void the_user_lands_on_the_app_sign_up_page() throws InterruptedException {
        appPOManager.getAppHomePage().navigateToSignupPage();
        Assert.assertTrue(appPOManager.getAppSignupPage().getSignupPageTitle());
    }

    @When("the user fills email and password on App login page")
    public void the_user_fills_email_and_password_on_App_login_page() throws InterruptedException {
        AppCredential credential = new AppCredential(productEntity);
        appPOManager.getAppLoginPage().fillCredential(credential.getLoginCredential(),credential.getLoginPassword());
    }

    @When("the user fills username {string} and password {string} on App login page")
    public void the_user_fills_username_and_password_on_App_login_page(String username,String password) throws InterruptedException {
        appPOManager.getAppLoginPage().fillCredential(username, password);
    }

    @And("the user taps Login button on the app login page")
    public void the_user_taps_Login_button_on_the_app_login_page() throws InterruptedException {
        appPOManager.getAppLoginPage().clickLogin();
    }

    @And("the user skips biometric validation")
    public void the_user_skips_biometric_validation() throws InterruptedException {
//        if (biometricsPage.biometricPageValidation()) {
//            Thread.sleep(3000);
//            appHomePage = biometricsPage.skipBiometric();
//        }
        Thread.sleep(5000);
    }

    @Then("the user sees button {string} is displayed at the app home page")
    public void the_user_sees_button_is_displayed_at_the_app_home_page(String buttonName) throws InterruptedException {
        Thread.sleep(5000);
        Assert.assertEquals(appPOManager.getAppHomePage().getButtonText(),buttonName);
        Assert.assertTrue(appPOManager.getAppHomePage().buttonValidation(buttonName));
    }

    @And("the user lands on app home page")
    public void the_user_lands_on_app_home_page() {
        Assert.assertTrue(appPOManager.getAppHomePage().bottomButtonIsTapped());
    }

    @Then("the user sees trade account label on the app me page")
    public void the_user_sees_trade_account_label_on_the_app_me_page() throws InterruptedException {
        Assert.assertTrue(appPOManager.getAppMePage().getTradeAccountLabel());
    }

    @And("the user login as username {string} and password {string} on App login page")
    public void the_user_login_as_username_and_password_on_App_login_page(String username,String password) throws InterruptedException {
        appPOManager.getAppLoginPage().loginAs(username, password);
    }

}
