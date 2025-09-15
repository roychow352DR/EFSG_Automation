package StepDefinitions.AdminPortal.aoApplicationSteps;

import Data.SQLDatabase;
import PageObject.AdminPortal.*;
import PageObject.AdminPortalPW.AOPOManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en_scouse.An;
import org.checkerframework.checker.units.qual.A;
import org.testng.Assert;
import utils.BaseTest;
import utils.SetCondition;

import java.io.IOException;
import java.sql.SQLException;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ApplicationSteps extends BaseTest {
    public AdminLoginPage login;
    public ApplicationListPage applicationPage;
    public ApplicantInformationPage applicationInfoPage;
    public PersonalInfoPage personalInfoPage;
    public ContactInfoPage contactInfoPage;
    public EmployeeFinancialPage employeeFinancialPage;
    public TradingExperiencePage tradingExperiencePage;
    public static AOPOManager aopoManager;
    public SQLDatabase sqlDb = new SQLDatabase();


    @Given("the user logged in to Admin Portal as username {string} and password {string}")
    public void the_user_logged_in_to_Admin_Portal(String username, String password) throws IOException, SQLException {
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
        aopoManager.getApplicationInfoPage().fillApplicationInfo(SetCondition.isExistedEmail(),
                SetCondition.isExistedPhoneNumber(),
                SetCondition.isCrossEntity());
    }

    @And("the user fills personal information page")
    public void the_user_fills_personal_information() throws IOException {
        aopoManager.getPersonalInfoPage().fillPersonalInfo(SetCondition.isBelow18(),
                SetCondition.isExpired(),
                SetCondition.isExpiredBeforeCurrent(),
                SetCondition.isEdd());

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
        aopoManager.getTradingExpPage().clickButtonByText(buttonName);
    }

    @Then("the user sees a record in {string} status is created on the application list")
    public void the_user_sees_a_record_with_status_is_created_on_the_application_list(String status) throws InterruptedException {
        assertThat(aopoManager.getApplicationListPage().getApplicationStatus(aopoManager.getApplicationInfoPage().submittedApplicantEmail())).hasText(status);
    }

    @When("the user clicks detail button of {string} record on the application page")
    public void the_user_clicks_detail_button_of_record_on_application(String status) throws IOException {
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
        aopoManager.getApplicationInfoPage().fillMandatory(SetCondition.isExistedEmail(),
                SetCondition.isExistedPhoneNumber(),
                SetCondition.isCrossEntity());
    }

    @And("the user lands on Application Information page")
    public void the_user_lands_on_Application_Information_page() {
        applicationPage.clickButton("Create Account");
        applicationPage.clickRadioButton("Individual");
        applicationPage.clickSubmitButton();
        applicationInfoPage = new ApplicantInformationPage(driver);
    }


    @And("the user submits mandatory information on personal information page")
    public void the_user_submits_mandatory_information_with_on_personal_information_page() throws IOException {
        aopoManager.getPersonalInfoPage().fillPersonalInfo(SetCondition.isBelow18(),
                SetCondition.isExpired(),
                SetCondition.isExpiredBeforeCurrent(),
                SetCondition.isEdd());
    }

    @Then("the user sees {string} error message displayed on personal information page")
    public void the_user_sees_error_message_displayed_on_personal_information_page(String errorText) {
        assertThat(aopoManager.getPersonalInfoPage().errorValidation()).hasText(errorText);
    }

    @And("the user fills expiry date {string} than current date")
    public void the_user_fills_expiry_date(String condition) throws InterruptedException, IOException {
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

    @When("the user submits mandatory information on application information page")
    public void the_user_submits_mandatory_information_with_on_application_information_page() throws IOException {
        System.out.println(SetCondition.isExistedEmail());
        aopoManager.getApplicationInfoPage().fillApplicationInfo(SetCondition.isExistedEmail(),
                SetCondition.isExistedPhoneNumber(),
                SetCondition.isCrossEntity());
        //aopoManager.getApplicationInfoPage().clickNext();

    }

    @And("the user selects {string} as reason on the verify reason pop up")
    public void the_user_selects_as_reason_on_the_verify_reason_pop_up(String reason) {
        aopoManager.getApplicationInfoPage().selectReason(reason);
    }

    @When("the user clicks {string} on the menu")
    public void the_user_clicks_on_the_menu(String menu) {
        aopoManager.getMenuPagePW().clickMenu(menu);
    }

    @And("the user fills mandatory information on personal information page")
    public void the_user_fills_mandatory_information_on_personal_information_page() throws IOException {
        aopoManager.getPersonalInfoPage().fillMandatory(SetCondition.isBelow18(),
                SetCondition.isExpired(),
                SetCondition.isExpiredBeforeCurrent(),
                SetCondition.isEdd());
    }

    @And("the user uncheck {string} checkbox on personal information page")
    public void the_user_uncheck_checkbox_on_personal_information_page(String label) {
        aopoManager.getPersonalInfoPage().uncheckBox(label);
    }

    @Then("the user sees {string} label is displayed on application information page")
    public void the_user_sees_label_is_displayed_on_application_information_page(String labelName) {
        assertThat(aopoManager.getApplicationInfoPage().getLabel(labelName)).hasText(labelName);
    }

    @And("the user fills blacklisted firstname {string} and lastname {string} on personal information page")
    public void the_user_fills_blacklisted_firstname_and_lastname_on_personal_information_page(String firstname, String lastName) {
        aopoManager.getPersonalInfoPage().fillBlacklistName(firstname, lastName);
    }

    @And("the user fills id {string} on personal information page")
    public void the_user_fills_blacklisted_id_on_personal_information_page(String id) throws IOException {
        aopoManager.getPersonalInfoPage().fillSpecificId(id);
    }

    @And("the user select {string} as ID Type on personal information page")
    public void the_user_select_as_ID_Type_on_personal_information_page(String idType) {
        aopoManager.getPersonalInfoPage().selectIdType(idType);
    }

    @Then("the user sees {string} button on application information page")
    public void the_user_sees_button_on_application_information_page(String buttonName) {
        assertThat(aopoManager.getApplicationInfoPage().getButtonByText(buttonName)).isVisible();
    }

    @Then("the user sees an existing record is updated to {string} status on the application list")
    public void the_user_sees_an_existing_record_is_updated_to_status_on_the_application_list(String status) throws IOException {
        assertThat(aopoManager.getApplicationListPage().getApplicationStatus(aopoManager.getApplicationListPage().getStatusEmail(status))).hasText(status);
    }

    @Then("the {string} account record is retrieved in CM database")
    public void the_account_record_is_retrieved_in_CM_database(String status) throws SQLException, IOException {
        Assert.assertNotNull(sqlDb.getPersonProfileId(aopoManager.getApplicationListPage().getStatusEmail(status)));
    }

    @And("the user sees a record in {string} status on the application list")
    public void the_user_sees_a_record_in_status_on_the_application_list(String status) throws IOException {
        Assert.assertNotNull(aopoManager.getApplicationListPage().getStatusEmail(status));
    }

    @Then("the user sees {string} label is not displayed on application information page")
    public void the_user_sees_label_is_not_displayed_on_application_information_page(String labelName) {
        assertThat(aopoManager.getApplicationInfoPage().getLabel(labelName)).isHidden();
    }

    @And("the user fills mandatory information on contact information page")
    public void the_user_fills_mandatory_information_on_contact_information_page() throws IOException {
        aopoManager.getContactInfoPage().fillMandatory();
    }

    @Given("the {string} condition is satisfied")
    public void the_condition_is_satisfied(String condition) {

        switch (condition) {
            case "DOB Below 18" -> isBelow18 = true;
            case "Exist Email" -> isExistedEmail = true;
            case "Exist Phone Number" -> isExistedPhoneNumber = true;
            case "Expired date" -> isExpired = true;
            case "EDD" -> isEdd = true;
            case "Expired date before current date" -> isExpiredBeforeCurrent = true;
            case "Cross Entity" -> isCrossEntity = true;
        }
        new SetCondition(isExistedEmail, isExistedPhoneNumber, isBelow18, isExpired, isEdd, isExpiredBeforeCurrent, isCrossEntity);
    }

    @And("the user selects entity {string} on the application information page")
    public void the_user_selects_entity_on_the_application_information_page(String entity) throws IOException {
        aopoManager.getApplicationInfoPage().selectEntity(isCrossEntity, entity);
    }

    @And("the user fills value {string} in the text field {string} on application information page")
    public void the_user_fills_value_in_the_text_field_on_application_information_page(String value,String textFieldName) throws IOException {
        if (textFieldName.equalsIgnoreCase("username")) {
            aopoManager.getApplicationInfoPage().fillUsername(value);
        }
        else if (textFieldName.equalsIgnoreCase("email")){
            aopoManager.getApplicationInfoPage().fillEmail(value);
        }
        else if (textFieldName.equalsIgnoreCase("mobileNumber")){
            aopoManager.getApplicationInfoPage().fillPhoneNumber(value);
        }
    }

    @Then("the user sees an error dialogue with wordings {string} on the application information page")
    public void the_user_sees_an_error_dialogue_with_wordings_on_the_application_information_page(String errorText) {
        Assert.assertEquals(aopoManager.getApplicationInfoPage().getToastMsg().textContent(), errorText);
    }

    @Then("the text field {string} is not editable")
    public void the_text_field_is_not_editable(String textFieldName) {
        assertThat(aopoManager.getApplicationInfoPage().getTextField(textFieldName)).isDisabled();
    }

    @Then("the text field {string} is editable")
    public void the_text_field_is_editable(String textFieldName) {
        assertThat(aopoManager.getApplicationInfoPage().getTextField(textFieldName)).isEnabled();
    }

    @And("the user fills username with digits number {int} on application information page")
    public void the_user_fills_username_with_digits_number_on_application_information_page(int length) throws IOException {
        aopoManager.getApplicationInfoPage().fillRandomUsername(length);
    }

    @And("the user fills full width username with digits number {int} on application information page")
    public void the_user_fills_full_width_username_with_digits_number_on_application_information_page(int length) throws IOException {
        aopoManager.getApplicationInfoPage().fillFullWidthRandomUsername(length);
    }

    @And("the record in status {string} is created in the application list")
    public void the_record_in_status_is_created_in_the_application_list(String status) throws IOException, InterruptedException {
        aopoManager.getApplicationListPage().createIndividual();
        aopoManager.getApplicationInfoPage().fillApplicationInfo(SetCondition.isExistedEmail(),
                SetCondition.isExistedPhoneNumber(),
                SetCondition.isCrossEntity());
        if (status.equalsIgnoreCase("Draft")) {
            aopoManager.getMenuPagePW().clickMenu("AO Application List");
            return;
        }
            aopoManager.getPersonalInfoPage().fillPersonalInfo(SetCondition.isBelow18(),
                    SetCondition.isExpired(),
                    SetCondition.isExpiredBeforeCurrent(),
                    SetCondition.isEdd());
            aopoManager.getContactInfoPage().fillContactInfo();
            aopoManager.getEmployeeFinInfoPage().fillEmployeeFinInfo();
            aopoManager.getTradingExpPage().fillTradingExp();
            aopoManager.getTradingExpPage().clickButtonByText("Submit");
         if (status.equalsIgnoreCase("Rejected")) {
            aopoManager.getApplicationListPage().clickDetailBtn("Pending Verification");
            aopoManager.getApplicationInfoPage().clickButtonByText("Next To Personal Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
            aopoManager.getPersonalInfoPage().clickButtonByText("Next To Employee and Financial Information");
            aopoManager.getEmployeeFinInfoPage().clickButtonByText("Next To Trading Experience");
            aopoManager.getTradingExpPage().clickButtonByText("Reject");
            aopoManager.getTradingExpPage().selectRejectReason("ID/Passport No. match with the EDD/AML list");
            aopoManager.getTradingExpPage().clickButtonByText("Confirm");
        }
    }

    @And("the user fills textField {string} retrieved from api endpoint on the application information page")
    public void the_user_fills_textField_retrieved_from_api_endpoint_on_the_application_information_page(String textFieldName){
        System.out.println(getRetrievedData());
        if (textFieldName.equalsIgnoreCase("username")){
            aopoManager.getApplicationInfoPage().fillUsername(getRetrievedData());
        }
    }

    @When("the user logout Admin Portal")
    public void the_user_logout_Admin_Portal(){
        aopoManager.getMenuPagePW().clickLogout();
        page.waitForTimeout(500);
    }

    @And("the user re-logged in to Admin Portal as username {string} and password {string}")
    public void the_user_re_logged_in_to_Admin_Portal_as_username_and_password(String username, String password){
        aopoManager.getAdminLoginPage().loginETE(username, password);
     //   page.pause();
        assertThat(aopoManager.getApplicationListPage().getMenuText()).isVisible();
    }


}
