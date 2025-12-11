package StepDefinitions.AdminPortal.login;

import API.CoreService;
import PageObject.AdminPortalPW.AOPOManager;
import PageObject.AdminPortal.AdminLoginPage;
import com.microsoft.playwright.Page;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import utils.BaseTest;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AOLoginSteps extends BaseTest {
    public AdminLoginPage login;
    public AOPOManager aopoManager;
    public Page page;
    public CoreService coreService;


    @Given("the user lands on Admin Portal login page")
    public void the_user_lands_on_Admin_Portal_login_page() throws IOException, URISyntaxException, InterruptedException {
        page = initializePage();
        aopoManager = new AOPOManager(page);
    }

    @Given("the user fills in with username {string} and password {string}")
    public void the_user_fills_in_with_username_and_password(String username, String password) throws IOException, InterruptedException {
        // login.loginApplication(username,password);
        aopoManager.getAdminLoginPage().fillCredential(username, password);
    }

    @Given("the user input nothing as username and password")
    public void the_user_input_nothing_as_username_and_password() throws InterruptedException {
        login.loginApplication("", "");
    }

    @When("the user clicks Sign In button")
    public void the_user_clicks_Sign_In_button() throws InterruptedException {
//        if(login.ctaButtonStatus()) {
//           applicationListPage = login.clickSignIn();
//        }
        aopoManager.getAdminLoginPage().clickLogin();
    }

    @Then("the user sees Menu display on the screen")
    public void the_user_sees_Menu_display_on_the_screen() throws IOException {
        assertThat(aopoManager.getApplicationListPage().getMenuText()).isVisible();
    }

    @Then("the user sees {string} message pop up")
    public void the_user_sees_message_pop_up(String error) {
        //  Assert.assertTrue(login.loginErrorValidation().equalsIgnoreCase(string));
        assertThat(aopoManager.getAdminLoginPage().loginErrorValidation(error)).containsText(error);
    }

    @Then("the user sees the Sign In button is unclickable")
    public void the_user_sees_the_Sign_In_button_is_unclickable() {
        // Assert.assertFalse(unclickableCTA(login.ctaButton()));
        assertThat(aopoManager.getAdminLoginPage().getLoginButton()).isDisabled();

    }

    @Given("webpage launch")
    public void webpage_launch() throws IOException {
        Page page = initializePage();
        aopoManager = new AOPOManager(page);
    }
}
