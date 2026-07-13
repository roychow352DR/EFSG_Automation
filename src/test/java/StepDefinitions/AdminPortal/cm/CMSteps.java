package StepDefinitions.AdminPortal.cm;

import Data.CmAccountStatus;
import PageObject.AdminPortalPW.AOPOManager;
import PageObject.AdminPortalPW.ApplicationListPagePW;
import com.microsoft.playwright.options.LoadState;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utils.BaseTest;
import utils.SetCondition;

import java.io.IOException;

import static StepDefinitions.AdminPortal.aoApplicationSteps.ApplicationSteps.accountCreation;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CMSteps extends BaseTest {

    private final CmAccountStatus cmAccountStatus;

    public CMSteps() throws IOException {
        aopoManager = new AOPOManager(page);
        this.cmAccountStatus = new CmAccountStatus(aopoManager);
    }

    @Then("the user sees {string} status on customer management page")
    public void the_user_sees_status_on_customer_management_page(String status) {
        page.waitForTimeout(2000);
        assertThat(aopoManager.getCustomerManagementPage().getStatusRow(status, ApplicationListPagePW.email)).hasText(status);
    }

    @When("the user clicks detail button of {string} record with {string} client type on the customer management page")
    public void the_user_clicks_detail_button_of_record_on_the_customer_management_page(String status, String clientType) throws IOException {
        aopoManager.getCustomerManagementPage().clickDetailBtn(status, clientType);
        page.waitForTimeout(3000);
    }


    @And("the user clicks {string} button on the CM application information page")
    public void the_user_clicks_button_on_the_CM_application_information_page(String buttonName) throws InterruptedException {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        aopoManager.getCmApplicationInfoPage().clickButtonByText(buttonName);
    }

    @And("the user clicks {string} button on the CM personal information page")
    public void the_user_clicks_button_on_the_CM_personal_information_page(String buttonName) {
        aopoManager.getCmPersonalInfoPage().clickButtonByText(buttonName);
    }

    @And("the user clicks {string} button on the CM contact information page")
    public void the_user_clicks_button_on_the_CM_contact_information_page(String buttonName) {
        aopoManager.getCmContactInfoPage().clickButtonByText(buttonName);
    }

    @And("the user clicks {string} button on the CM employee & financial page")
    public void the_user_clicks_button_on_the_CM_employee_financial_page(String buttonName) {
        aopoManager.getCmEmployeeInfoPage().clickButtonByText(buttonName);
    }

    @And("the user clicks {string} button on the CM trading experience page")
    public void the_user_clicks_button_on_the_CM_trading_experience_page(String buttonName) {
        aopoManager.getCmTradingExpPage().clickButtonByText(buttonName);
    }

    @Then("the user sees an existing record is updated to {string} status on the customer management list")
    public void the_user_sees_an_existing_record_is_updated_to_status_on_the_customer_management_list(String status) {
        assertThat(aopoManager.getCustomerManagementPage().getStatusRow(status, aopoManager.getCustomerManagementPage().email)).hasText(status);
    }

    @And("the user fills {string} as reject reason on the CM trading experience page")
    public void the_user_fills_as_reject_reason_on_the_CM_trading_experience_page(String reason) {
        aopoManager.getCmTradingExpPage().fillReason(reason);
    }

    @And("the user edit text field {string} on the CM personal information page")
    public void the_user_edit_text_field_on_the_CM_personal_information_page(String editField) throws IOException {
        if (editField.equalsIgnoreCase("mobile")) {
            aopoManager.getCmPersonalInfoPage().fillMobile();
            setRetrievedData(aopoManager.getCmPersonalInfoPage().getTextFieldValue(editField));
        } else if (editField.equalsIgnoreCase("username")) {
            setOriginData(aopoManager.getCmPersonalInfoPage().getTextFieldValue(editField));
            aopoManager.getCmPersonalInfoPage().fillUsername();
            setRetrievedData(aopoManager.getCmPersonalInfoPage().getTextFieldValue(editField));
        }
    }

    @When("the user clicks detail button of modified record on the customer management page")
    public void the_user_clicks_detail_button_of_modified_record_on_the_customer_management_page() throws IOException {
        aopoManager.getCustomerManagementPage().clickDetailBtn();
    }

    @Then("the user sees {string} button on the CM personal information page")
    public void the_user_sees_button_on_the_CM_personal_information_page(String buttonName) {
        assertThat(aopoManager.getCmPersonalInfoPage().locatorValidation(buttonName)).isVisible();
    }

    @Then("the user sees {string} dialogue on the CM personal information page upon click on the {string} button")
    public void the_user_sees_dialogue_on_the_CM_personal_information_page_upon_click_on_the_button(String dialogueName, String buttonName) {
        boolean isVisible = aopoManager.getCmPersonalInfoPage().getHistoryDialogue().isVisible();
        if (isVisible) {
            aopoManager.getCmPersonalInfoPage().clickCrossButton();
        }
        Assert.assertTrue(isVisible);
    }

    @And("the user sees change value of {string} on the CM personal information page")
    public void the_user_sees_change_value_of_on_the_CM_personal_information_page(String label) {
        assertThat(aopoManager.getCmPersonalInfoPage().getHistoryBtn()).isVisible();
        setRetrievedData(aopoManager.getCmPersonalInfoPage().getFieldTextByLabel(label));
    }

    @When("the user clicks detail button of specific entity record on the customer management page")
    public void the_user_clicks_detail_button_of_specific_entity_record_on_the_customer_management_page() throws IOException {
        aopoManager.getCustomerManagementPage().clickDetailBtn();
        page.waitForTimeout(2000);
    }

    @Then("the user sees an error dialogue with wordings {string} on the trading experience page")
    public void the_user_sees_an_error_dialogue_with_wordings_on_the_trading_experience_page(String errorText) {
        assertThat(aopoManager.getCmTradingExpPage().getDialogueText()).hasText(errorText);
        aopoManager.getCmTradingExpPage().clickCloseBtn();
    }

    @And("the user uncheck {string} checkbox on the CM personal information page")
    public void the_user_uncheck_checkbox_on_the_CM_personal_information_page(String checkboxName) {
        aopoManager.getCmPersonalInfoPage().uncheckBox(checkboxName);
    }

    @Then("the user sees {string} error message displayed on the CM personal information page")
    public void the_user_sees_error_message_displayed_on_the_CM_personal_information_page(String errorText) {
        assertThat(aopoManager.getCmPersonalInfoPage().errorValidation()).hasText(errorText);
    }

    @Then("the user sees {string} dialogue is prompted on the customer management page")
    public void the_user_sees_dialogue_is_prompted_on_the_customer_management_page(String dialogueText) {
        assertThat(aopoManager.getCustomerManagementPage().getDialogue()).hasText(dialogueText);
    }

    @When("the user sees a record in {string} status with {string} client type on the customer management page")
    public void the_user_sees_a_record_in_status_with_client_type_on_the_customer_management_page(String cmStatus, String clientType) throws IOException {
        aopoManager.getCustomerManagementPage().getStatusEmail(cmStatus, clientType);
    }

    @Then("the user sees dialogue text {string} is prompted on the CM personal information page")
    public void the_user_sees_dialogue_text_is_prompted_on_the_CM_personal_information_page(String toastMsg) {
        assertThat(aopoManager.getCmPersonalInfoPage().getToastMsg()).containsText(toastMsg);
    }

    @And("the user performs first approval on cm page for the account type {string}")
    public void the_user_performs_first_approval_on_cm_page_for_the_account_type(String accountType) throws InterruptedException {
        accountCreation.cmFirstApproval(accountType);
    }

    @And("the user performs second approval on cm page for the account type {string}")
    public void the_user_performs_second_approval_on_cm_page(String accountType) throws InterruptedException {
        accountCreation.cmSecondApproval(accountType);
    }

    @Then("the user sees text field {string} value is updated on the CM personal information page")
    public void the_user_sees_text_field_value_is_updated_on_the_CM_personal_information_page(String textFieldName) {
        Assert.assertEquals(aopoManager.getCmPersonalInfoPage().getTextFieldValue(textFieldName), getRetrievedData());
    }

    @And("the user fills value {string} in the text field {string} on the CM personal information page")
    public void the_user_fills_value_in_the_text_field_on_the_CM_personal_information_page(String value, String textFieldName) throws IOException {
        aopoManager.getCmPersonalInfoPage().fillInputFieldByName(value, textFieldName);
        aopoManager.getCmPersonalInfoPage().selectExpiryDate(SetCondition.isExpired(), SetCondition.isExpiredBeforeCurrent());
    }

    @Then("the user lands on next tab {string} of customer management edit page")
    public void the_user_lands_on_next_tab_of_customer_management_edit_page(String buttonText) {
        assertThat(aopoManager.getContactInfoPage().getTab(buttonText)).hasAttribute("aria-selected", "true");
    }

    @Then("the user sees text field {string} is editable on the CM personal information page")
    public void the_user_sees_text_field_is_editable_on_the_CM_personal_information_page(String textFieldName) {
        assertThat(aopoManager.getCmPersonalInfoPage().getTextField(textFieldName)).isEditable();
    }

    @Then("the user sees text field {string} is not editable on the CM personal information page")
    public void the_user_sees_text_field_is_not_editable_on_the_CM_personal_information_page(String textFieldName) {
        assertThat(aopoManager.getCmPersonalInfoPage().getTextField(textFieldName)).isDisabled();
    }

    @And("the user edit text field {string} on the CM trading experience page")
    public void the_user_edit_text_field_on_the_CM_trading_experience_page(String textFieldName) {
        setRetrievedData(aopoManager.getCmTradingExpPage().editDropdownVal(textFieldName));
    }

    @Then("the user sees text field {string} value is updated on the CM trading experience page")
    public void the_user_sees_text_field_value_is_updated_on_the_CM_trading_experience_page(String textFieldName) {
        page.waitForTimeout(5000);
        Assert.assertEquals(aopoManager.getCmTradingExpPage().getFieldValByLabel(textFieldName), getRetrievedData());
    }

    @Then("the user sees all elements are not editable on the CM personal information page")
    public void the_user_sees_all_elements_are_not_editable_on_the_CM_personal_information_page() {
        Assert.assertFalse(aopoManager.getCmPersonalInfoPage().checkTextFieldIsEditable());
        Assert.assertFalse(aopoManager.getCmPersonalInfoPage().checkElementIsClickable());
    }

    @Then("the user sees all elements are not editable on the CM application information page")
    public void the_user_sees_all_elements_are_not_editable_on_the_CM_application_information_page() {
        Assert.assertFalse(aopoManager.getCmApplicationInfoPage().checkTextFieldIsEditable());
    }

    @Then("the user sees all elements are not editable on the CM contact information page")
    public void the_user_sees_all_elements_are_not_editable_on_the_CM_contact_information_page() {
        Assert.assertFalse(aopoManager.getCmContactInfoPage().checkTextFieldIsEditable());
        Assert.assertFalse(aopoManager.getCmContactInfoPage().checkElementIsClickable());
    }

    @Then("the user sees all elements are not editable on the CM employee and financial information page")
    public void the_user_sees_all_elements_are_not_editable_on_the_CM_employee_and_financial_information_page() {
        Assert.assertFalse(aopoManager.getCmEmployeeInfoPage().checkElementIsClickable());
    }

    @Then("the user sees dropdown {string} is not editable on the CM trading experience page")
    public void the_user_sees_dropdown_is_not_editable_on_the_CM_trading_experience_page(String dropdownName) {
        assertThat(aopoManager.getCmTradingExpPage().getDropdown(dropdownName)).isDisabled();
    }

    @Then("the user sees dropdown {string} is editable on the CM trading experience page")
    public void the_user_sees_dropdown_is_editable_on_the_CM_trading_experience_page(String dropdownName) {
        assertThat(aopoManager.getCmTradingExpPage().getDropdown(dropdownName)).isEnabled();
    }

    @And("the user clicks detail button of amended record on the customer management page")
    public void the_user_clicks_detail_button_of_amended_record_on_the_customer_management_page() {
        aopoManager.getCustomerManagementPage().clickAmendDetailBtn();
    }

    @Then("the user sees an error dialogue with wordings {string} is prompted on the CM trading experience page")
    public void the_user_sees_an_error_dialogue_with_wordings_is_prompted_on_the_CM_trading_experience_page(String dialogueText) {
        assertThat(aopoManager.getCmTradingExpPage().getDialogue()).hasText(dialogueText);
    }

    @And("the user selects ID Type {string} on the CM personal information page")
    public void the_user_selects_ID_Type_on_the_CM_personal_information_page(String idType) throws IOException {
        aopoManager.getCmPersonalInfoPage().selectIdType(idType);
    }

    @When("the user clicks button {string} on the customer management page")
    public void the_user_clicks_button_on_the_customer_management_page(String buttonText) {
        aopoManager.getCustomerManagementPage().clickBtnByText(buttonText);
    }

    @And("the user fills value {string} in the text field {string} on the customer management filter dialogue")
    public void the_user_fills_value_in_the_text_field_on_the_customer_management_filter_dialogue(String filterVal, String inputField) {
        aopoManager.getCustomerManagementPage().fillValToField(filterVal, inputField);
    }

    @Then("the customer management list displays {string} in the {string} column as a filtered result")
    public void the_customer_management_list_displays_in_the_column_as_a_filtered_result(String result, String column) throws IOException {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue((aopoManager.getCustomerManagementPage().filteredVal(column, result)));
    }

    @When("the user fills value {string} on the customer management search field")
    public void the_user_fills_value_on_the_customer_management_search_field(String searchVal) {
        aopoManager.getCustomerManagementPage().fillSearchVal(searchVal);
        page.keyboard().press("Enter");
    }

    @And("the user clicks detail button of newly created record with account type {string} on the customer management page")
    public void the_user_clicks_detail_button_of_newly_created_record_with_account_type_on_the_customer_management_page(String accountType) {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        if (accountType.equalsIgnoreCase("Individual")) {
            aopoManager.getCustomerManagementPage().clickClientRecordDetailBtn(aopoManager.getApplicationInfoPage().submittedApplicantEmail());
        } else {
            aopoManager.getCustomerManagementPage().clickClientRecordDetailBtn(aopoManager.getCompanyAccountPagePW().submittedApplicantEmail());
        }
    }

    @And("the user clicks button {string} on the CM user information page")
    public void the_user_clicks_button_on_the_CM_user_information_page(String buttonText) {
        aopoManager.getCmUserInformationPage().clickBtnByText(buttonText);
    }

    @And("the user fills value {string} in the text field {string} on the CM user information page")
    public void the_user_fills_value_in_the_text_field_on_the_CM_user_information_page(String value, String textFieldName) {
        aopoManager.getCmUserInformationPage().fillValToField(value, textFieldName);
    }

    @Then("the user sees dialogue text {string} is prompted on the CM user information page")
    public void the_user_sees_dialogue_text_is_prompted_on_the_CM_user_information_page(String dialogueText) {
        assertThat(aopoManager.getCmUserInformationPage().getToastMsg()).hasText(dialogueText);
    }

    @And("the user edit text field {string} on the CM user information page")
    public void the_user_edit_text_field_on_the_CM_user_information_page(String textFieldName) throws IOException {
        if (textFieldName.equalsIgnoreCase("mobile")) {
            aopoManager.getCmUserInformationPage().fillMobile();
            setRetrievedData(aopoManager.getCmPersonalInfoPage().getTextFieldValue(textFieldName));
        } else if (textFieldName.equalsIgnoreCase("username")) {
            setOriginData(aopoManager.getCmPersonalInfoPage().getTextFieldValue(textFieldName));
            aopoManager.getCmUserInformationPage().fillUsername();
            setRetrievedData(aopoManager.getCmPersonalInfoPage().getTextFieldValue(textFieldName));
        }
    }

    @And("the user submits change on customer management page")
    public void the_user_submits_change_on_customer_management_page() throws InterruptedException {
        cmAccountStatus.submitChange();
    }

    @Then("the user sees title {string} is displayed at the create company account page")
    public void the_user_sees_title_displayed_at_the_create_company_account_page(String title) {
        assertThat(aopoManager.getCompanyAccountPagePW().getPageTitle()).containsText(title);
    }

    @Then("the user sees status {string} is displayed at the create company account page")
    public void the_user_sees_status_is_displayed_at_the_create_company_account_page(String status) {
        assertThat(aopoManager.getCompanyAccountPagePW().getAccountStatusLabel()).hasText(status);
    }

    @And("the user performs first approval on customer management page")
    public void the_user_performs_first_approval_on_customer_management_page() throws InterruptedException {
        cmAccountStatus.cmFirstApproval();
    }

    @And("the user rejects the submission on customer management page")
    public void the_user_rejects_the_submission_on_customer_management_page() throws InterruptedException {
        cmAccountStatus.cmReject();
    }

    @And("the user performs second approval on customer management page")
    public void the_user_performs_second_approval_on_customer_management_page() throws InterruptedException {
        cmAccountStatus.cmSecondApproval();
    }

    @And("the user submits personal information change on customer management page")
    public void the_user_submits_personal_information_change_on_customer_management_page() throws InterruptedException {
        cmAccountStatus.submitPersonalInfoChange();
    }

    @And("the user performs second approval from personal info page on customer management page")
    public void the_user_performs_second_approval_from_personal_info_page_on_customer_management_page() throws InterruptedException {
        cmAccountStatus.cmSecondApprovalFromPersonalInfo();
    }

    @And("the user performs first approval from personal info page on customer management page")
    public void the_user_performs_first_approval_from_personal_info_page_on_customer_management_page() throws InterruptedException {
        cmAccountStatus.cmSecondApprovalFromPersonalInfo();
    }
}
