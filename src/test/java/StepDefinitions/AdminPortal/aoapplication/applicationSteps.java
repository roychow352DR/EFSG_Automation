package StepDefinitions.AdminPortal.aoapplication;

import PageObject.AdminPortal.*;
import PageObject.AdminPortalPW.AOPOManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utils.BaseTest;

import java.io.IOException;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class applicationSteps extends BaseTest {
    public AdminLoginPage login;
    public ApplicationListPage applicationPage;
    public ApplicantInformationPage applicationInfoPage;
    public PersonalInfoPage personalInfoPage;
    public ContactInfoPage contactInfoPage;
    public EmployeeFinancialPage employeeFinancialPage;
    public TradingExperiencePage tradingExperiencePage;
    public AOPOManager aopoManager;
    public boolean isExistedEmail = false;
    public boolean isExistedPhoneNumber = false;
    public boolean isBelow18 = false;
    public boolean isExpired = false;


    @Given("the user logged in to Admin Portal as username {string} and password {string}")
    public void the_user_logged_in_to_Admin_Portal(String username, String password) throws IOException, InterruptedException {
        page = initializePage();
        aopoManager = new AOPOManager(page);
        aopoManager.getAdminLoginPage().loginETE(username, password);
        assertThat(aopoManager.getApplicationListPage().getMenuText()).isVisible();
    }

    @And("the user clicks {string} button on the application page")
    public void the_user_clicks_button_on_the_application_page(String buttonName) {
        aopoManager.getApplicationListPage().clickButton(buttonName);
    }

    @And("the user selects {string} radio button on the create account pop up")
    public void the_user_selects_radio_on_the_create_account_pop_up(String radioBtnLabel) {
        aopoManager.getApplicationListPage().clickRadioButton(radioBtnLabel);
    }

    @And("the user clicks {string} button on the create account pop up")
    public void the_user_clicks_cta_on_the_create_account_pop_up(String buttonName) {
        aopoManager.getApplicationListPage().clickBtnOnCreate(buttonName);
    }

    @And("the user fills application information page")
    public void the_user_fills_application_information() throws IOException {
        aopoManager.getApplicationInfoPage().fillApplicationInfo(isExistedEmail,isExistedPhoneNumber);
    }

    @And("the user fills personal information page")
    public void the_user_fills_personal_information() throws IOException {
        aopoManager.getPersonalInfoPage().fillPersonalInfo(isBelow18,isExpired);

    }

    @And("the user fills contact information page")
    public void the_user_fills_contact_information() throws IOException {
        aopoManager.getContactInfoPage().fillContactInfo();
    }

    @And("the user fills employee & financial information page")
    public void the_user_fills_employee_financial_information() throws IOException {
        aopoManager.getEmployeeFinInfoPage().fillEmployeeFinInfo();
    }

    @And("the user fills trading experience page")
    public void the_user_fills_trading_experience() throws IOException {
        aopoManager.getTradingExpPage().fillTradingExp();

    }

    @When("the user clicks {string} button on the trading experience page")
    public void the_user_clicks_button_on_trading_experience(String buttonName) {
        aopoManager.getTradingExpPage().submitApplication(buttonName);
    }

    @Then("the user sees a record in {string} status is created on the application list")
    public void the_user_sees_a_record_with_status_is_created_on_the_application_list(String status) throws InterruptedException {
        assertThat(aopoManager.getApplicationListPage().getApplicationStatus(aopoManager.applicationInfoPagePW.submittedApplicantEmail())).hasText(status);
    }

    @When("the user clicks detail button of {string} record on the application page")
    public void the_user_clicks_detail_button_of_record_on_application(String status) {
        aopoManager.getApplicationListPage().clickDetailBtn(status);
    }

    @And("the user clicks {string} button on the application information page")
    public void the_user_clicks_button_on_application_information(String buttonName) throws InterruptedException {
        aopoManager.applicationInfoPagePW.clickButtonByText(buttonName);
    }

    @And("the user clicks {string} button on the personal information page")
    public void the_user_clicks_button_on_personal_information(String buttonName) {
        aopoManager.personalInfoPagePW.clickButtonByText(buttonName);

    }

    @And("the user clicks {string} button on the contact information page")
    public void the_user_clicks_button_on_contact_information(String buttonName) {
        aopoManager.getContactInfoPage().clickButtonByText(buttonName);
    }

    @And("the user clicks {string} button on the employee & financial information page")
    public void the_user_clicks_button_on_employee_financial_information(String buttonName) {
        aopoManager.getEmployeeFinInfoPage().clickButtonByText(buttonName);
    }

    @And("the user clicks {string} button on L2 trading experience page")
    public void the_user_clicks_button_on_L2_trading_experience(String buttonName) {
        tradingExperiencePage.clickApprove();
    }

    @And("the user selects {string} as verify reason on the verify pop up")
    public void the_user_selects_verify_reason_on_the_verify_pop_up(String reason) {
        aopoManager.getTradingExpPage().selectReason(reason);
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
    public void the_user_fills_promotion_code_on_application_information_page(String type, String code) {
        if (type.equalsIgnoreCase("promo")) {
            aopoManager.getApplicationInfoPage().fillPromoCode(code);
        } else if (type.equalsIgnoreCase("referral")) {
            aopoManager.getApplicationInfoPage().fillReferralCode(code);
        }

    }

    @Then("the user sees {string} error message displayed on application information page")
    public void the_user_sees_error_message_displayed_on_application_information_page(String error) {
       // assertThat(aopoManager.getApplicationInfoPage().getToastMsg()).hasText(error);
        assertThat(aopoManager.getApplicationInfoPage().errorValidation()).hasText(error);
    }

    @And("the user fills mandatory information on application information page")
    public void the_user_fills_mandatory_information_on_application_information_page() throws IOException {
        aopoManager.getApplicationInfoPage().fillMandatory(isExistedEmail,isExistedPhoneNumber);

    }

    @And("the user lands on Application Information page")
    public void the_user_lands_on_Application_Information_page() {
        applicationPage.clickButton("Create Account");
        applicationPage.clickRadioButton("Individual");
        applicationPage.clickSubmitButton();
        applicationInfoPage = new ApplicantInformationPage(driver);
    }


    @And("the user submits mandatory information with {string} on personal information page")
    public void the_user_submits_mandatory_information_with_on_personal_information_page(String condition) throws IOException {
        if (condition.equalsIgnoreCase("DOB below 18")) {
            isBelow18 = true;
        }
        else if (condition.equalsIgnoreCase("Expired date")) {
            isExpired = true;
        }
        aopoManager.getPersonalInfoPage().fillPersonalInfo(isBelow18,isExpired);

    }

    @Then("the user sees {string} error message displayed on personal information page")
    public void the_user_sees_error_message_displayed_on_personal_information_page(String errorText) {
        assertThat(aopoManager.getPersonalInfoPage().errorValidation()).hasText(errorText);
    }

    @And("the user fills expiry date {string} than current date")
    public void the_user_fills_expiry_date(String condition) throws InterruptedException {
        Thread.sleep(3000);
        int days;
        if (condition.equalsIgnoreCase("later")) {
            days = 1;
            personalInfoPage.selectExpiryDate(days);
        } else {
            days = -1;
            personalInfoPage.selectExpiryDate(days);
        }
    }

    @When("the user submits mandatory information with {string} on application information page")
    public void the_user_submits_mandatory_information_with_on_application_information_page(String condition) throws IOException {
        if (condition.equalsIgnoreCase("Exist email"))
        {
            isExistedEmail = true;
        }
        else if (condition.equalsIgnoreCase("Exist phoneNumber"))
        {
            isExistedPhoneNumber = true;
        }
        aopoManager.getApplicationInfoPage().fillApplicationInfo(isExistedEmail,isExistedPhoneNumber);
        //aopoManager.getApplicationInfoPage().clickNext();

    }



}
