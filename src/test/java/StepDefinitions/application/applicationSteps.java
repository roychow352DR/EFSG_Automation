package StepDefinitions.application;

import PageObject.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utils.BaseTest;

import java.io.IOException;

public class applicationSteps extends BaseTest {
    public LoginPage login;
    public ApplicationPage applicationPage;
    public ApplicantInformationPage applicationInfoPage;
    public PersonalInfoPage personalInfoPage;
    public ContactInfoPage contactInfoPage;
    public EmployeeFinancialPage employeeFinancialPage;
    public TradingExperiencePage tradingExperiencePage;

    @Given("the user logged in to Admin Portal")
    public void the_user_logged_in_to_Admin_Portal() throws IOException, InterruptedException {
        login = launchApplication();
        login.loginApplication("aoadmin01", "P@ssw0rd!");
        Thread.sleep(2000);
        if (login.ctaButtonStatus()) {
            applicationPage = login.clickSignIn();
        }
        Assert.assertTrue(applicationPage().menuTitle().isDisplayed());
    }

    @Given("the user clicks {string} button on the application page")
    public void the_user_clicks_button_on_the_application_page(String string) {
        applicationPage.clickButton(string);
    }

    @And("the user selects {string} radio button on the create account pop up")
    public void And_the_user_selects_radio_button_on_the_create_account_pop_up(String string) {
        applicationPage.clickRadioButton(string);
    }

    @And("the user clicks {string} button on the create account pop up")
    public ApplicantInformationPage the_user_clicks_button_on_the_create_account_pop_up(String string) {
        applicationPage.clickSubmitButton();
        applicationInfoPage = new ApplicantInformationPage(driver);
        return applicationInfoPage;
    }

    @And("the user fills application information page")
    public void the_user_fills_application_information_page() throws InterruptedException {
        applicationInfoPage.fillInApplicantInfo();
        String submitSuccess = applicationInfoPage.submitApplicationInfo();
        while (submitSuccess.contains("in use"))
        {
            if (submitSuccess.contains("email")) {
                applicationInfoPage.refill("email");
            }
            else if (submitSuccess.contains("mobile"))
            {
                applicationInfoPage.refill("mobile");
             //   submitSuccess = applicationInfoPage.submitApplicationInfo();
            }
            submitSuccess = applicationInfoPage.submitApplicationInfo();
        }
        personalInfoPage = applicationInfoPage.createPersonalInfo();
    }

    @And ("the user fills personal information page")
    public void the_user_fills_personal_information_page() throws InterruptedException {
        personalInfoPage.fillInPersonalInfo();
        contactInfoPage = personalInfoPage.submitPersonalInfo();

    }

    @And ("the user fills contact information page")
    public void the_user_fills_contact_information_page()
    {
        contactInfoPage.fillContactInformation();
        employeeFinancialPage = contactInfoPage.submitContactInfo();
    }

    @And ("the user fills employee & financial information page")
    public void the_user_fills_employee_financial_information_page()
    {
        employeeFinancialPage.fillEmployeeFinancial();
        tradingExperiencePage = employeeFinancialPage.submitEmployeeFinancial();
    }

    @And ("the user fills trading experience page")
    public void the_user_fills_trading_experience_page()
    {
        tradingExperiencePage.fillTradeExperience();

    }

    @When("the user clicks {string} button on the trading experience page")
    public void the_user_clicks_submit_button_on_the_trading_experience_page(String string)
    {
        tradingExperiencePage.submitTradeExperience(string);
    }

    @Then("the user sees a record in {string} status is created on the application list")
    public void the_user_sees_a_record_in_status_is_created_on_the_application_list(String string) throws InterruptedException {
        Thread.sleep(5000);
        Assert.assertEquals(applicationPage.getNewRecordEmail(),applicationInfoPage.retrieveSubmittedEmail());
        Assert.assertEquals(applicationPage.getNewRecordStatus(),string);
    }
}
