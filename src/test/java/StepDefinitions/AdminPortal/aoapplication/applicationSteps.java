package StepDefinitions.AdminPortal.aoapplication;

import PageObject.AdminPortal.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utils.BaseTest;

import java.io.IOException;

public class applicationSteps extends BaseTest {
    public AdminLoginPage login;
    public ApplicationListPage applicationPage;
    public ApplicantInformationPage applicationInfoPage;
    public PersonalInfoPage personalInfoPage;
    public ContactInfoPage contactInfoPage;
    public EmployeeFinancialPage employeeFinancialPage;
    public TradingExperiencePage tradingExperiencePage;

    @Given("^the user logged in to Admin Portal as username (.+) and password (.+)$")
    public void logged_in_to_Admin_Portal(String name, String password) throws IOException, InterruptedException {
        login = launchApplication();
        login.loginApplication(name, password);
        Thread.sleep(2000);
        if (login.ctaButtonStatus()) {
            applicationPage = login.clickSignIn();

        }
        Assert.assertTrue(applicationPage().menuTitle().isDisplayed());
    }

    @And("the user clicks {string} button on the application page")
    public void clicks_button_on_the_application_page(String string) {
        applicationPage.clickButton(string);
    }

    @And("the user selects {string} radio button on the create account pop up")
    public void selects_radio_on_the_create_account_pop_up(String string) {
        applicationPage.clickRadioButton(string);
    }

    @And("the user clicks {string} button on the create account pop up")
    public void clicks_cta_on_the_create_account_pop_up(String string) {
        applicationPage.clickSubmitButton();
        applicationInfoPage = new ApplicantInformationPage(driver);
    }

    @And("the user fills application information page")
    public void fills_application_information() throws InterruptedException {
        applicationInfoPage.fillInApplicantInfo();
        String submitSuccess = applicationInfoPage.submitApplicationInfo();
        while (submitSuccess.contains("in use")) {
            if (submitSuccess.contains("email")) {
                applicationInfoPage.refill("email");
            } else if (submitSuccess.contains("mobile")) {
                applicationInfoPage.refill("mobile");
            }
            submitSuccess = applicationInfoPage.submitApplicationInfo();
        }
        personalInfoPage = applicationInfoPage.createPersonalInfo();
    }

    @And("the user fills personal information page")
    public void fills_personal_information() throws InterruptedException {
        personalInfoPage.fillInPersonalInfo();
        contactInfoPage = personalInfoPage.submitPersonalInfo();

    }

    @And("the user fills contact information page")
    public void fills_contact_information() {
        contactInfoPage.fillContactInformation();
        employeeFinancialPage = contactInfoPage.submitContactInfo();
    }

    @And("the user fills employee & financial information page")
    public void fills_employee_financial_information() {
        employeeFinancialPage.fillEmployeeFinancial();
        tradingExperiencePage = employeeFinancialPage.submitEmployeeFinancial();
    }

    @And("the user fills trading experience page")
    public void fills_trading_experience() {
        tradingExperiencePage.fillTradeExperience();

    }

    @When("the user clicks {string} button on the trading experience page")
    public void clicks_button_on_trading_experience(String string) {
        tradingExperiencePage.submitTradeExperience(string);
    }

    @Then("the user sees a record in {string} status is created on the application list")
    public void sees_a_record_with_status_is_created_on_the_application_list(String string) throws InterruptedException {
        Thread.sleep(5000);
        Assert.assertEquals(applicationPage.getNewRecordEmail(), applicationInfoPage.retrieveSubmittedEmail());
        Assert.assertEquals(applicationPage.getNewRecordStatus(), string);
    }

    @When("the user clicks detail button of {string} record on the application page")
    public void clicks_button_of_record_on_application(String status) {
        applicationInfoPage = applicationPage.clickActionButtonBasedOnStatus(status);
    }

    @And("the user clicks {string} button on the application information page")
    public void clicks_button_on_application_information(String buttonName) throws InterruptedException {
        Thread.sleep(5000);
        personalInfoPage = applicationInfoPage.clickNext();
    }

    @And("the user clicks {string} button on the personal information page")
    public void clicks_button_on_personal_information(String buttonName) {
        personalInfoPage.clickCTA(buttonName);
        contactInfoPage = new ContactInfoPage(driver);

    }

    @And("the user clicks {string} button on the contact information page")
    public void clicks_button_on_contact_information(String buttonName) {
        contactInfoPage.clickCTA(buttonName);
        employeeFinancialPage = new EmployeeFinancialPage(driver);
    }

    @And("the user clicks {string} button on the employee & financial information page")
    public void clicks_button_on_employee_financial_information(String buttonName) {
        employeeFinancialPage.clickCTA(buttonName);
        tradingExperiencePage = new TradingExperiencePage(driver);
    }

    @And("the user clicks {string} button on L2 trading experience page")
    public void clicks_button_on_L2_trading_experience(String buttonName) {
        tradingExperiencePage.clickApprove();
    }

    @And("the user selects {string} as verify reason on the verify pop up")
    public void selects_verify_reason_on_the_verify_pop_up(String reason) {
        tradingExperiencePage.selectReason(reason);
    }

    @And("the user clicks {string} button on the verify pop up")
    public void clicks_on_the_verify_pop_up(String buttonName) {
        tradingExperiencePage.clickButtonOnVerify(buttonName);
    }

    @Then("the user sees a record in {string} status after approval")
    public void sees_a_record_with_status_after_first_approval(String status) throws InterruptedException {
        Assert.assertEquals(applicationPage.getEmailStatus(), status);
    }

}
