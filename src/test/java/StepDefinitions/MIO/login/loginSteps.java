package StepDefinitions.MIO.login;

import PageObject.MIOadmin.DashboardPage;
import PageObject.MIOadmin.MIOLoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utils.BaseTest;

import java.io.IOException;

public class loginSteps extends BaseTest {
    public MIOLoginPage mioLoginPage;
    public DashboardPage dashboardPage;
    @Given("the user lands on MIO Admin Portal login page")
    public void the_user_lands_on_MIO_Admin_Portal_login_page() throws IOException, InterruptedException {
        mioLoginPage = launchMIOApplication();
    }

    @Given("^the user fills username (.+) and password (.+) on MIO Admin Portal login page$")
    public void the_user_fills_username_and_password_on_MIO_Admin_Portal_login_page(String username,String password)
    {
        mioLoginPage.fillCredential(username,password);
    }

    @When("the user clicks Sign In button on MIO Admin Portal login page")
    public void the_user_clicks_Sign_In_button_on_MIO_Admin_Portal_login_page()
    {
        if (mioLoginPage.signInButtonStatus()) {
            dashboardPage = mioLoginPage.clickSignIn();
        }
    }

    @Then("the user sees {string} display on the MIO Admin Portal landing screen")
    public void the_user_sees_display_on_the_MIO_Admin_Portal_landing_screen(String text) throws InterruptedException {
        Thread.sleep(5000);
        Assert.assertEquals(dashboardPage.welcomeMsg(),text);

    }

}
