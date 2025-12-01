package StepDefinitions.AdminPortal.aoApplicationSteps;

import API.CoreService;
import Data.AoAccountCreation;
import Data.SQLDatabase;
import PageObject.AdminPortal.*;
import PageObject.AdminPortalPW.AOPOManager;
import com.microsoft.playwright.options.LoadState;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utils.BaseTest;
import utils.SetCondition;

import java.io.IOException;
import java.sql.SQLException;

import static PageObject.AdminPortalPW.ApplicationInfoPagePW.applicantEmail;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static utils.SetCondition.isThirdParty;


public class ApplicationSteps extends BaseTest {
    public AdminLoginPage login;
    public ApplicationListPage applicationPage;
    public ApplicantInformationPage applicationInfoPage;
    public PersonalInfoPage personalInfoPage;
    public ContactInfoPage contactInfoPage;
    public EmployeeFinancialPage employeeFinancialPage;
    public TradingExperiencePage tradingExperiencePage;
    public CoreService coreService;
    public SQLDatabase sqlDb ;
    public static AoAccountCreation accountAction;

    public void objectInit() throws IOException {
        aopoManager = new AOPOManager(page);
        coreService = new CoreService(page, productEnv);
        sqlDb = new SQLDatabase();
        accountAction = new AoAccountCreation(aopoManager);
    }


    @Given("the user logged in to Admin Portal as username {string} and password {string}")
    public void the_user_logged_in_to_Admin_Portal(String username, String password) throws IOException, SQLException {
        page = initializePage();
        objectInit();
        aopoManager.getAdminLoginPage().loginETE(username, password);
        page.waitForLoadState(LoadState.NETWORKIDLE);
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
        setRetrievedData(aopoManager.getApplicationInfoPage().submittedApplicantEmail());
    }

    @And("the user fills personal information page")
    public void the_user_fills_personal_information() throws IOException {
        aopoManager.getPersonalInfoPage().fillPersonalInfo(SetCondition.isBelow18(),
                SetCondition.isExpired(),
                SetCondition.isExpiredBeforeCurrent(),
                SetCondition.isEdd(),
                SetCondition.isThirdParty());

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
        assertThat(aopoManager.getApplicationListPage().getApplicationStatus(aopoManager.getApplicationInfoPage().submittedApplicantEmail(),
                aopoManager.getApplicationListPage().getFirstRow())).hasText(status);
    }

    @When("the user clicks detail button of {string} record on the application page")
    public void the_user_clicks_detail_button_of_record_on_application(String status) throws IOException {
        aopoManager.getApplicationListPage().clickDetailBtn(status);
    }

    @And("the user clicks {string} button on the application information page")
    public void the_user_clicks_button_on_application_information(String buttonName) throws InterruptedException {
        aopoManager.applicationInfoPagePW.clickButtonByText(buttonName);
        //page.waitForTimeout(5000);
        page.waitForLoadState(LoadState.NETWORKIDLE);
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
                SetCondition.isEdd(),
                SetCondition.isThirdParty());
    }

    @Then("the user sees {string} error message displayed on personal information page")
    public void the_user_sees_error_message_displayed_on_personal_information_page(String errorText) {
        assertThat(aopoManager.getPersonalInfoPage().errorValidation()).hasText(errorText);
    }

    @And("the user fills expiry date {string} than current date")
    public void the_user_fills_expiry_date(String condition) throws InterruptedException, IOException {
        page.waitForTimeout(3000);
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
        aopoManager.getApplicationInfoPage().fillApplicationInfo(SetCondition.isExistedEmail(),
                SetCondition.isExistedPhoneNumber(),
                SetCondition.isCrossEntity());
        //aopoManager.getApplicationInfoPage().clickNext();

    }

    @And("the user selects {string} as reason on the verify reason pop up on application information page")
    public void the_user_selects_as_reason_on_the_verify_reason_pop_up_on_application_information_page(String reason) {
        aopoManager.getApplicationInfoPage().selectReason(reason);
    }

    @When("the user clicks {string} on the ao admin portal menu")
    public void the_user_clicks_on_the__ao_admin_portal_menu(String menu) {
        aopoManager.getMenuPagePW().clickMenu(menu);
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    @And("the user fills mandatory information on personal information page")
    public void the_user_fills_mandatory_information_on_personal_information_page() throws IOException {
        aopoManager.getPersonalInfoPage().fillMandatory(SetCondition.isBelow18(),
                SetCondition.isExpired(),
                SetCondition.isExpiredBeforeCurrent(),
                SetCondition.isEdd(),
                SetCondition.isThirdParty());
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
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertThat(aopoManager.getApplicationListPage().getApplicationStatus(aopoManager.getApplicationInfoPage().submittedApplicantEmail(),
                aopoManager.getApplicationListPage().getFirstRow())).hasText(status);
    }

    @Then("the {string} account record is retrieved in CM database")
    public void the_account_record_is_retrieved_in_CM_database(String status) throws SQLException, IOException {
        Assert.assertNotNull(sqlDb.getPersonProfileId(aopoManager.getApplicationListPage().email));
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
            case "3rd Party" -> isThirdParty = true;
        }
        new SetCondition(isExistedEmail, isExistedPhoneNumber, isBelow18, isExpired, isEdd, isExpiredBeforeCurrent, isCrossEntity, isThirdParty);
    }

    @And("the user selects entity {string} on the application information page")
    public void the_user_selects_entity_on_the_application_information_page(String entity) throws IOException {
        aopoManager.getApplicationInfoPage().selectEntity(isCrossEntity, entity);
    }

    @And("the user fills value {string} in the text field {string} on application information page")
    public void the_user_fills_value_in_the_text_field_on_application_information_page(String value, String textFieldName) throws IOException {
        if (textFieldName.equalsIgnoreCase("username")) {
            aopoManager.getApplicationInfoPage().fillUsername(value);
        } else if (textFieldName.equalsIgnoreCase("email")) {
            aopoManager.getApplicationInfoPage().fillEmail(value);
        } else if (textFieldName.equalsIgnoreCase("mobileNumber")) {
            aopoManager.getApplicationInfoPage().fillPhoneNumber(value);
        } else {
            aopoManager.getApplicationInfoPage().fillTextFieldVal(value, textFieldName);
        }

    }

    @And("the user fills entity IB Code in the text field {string} on application information page")
    public void the_user_fills_entity_IB_Code_the_text_field_on_application_information_page(String textFieldName) {
        aopoManager.getApplicationInfoPage().fillTextFieldVal(aopoManager.getApplicationInfoPage().getIbCode(productEntity), textFieldName);
    }

    @Then("the user sees an error dialogue with wordings {string} on the application information page")
    public void the_user_sees_an_error_dialogue_with_wordings_on_the_application_information_page(String errorText) {
        Assert.assertEquals(aopoManager.getApplicationInfoPage().getToastMsg().textContent(), errorText);
    }

    @Then("the user sees text field {string} is not editable on the application information page")
    public void the_user_sees_text_field_is_not_editable_on_the_application_information_page(String textFieldName) {
        assertThat(aopoManager.getApplicationInfoPage().getTextField(textFieldName)).isDisabled();
    }

    @Then("the user sees text field {string} is editable on the application information page")
    public void the_user_sees_text_field_is_editable_on_the_application_information_page(String textFieldName) {
        assertThat(aopoManager.getApplicationInfoPage().getTextField(textFieldName)).isEditable();
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
        if (status.equalsIgnoreCase("Draft")) {
            accountAction.createL2DraftIndividual();
        }
        else if (status.equalsIgnoreCase("Rejected")) {
            accountAction.createL2RejectedIndividual();
        }
        else if (status.equalsIgnoreCase("Pending Verification")) {
            accountAction.createL2AccountIndividual();
        }
        else if (status.equalsIgnoreCase("Pending Deposit")) {
            accountAction.createL3AccountIndividual();
        }

    }

    @And("the user fills textField {string} retrieved from api endpoint on the application information page")
    public void the_user_fills_textField_retrieved_from_api_endpoint_on_the_application_information_page(String textFieldName) {
        if (textFieldName.equalsIgnoreCase("username")) {
            aopoManager.getApplicationInfoPage().fillUsername(getRetrievedData());
        }
    }

    @When("the user logout Admin Portal")
    public void the_user_logout_Admin_Portal() {
        aopoManager.getMenuPagePW().clickLogout();
        page.waitForTimeout(1000);
    }

    @And("the user re-logged in to Admin Portal as username {string} and password {string}")
    public void the_user_re_logged_in_to_Admin_Portal_as_username_and_password(String username, String password) {
        aopoManager.getAdminLoginPage().loginETE(username, password);
        //assertThat(aopoManager.getApplicationListPage().getMenuText()).isVisible();
    }

    @When("the user edit text field {string} on the trading experience page")
    public void the_user_edit_text_field_on_the_trading_experience_page(String textFieldName) {
        String editVal = aopoManager.getTradingExpPage().editDropdownVal(textFieldName);
        setRetrievedData(editVal);
    }

    @Then("the user sees text field {string} value is updated on the trading experience page")
    public void the_user_sees_text_field_value_is_updated_on_the_trading_experience_page(String textFieldName) {
        page.waitForTimeout(500);
        Assert.assertEquals(aopoManager.getTradingExpPage().getFieldValByLabel(textFieldName), getRetrievedData());
    }

    @And("the user clicks detail button of modified record on the application page")
    public void the_user_clicks_detail_button_of_modified_record_on_the_application_page() {
        aopoManager.getApplicationListPage().clickDetailBtn();
    }

    @Then("the user sees text field {string} is not editable on the trading experience page")
    public void the_user_sees_text_field_is_not_editable_on_the_trading_experience_page(String textFieldName) {
        assertThat(aopoManager.getTradingExpPage().getDropdown(textFieldName)).isDisabled();
    }

    @And("the user selects dropdown value {string} for the dropdown field {string} on the trading experience page")
    public void the_user_selects_dropdown_value_for_the_dropdown_field_on_the_trading_experience_page(String dropdownVal, String dropdownFieldName) {
        aopoManager.getTradingExpPage().selectDropdownOption(dropdownVal, dropdownFieldName);
    }

    @Then("the user sees dropdown value {string} for the dropdown field {string} on the trading experience page")
    public void the_user_sees_dropdown_value_for_the_dropdown_field_on_the_trading_experience_page(String dropdownVal, String dropdownFieldName) {
        assertThat(aopoManager.getTradingExpPage().getDropdown(dropdownFieldName)).hasValue(dropdownVal);
    }

    @And("the user clicks detail button of newly created record with account type {string} on the application page")
    public void the_user_clicks_detail_button_of_newly_created_record_on_the_application_page(String accountType) {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        if (accountType.equalsIgnoreCase("Individual")) {
            aopoManager.getApplicationListPage().clickClientRecordDetailBtn(aopoManager.getApplicationInfoPage().submittedApplicantEmail());
        } else {
            aopoManager.getApplicationListPage().clickClientRecordDetailBtn(aopoManager.getCompanyAccountPagePW().submittedApplicantEmail());
        }
    }

    @And("the user fills value {string} in the text field {string} on the application filter dialogue")
    public void the_user_fills_value_in_the_text_field_on_the_application_filter_dialogue(String filterVal, String filerField) {
        aopoManager.getApplicationListPage().inputFilterValue(filterVal, filerField);
    }

    @Then("the application list displays {string} in the {string} column as a filtered result")
    public void the_application_list_displays_in_the_column_as_a_filtered_result(String result, String column) {
        Assert.assertTrue((aopoManager.getApplicationListPage().filteredVal(column, result)));
    }

    @When("the user fills value {string} on the application search field")
    public void the_user_fills_value_in_the_text_field_on_the_application_search_field(String searchVal) {
        page.waitForTimeout(2000);
        aopoManager.getApplicationListPage().fillSearchVal(searchVal);
        page.keyboard().press("Enter");
    }

    @Then("the user sees value {string} is displayed at the text field {string} on the application information page")
    public void the_user_sees_value_is_displayed_at_the_text_field_on_the_application_information_page(String value, String textField) {
        page.waitForTimeout(2000);
        Assert.assertEquals(aopoManager.getApplicationInfoPage().getTextField(textField).inputValue(), value);
    }

    @And("the user clicks detail button of app client on the application page")
    public void the_user_clicks_detail_button_of_app_client_on_the_application_page() {
        aopoManager.getApplicationListPage().clickClientRecordDetailBtn(getRetrievedData());
    }

    @Then("the user sees text field {string} displayed expected value as trade group info {string} obtain from eCRM on the application information page")
    public void the_user_sees_text_field_displayed_expected_value_as_trade_group_info_obtain_from_eCRM_on_the_application_information_page(String textFieldName, String tradeGroupInfo) throws IOException {
        page.waitForTimeout(2000);
        Assert.assertEquals(aopoManager.getApplicationInfoPage().getTextField(textFieldName).inputValue(),
                coreService.getTradeGroupInfo(tradeGroupInfo, retrieveLocalStorageVal()));
    }

    @Then("the user sees text field {string} displayed expected value as entity trade group info {string} obtain from eCRM on the application information page")
    public void the_user_sees_text_field_displayed_expected_value_as_entity_trade_group_info_obtain_from_eCRM_on_the_application_information_page(String textFieldName, String tradeGroupInfo) throws IOException {
        page.waitForTimeout(2000);
        Assert.assertEquals(aopoManager.getApplicationInfoPage().getTextField(textFieldName).inputValue(),
                coreService.getTradeGroupInfoBasedOnEntity(tradeGroupInfo, retrieveLocalStorageVal(), productEntity));
    }


    @And("the user fills reusable data in the text field {string} on the application information page")
    public void the_user_fills_reusable_data_in_the_text_field_on_the_application_information_page(String textFieldName) {
        aopoManager.getApplicationInfoPage().fillTextFieldVal(getOriginData(), textFieldName);
    }

    @And("the user clicks {string} button on the create company account page")
    public void the_user_clicks_button_on_create_company_account(String buttonName) {
        aopoManager.getCompanyAccountPagePW().clickButtonByText(buttonName);
    }

    @And("the user clicks {string} button on the create company account pop up")
    public void the_user_clicks_button_on_the_create_company_account_pop_up(String buttonName) {
        aopoManager.getCompanyAccountPagePW().clickButtonByText(buttonName);
    }

    @And("the user fill mandatory information on create company account page")
    public void the_user_fill_mandatory_information_on_create_company_account_page() throws IOException {
        aopoManager.getCompanyAccountPagePW().fillMandatory(SetCondition.isExistedEmail(), SetCondition.isExistedPhoneNumber(), SetCondition.isBelow18());

    }

    @Then("the user sees title {string} is displayed at the company account detail page")
    public void the_user_sees_title_is_displayed_at_the__company_account_detail_page(String titleName) {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertThat(aopoManager.getCompanyAccountPagePW().getTitle()).containsText(titleName);
    }

    @Then("the user sees status {string} is displayed at the company account detail page")
    public void the_user_sees_status_is_displayed_at_the__company_account_detail_page(String status) {
        assertThat(aopoManager.getCompanyAccountPagePW().getAccountStatus()).hasText(status);
    }

    @When("the user clicks the detail button for the application record with status {string}, created by {string}, and client type {string} on the application list page")
    public void the_user_clicks_detail_button_of_status_record_with_client_type_on_the_application_list_page(String status, String createdBy, String clientType) throws IOException {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        String email = coreService.getAoClient(retrieveLocalStorageVal(), "email", "statusLabel", status, createdBy, clientType);
        Assert.assertNotNull(email);
        aopoManager.getApplicationListPage().clickClientRecordDetailBtn(email);
        applicantEmail = email;
        page.waitForTimeout(2000);
    }

    @Then("the user sees button {string} is disabled on the trading experience page")
    public void the_user_sees_button_is_disabled_on_the_trading_experience_page(String buttonText) {
        assertThat(aopoManager.getTradingExpPage().getButton(buttonText)).isDisabled();
    }

    @Then("the user sees button {string} is hidden on the application list page")
    public void the_user_sees_button_is_hidden_on_the_application_list_page(String buttonText) {
        assertThat(aopoManager.getApplicationListPage().getButton(buttonText)).isHidden();
    }

    @Then("the user sees {string} message pop up on the ao login page")
    public void the_user_sees_message_pop_up_on_the_ao_login_page(String message) {
        assertThat(aopoManager.getAdminLoginPage().loginErrorValidation(message)).hasText(message);
    }

    @Then("the user sees profile name {string} is displayed on the ao admin portal menu")
    public void the_user_sees_profile_name_is_displayed_on_the_ao_admin_portal_menu(String profileName) {
        assertThat(aopoManager.getMenuPagePW().getProfile()).hasText(profileName);
    }

    @And("the user fills age {string} on the personal information page")
    public void the_user_fills_age_on_the_personal_information_page(String age) throws IOException {
        page.waitForTimeout(1000);
        aopoManager.getPersonalInfoPage().fillDob(age);
    }

    @And("the user clicks entity checkbox on the application filter dialogue")
    public void the_user_clicks_entity_checkbox_on_the_application_filter_dialogue() {
        aopoManager.getApplicationListPage().clickEntityCheckbox(productEntity);
    }

    @Then("the user sees relevant entity records displayed as filtered result on the application list")
    public void the_user_sees_relevant_entity_records_displayed_as_filtered_result_on_the_application_list() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(aopoManager.getApplicationListPage().filteredEntityVal(productEntity));
    }

    @And("the user perform {string} approval on the application page")
    public void the_user_perform_approval_on_the_application_page(String approval) {
        aopoManager.getApplicationInfoPage().clickButtonByText("Next To Personal Information");
        aopoManager.getPersonalInfoPage().clickButtonByText("Next To Contact Information");
        aopoManager.getContactInfoPage().clickButtonByText("Next To Employee and Financial Information");
        aopoManager.getEmployeeFinInfoPage().clickButtonByText("Next To Trading Experience");
        if (approval.equalsIgnoreCase("first")) {
            aopoManager.getTradingExpPage().clickButtonByText("Verify");
        } else {
            aopoManager.getTradingExpPage().clickButtonByText("Approve");
        }
        aopoManager.getTradingExpPage().selectReason("Pass eKYC");
        aopoManager.getTradingExpPage().clickButtonByText("Confirm");

    }

    @And("the user selects verify reason {string} on the verify pop up on the create company account page")
    public void the_user_selects_verify_reason_on_the_verify_pop_up_on_the_create_company_account_page(String reason) {
        aopoManager.getCompanyAccountPagePW().selectReason(reason);
    }

    @Then("the user sees text field label {string} is displayed on application information page")
    public void the_user_sees_text_field_label_is_displayed_on_application_information_page(String labelName) {
        assertThat(aopoManager.getApplicationInfoPage().getTextFieldLabel(labelName)).isVisible();
    }

    @And("the user fills reusable data in the text field {string} on create company account page")
    public void the_user_fills_reusable_data_in_the_text_field_on_create_company_account_page(String textFieldName) {
        aopoManager.getCompanyAccountPagePW().fillTextFieldVal(getOriginData(), textFieldName);
    }

    @And("the user empties the text field {string} on application information page")
    public void the_user_empties_the_text_field_on_application_information_page(String textFieldName) {
        aopoManager.getApplicationInfoPage().emptyField(textFieldName);
    }

    @And("the user empties the text field {string} on create company account page")
    public void the_user_empties_the_text_field_on_company_account_detail_page(String textFieldName) {
        aopoManager.getCompanyAccountPagePW().emptyField(textFieldName);
    }

    @Then("the user sees {string} error message displayed on company account detail page")
    public void the_user_sees_error_message_displayed_on_create_company_account_page(String msgText) {
        assertThat(aopoManager.getCompanyAccountPagePW().getErrorMsg()).containsText(msgText);
    }

    @And("the user fills value {string} in the text field {string} on create company account page")
    public void the_user_fills_value_in_the_text_field_on_create_company_account_page(String value, String textField) {
        aopoManager.getCompanyAccountPagePW().fillTextFieldVal(value, textField);
    }

    @Then("the user sees an error dialogue with wordings {string} on the create company account page")
    public void the_user_sees_an_error_dialogue_with_wordings_on_the_create_company_account_page(String dialogueText) {
        assertThat(aopoManager.getCompanyAccountPagePW().getToastMsg()).containsText(dialogueText);
    }

    @And("the user clicks detail button of status changed record on the application page")
    public void the_user_clicks_detail_button_of_status_changed_record_on_the_application_page(){
        aopoManager.getApplicationListPage().clickClientRecordDetailBtn(applicantEmail);
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    @Then("the user sees {string} label is displayed on company account detail page")
    public void the_user_sees_label_is_displayed_on_company_account_detail_page(String statusLabel) {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        assertThat(aopoManager.getCompanyAccountPagePW().getAccountStatus()).hasText(statusLabel);
    }

}
