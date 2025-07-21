package StepDefinitions.MIO.login;

import PageObject.MIOadmin.MIOPOManager;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utils.BaseTest;

import java.io.IOException;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MIOLoginSteps extends BaseTest {
    public MIOPOManager mioPoManager;
    @Given("the user lands on MIO Admin Portal login page")
    public void the_user_lands_on_MIO_Admin_Portal_login_page() throws IOException, InterruptedException {
        page = initializePage();
        mioPoManager = new MIOPOManager(page);
    }

    @Given("the user fills username {string} and password {string} on MIO Admin Portal login page")
    public void the_user_fills_username_and_password_on_MIO_Admin_Portal_login_page(String username,String password)
    {
        mioPoManager.getLoginPage().filLCredential(username,password);
    }

    @When("the user clicks Sign In button on MIO Admin Portal login page")
    public void the_user_clicks_Sign_In_button_on_MIO_Admin_Portal_login_page()
    {
        mioPoManager.getLoginPage().clickSignIn();
    }

    @Then("the user sees {string} is displayed as profile name")
    public void the_user_sees_display_on_the_MIO_Admin_Portal_landing_screen(String text) throws InterruptedException {
        assertThat(mioPoManager.getDashboardPage().getProfileName(text)).hasText(text);
    }
}
