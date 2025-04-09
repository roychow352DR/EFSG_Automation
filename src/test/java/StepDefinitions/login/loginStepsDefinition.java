package StepDefinitions.login;

import PageObject.LoginPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utils.BaseTest;

import java.io.IOException;
import java.net.URISyntaxException;

public class loginStepsDefinition extends BaseTest {
    public LoginPage loginPage;
    @Given("the user lands on Admin Portal login page")
    public void the_user_lands_on_Admin_Portal_login_page() throws IOException, URISyntaxException, InterruptedException {
        loginPage =  launchApplication();
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
        if(loginPage.ctaButtonStatus()) {
           loginPage.clickSignIn();
        }
    }

    @Then("the user sees Menu display on the screen")
    public void the_user_sees_Menu_display_on_the_screen() throws IOException {

        Assert.assertTrue(applicationPage().menuTitle().isDisplayed());


    }

    @Then("the user sees {string} message pop up")
    public void the_user_sees_message_pop_up(String string)
    {
        if (string.equalsIgnoreCase("User account is suspended! Please contact administration"))
        {
            Assert.assertTrue(loginPage.suspendErrorValidation().equalsIgnoreCase(string));
        }
        else if (string.equalsIgnoreCase("Invalid username or password"))
        {
            Assert.assertTrue(loginPage.loginErrorValidation().equalsIgnoreCase(string));
        }

    }

    @Then("the user sees the Sign In button is unclickable")
    public void the_user_sees_the_Sign_In_button_is_unclickable()
    {
        Assert.assertFalse(unclickableCTA(login.ctaButton()));

    }


  /*  @After
    public void tearDown() throws InterruptedException {
       // Thread.sleep(5000);
        driver.quit();
    }*/

}
