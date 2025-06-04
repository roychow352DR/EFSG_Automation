package StepDefinitions.AdminPortal.login;

import PageObject.AdminPortal.AdminLoginPage;
import PageObject.AdminPortal.ApplicationListPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utils.BaseTest;

import java.io.IOException;
import java.net.URISyntaxException;

public class loginStepsDefinition extends BaseTest {
    public AdminLoginPage login;
    public ApplicationListPage applicationListPage;

    @Given("the user lands on Admin Portal login page")
    public void the_user_lands_on_Admin_Portal_login_page() throws IOException, URISyntaxException, InterruptedException {
        login =  launchApplication();
        Thread.sleep(2000);
    }

    @Given("^the user fills in with username (.+) and password (.+)$")
    public void the_user_fills_in_with_username_and_password(String username,String password) throws IOException, InterruptedException {
        login.loginApplication(username,password);
    }

    @Given("the user input nothing as username and password")
    public void the_user_input_nothing_as_username_and_password() throws InterruptedException {
        login.loginApplication("","");
    }

    @When("the user clicks Sign In button")
    public void the_user_clicks_Sign_In_button() throws InterruptedException {
        if(login.ctaButtonStatus()) {
           applicationListPage = login.clickSignIn();
        }
    }

    @Then("the user sees Menu display on the screen")
    public void the_user_sees_Menu_display_on_the_screen() throws IOException {

        Assert.assertTrue(applicationListPage.menuTitle().isDisplayed());


    }

    @Then("the user sees {string} message pop up")
    public void the_user_sees_message_pop_up(String string)
    {
        if (string.contains("Invalid")) {
            Assert.assertTrue(login.loginErrorValidation().equalsIgnoreCase(string));
        }
        else if (string.contains("Suspend"))
        {
            Assert.assertTrue(login.suspendErrorValidation().equalsIgnoreCase(string));
        }
    }

    @Then("the user sees the Sign In button is unclickable")
    public void the_user_sees_the_Sign_In_button_is_unclickable()
    {
        Assert.assertFalse(unclickableCTA(login.ctaButton()));

    }



}
