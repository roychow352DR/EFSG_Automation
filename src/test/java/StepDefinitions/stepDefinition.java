package StepDefinitions;

import PageObject.LoginPage;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utils.BaseTest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

public class stepDefinition extends BaseTest {
    public LoginPage loginPage;
    @Given("the user lands on Admin Portal login page")
    public void the_user_lands_on_Admin_Portal_login_page() throws IOException, URISyntaxException {
        loginPage =  launchApplication();
    }

    @When("^the user fills in with username (.+) and password (.+)$")
    public void the_user_fills_in_with_username_and_password(String username,String password) throws IOException, InterruptedException {
        login.loginApplication(username,password);
       //Thread.sleep(3000);
    }

    @When("the user input nothing as username and password")
    public void the_user_input_nothing_as_username_and_password() throws InterruptedException {
        login.loginApplication("","");
    }

    @And("the user clicks Sign In button")
    public void the_user_clicks_Sign_In_button()
    {
        if(loginPage.ctaButtonStatus()) {
            loginPage.clickSignIn();
        }
    }

    @Then("the user sees Menu display on the screen")
    public void the_user_sees_Menu_display_on_the_screen() throws IOException {
       // String caseId = "560";

        Assert.assertTrue(applicationPage().menuTitle().isDisplayed());
       //driver.quit();
        boolean testPassed = true; // Replace with actual validation logic
        Assert.assertTrue(testPassed);


//        // Log result into Qase// Replace with your Qase case ID
//        qaseApiClient.logTestResult(projectCode, runId, "560", testPassed);
//        System.out.println("Test Result Logged in Qase.");

    }

    @Then("the user sees {string} message pop up")
    public void the_user_sees_message_pop_up(String string)
    {
        Assert.assertTrue(loginPage.errorValidation().equalsIgnoreCase(string));
    }

    @Then("the user sees the Sign In button is unclickable")
    public void the_user_sees_the_Sign_In_button_is_unclickable()
    {
        Assert.assertFalse(unclickableCTA(login.ctaButton()));

    }

    @After
    public void tearDown()
    {
        driver.quit();
    }

}
