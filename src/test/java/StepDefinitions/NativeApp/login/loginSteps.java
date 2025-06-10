package StepDefinitions.NativeApp.login;

import PageObject.NativeApp.AppHomePage;
import PageObject.NativeApp.AppLoginPage;
import PageObject.NativeApp.BiometricsPage;
import PageObject.NativeApp.WelcomePage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utils.BaseTest;

import java.io.IOException;

public class loginSteps extends BaseTest {
    private WelcomePage welcomePage;
    private AppLoginPage loginPage;
    private BiometricsPage biometricsPage;
    private AppHomePage appHomePage;

    @Given("the user launch the app")
    public void the_user_launch_the_app() throws IOException, InterruptedException {
        welcomePage = launchApp();
    }

    @Given("the user lands on the login page")
    public void the_user_lands_on_the_login_page() throws InterruptedException {
        loginPage = welcomePage.launchToLogin();
        Assert.assertTrue(loginPage.loginPageValidation());
    }

    @When("^the user fills email (.+) and password (.+) on App login page$")
    public void the_user_fills_username_and_password_on_App_login_page(String email,String password) throws InterruptedException {
        loginPage.fillCredential(email,password);
    }

    @And("the user clicks Login button on App login page")
    public void the_user_clicks_Login_button_on_App_login_page() throws InterruptedException {
        biometricsPage = loginPage.clickLogin();
    }

    @And("the user skips biometric validation")
    public void the_user_skips_biometric_validation() throws InterruptedException {
        if (biometricsPage.biometricPageValidation()) {
            Thread.sleep(3000);
            appHomePage = biometricsPage.skipBiometric();
        }
    }

    @Then("the user sees {string} button is displayed at the home page")
    public void the_user_sees__button_is_displayed_at_the_home_page(String string)
    {
        Assert.assertTrue(appHomePage.buttonValidation());
    }
}
