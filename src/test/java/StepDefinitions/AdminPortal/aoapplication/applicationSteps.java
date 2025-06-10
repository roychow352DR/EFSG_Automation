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
    public void the_user_logged_in_to_Admin_Portal(String name, String password) throws IOException, InterruptedException {
        login = launchApplication();
        Thread.sleep(2000);
        login.loginApplication(name, password);
        Thread.sleep(2000);
        if (login.ctaButtonStatus()) {
            applicationPage = login.clickSignIn();

        }
        Assert.assertTrue(applicationPage().menuTitle().isDisplayed());
    }

    @And("the user clicks {string} button on the application page")
    public void the_user_clicks_button_on_the_application_page(String string) {
        applicationPage.clickButton(string);
    }

    @And("the user selects {string} radio button on the create account pop up")
    public void the_user_selects_radio_on_the_create_account_pop_up(String string) {
        applicationPage.clickRadioButton(string);
    }

    @And("the user clicks {string} button on the create account pop up")
    public void the_user_clicks_cta_on_the_create_account_pop_up(String string) {
        applicationPage.clickSubmitButton();
        applicationInfoPage = new ApplicantInformationPage(driver);
    }

    @And("the user fills application information page")
    public void the_user_fills_application_information() throws InterruptedException {
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
    public void the_user_fills_personal_information() throws InterruptedException {
        boolean below18 = false;
        personalInfoPage.fillInPersonalInfo(below18);
        contactInfoPage = personalInfoPage.submitPersonalInfo();

    }

    @And("the user fills contact information page")
    public void the_user_fills_contact_information() {
        contactInfoPage.fillContactInformation();
        employeeFinancialPage = contactInfoPage.submitContactInfo();
    }

    @And("the user fills employee & financial information page")
    public void the_user_fills_employee_financial_information() {
        employeeFinancialPage.fillEmployeeFinancial();
        tradingExperiencePage = employeeFinancialPage.submitEmployeeFinancial();
    }

    @And("the user fills trading experience page")
    public void the_user_fills_trading_experience() {
        tradingExperiencePage.fillTradeExperience();

    }

    @When("the user clicks {string} button on the trading experience page")
    public void the_user_clicks_button_on_trading_experience(String string) {
        tradingExperiencePage.submitTradeExperience(string);
    }

    @Then("the user sees a record in {string} status is created on the application list")
    public void the_user_sees_a_record_with_status_is_created_on_the_application_list(String string) throws InterruptedException {
        Thread.sleep(5000);
        Assert.assertEquals(applicationPage.getNewRecordEmail(), applicationInfoPage.retrieveSubmittedEmail());
        Assert.assertEquals(applicationPage.getNewRecordStatus(), string);
    }

    @When("the user clicks detail button of {string} record on the application page")
    public void the_user_clicks_detail_button_of_record_on_application(String status) {
        applicationInfoPage = applicationPage.clickActionButtonBasedOnStatus(status);
    }

    @And("the user clicks {string} button on the application information page")
    public void the_user_clicks_button_on_application_information(String buttonName) throws InterruptedException {
        Thread.sleep(5000);
        personalInfoPage = applicationInfoPage.clickNext();
    }

    @And("the user clicks {string} button on the personal information page")
    public void the_user_clicks_button_on_personal_information(String buttonName) {
        contactInfoPage = personalInfoPage.clickCTA(buttonName);

    }

    @And("the user clicks {string} button on the contact information page")
    public void the_user_clicks_button_on_contact_information(String buttonName) {
        contactInfoPage.clickCTA(buttonName);
        employeeFinancialPage = new EmployeeFinancialPage(driver);
    }

    @And("the user clicks {string} button on the employee & financial information page")
    public void the_user_clicks_button_on_employee_financial_information(String buttonName) {
        employeeFinancialPage.clickCTA(buttonName);
        tradingExperiencePage = new TradingExperiencePage(driver);
    }

    @And("the user clicks {string} button on L2 trading experience page")
    public void the_user_clicks_button_on_L2_trading_experience(String buttonName) {
        tradingExperiencePage.clickApprove();
    }

    @And("the user selects {string} as verify reason on the verify pop up")
    public void the_user_selects_verify_reason_on_the_verify_pop_up(String reason) {
        tradingExperiencePage.selectReason(reason);
    }

    @And("the user clicks {string} button on the verify pop up")
    public void the_user_clicks_on_the_verify_pop_up(String buttonName) {
        tradingExperiencePage.clickButtonOnVerify(buttonName);
    }

    @Then("the user sees a record in {string} status after approval")
    public void the_user_sees_a_record_with_status_after_first_approval(String status) throws InterruptedException {
        Assert.assertEquals(applicationPage.getEmailStatus(), status);
    }

    @And("the user fills {string} code {string} on application information page")
    public void the_user_fills_promotion_code_on_application_information_page(String type,String code)
    {
            applicationInfoPage.fillCode(type,code);

    }

    @Then("the user sees {string} error message displayed on application information page")
    public void the_user_sees_error_message_displayed_on_application_information_page(String error)
    {
        Assert.assertEquals(applicationInfoPage.errorValidation(),error);
    }

    @And("the user fills mandatory information on application information page")
    public void the_user_fills_mandatory_information_on_application_information_page()
    {
        applicationInfoPage.fillInApplicantInfo();

    }

    @And("the user lands on Application Information page")
    public void the_user_lands_on_Application_Information_page()
    {
        applicationPage.clickButton("Create Account");
        applicationPage.clickRadioButton("Individual");
        applicationPage.clickSubmitButton();
        applicationInfoPage = new ApplicantInformationPage(driver);
    }

    @And("the user fills existing {string} on application information page")
    public void the_user_fills_existing_phone_number_on_application_information_page(String info)
    {
        String phoneNumber = "96553209";
        String email = "eieuatapproved@yopmail.com";
        if (info.equalsIgnoreCase("phone")) {
            applicationInfoPage.fillExistingInfo(phoneNumber);
        }
        else if (info.equalsIgnoreCase("email"))
        {
            applicationInfoPage.fillExistingInfo(email);
        }
    }

    @And("the user fills mandatory information on personal information page")
    public void the_user_fills_mandatory_information_on_personal_information_page() throws InterruptedException {
        boolean below18 = false;
        personalInfoPage.fillInPersonalInfo(below18);
    }

    @And("the user fills mandatory information with DOB below 18 on personal information page")
    public void the_user_fills_mandatory_information_with_DOB_below_18_on_personal_information_page() throws InterruptedException {
        boolean below18 = true;
        personalInfoPage.fillInPersonalInfo(below18);

    }

    @Then("the user sees {string} error message displayed on personal information page")
    public void the_user_sees_error_message_displayed_on_personal_information_page(String errorText)
    {
        Assert.assertEquals(personalInfoPage.errorValidation(),errorText);
    }

    @And("the user fills expiry date {string} than current date")
    public void the_user_fills_expiry_date(String condition) throws InterruptedException {
        Thread.sleep(3000);
        int days;
        if (condition.equalsIgnoreCase("later")) {
            days = 1;
            personalInfoPage.selectExpiryDate(days);
        }
        else {
            days = -1;
            personalInfoPage.selectExpiryDate(days);
        }
    }


}
