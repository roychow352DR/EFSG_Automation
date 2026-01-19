package StepDefinitions.NativeApp.aoSteps;

import PageObject.NativeApp.AppPOManager;
import StepDefinitions.NativeApp.login.loginSteps;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class aoSteps {

    AppPOManager appPoManager = loginSteps.appPOManager;

    @Then("the user sees client agreement page")
    public void the_user_sees_client_agreement_page() {
        Assert.assertTrue(appPoManager.getAppClientAgreementPage().getClientAgreementTitle().isDisplayed());
    }

    @When("the user taps button {string} on the app portfolio page")
    public void the_user_taps_button_on_the_app_portfolio_page(String btnName){
        appPoManager.getAppPortfolioPage().tapButtonOnPortfolioPage(btnName);
    }

    @When("the user fills value {string} in the text field {string} on the app sign up page")
    public void the_user_fills_value_in_the_text_field_on_the_app_sign_up_page(String value,String textField) throws InterruptedException {
        appPoManager.getAppSignupPage().enterTextFieldValue(value,textField);
    }

    @And("the user fills mandatory information on the app sign up page")
    public void the_user_fills_mandatory_information_on_the_app_sign_up_page() throws InterruptedException {
        appPoManager.getAppSignupPage().fillMandatoryFields();
    }

    @Then("the user sees error message {string} is displayed at the app sign up page")
    public void the_user_sees_error_message_is_displayed_at_the_app_sign_up_page(String errorText) {
        Assert.assertEquals(appPoManager.getAppSignupPage().getErrorMsg(errorText).getText(),errorText);
    }

    @Then("the user sees username {string} is display at the app me page")
    public void the_user_sees_username_is_display_at_the_app_me_page(String username){
        Assert.assertTrue(appPoManager.getAppMePage().getUsername(username).isDisplayed());
    }

}
