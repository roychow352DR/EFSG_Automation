package StepDefinitions.AdminPortal.cm;

import io.cucumber.java.bs.A;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.io.IOException;

import static StepDefinitions.AdminPortal.aoapplication.ApplicationSteps.aopoManager;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static utils.BaseTest.page;


public class CMSteps {

    @Then("the user sees {string} status on customer management page")
    public void the_user_sees_status_on_customer_management_page(String status) {
        assertThat(aopoManager.getCustomerManagementPage().getStatusRow(status, aopoManager.getApplicationListPage().email)).hasText(status);
    }

    @When("the user clicks detail button of {string} record with {string} client type on the customer management page")
    public void the_user_clicks_detail_button_of_record_on_the_customer_management_page(String status, String clientType) throws IOException {
        aopoManager.getCustomerManagementPage().clickDetailBtn(status, clientType);
    }

    @And("the user clicks {string} button on the CM application information page")
    public void the_user_clicks_button_on_the_CM_application_information_page(String buttonName) throws InterruptedException {
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
        aopoManager.cmTradingExpPage.fillReason(reason);
    }

    @And("the user edit {string} on the CM personal information page")
    public void the_user_edit_on_the_CM_personal_information_page(String editField) throws IOException {
        if (editField.contains("Mobile")) {
            aopoManager.cmPersonalInfoPage.fillMobile();
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
        assertThat(aopoManager.getCmPersonalInfoPage().getHistoryDialogue()).isVisible();
    }

    @And("the user sees change value of {string} on the CM personal information page")
    public void the_user_sees_change_value_of_on_the_CM_personal_information_page(String label) {
        assertThat(aopoManager.getCmPersonalInfoPage().getHistoryBtn()).isVisible();
        aopoManager.getCmPersonalInfoPage().getFieldTextByLabel(label);
    }

    @When("the user clicks detail button of specific entity record on the customer management page")
    public void the_user_clicks_detail_button_of_specific_entity_record_on_the_customer_management_page() throws IOException {
        aopoManager.getCustomerManagementPage().clickDetailBtn();
    }

    @Then("the user sees an error dialogue with wordings {string} on the trading experience page")
    public void the_user_sees_an_error_dialogue_with_wordings_on_the_trading_experience_page(String errorText) {
        assertThat(aopoManager.getCmTradingExpPage().getDialogueText()).hasText(errorText);
    }
}
