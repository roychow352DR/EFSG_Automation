package StepDefinitions.AdminPortal.cm;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static StepDefinitions.AdminPortal.aoapplication.ApplicationSteps.aopoManager;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static utils.BaseTest.page;


public class CMSteps {

    @Then("the user sees {string} status on customer management page")
    public void the_user_sees_status_on_customer_management_page(String status){
        assertThat(aopoManager.getCustomerManagementPage().getStatusRow(status,aopoManager.getApplicationListPage().email)).hasText(status);
    }

    @When("the user clicks detail button of {string} record on the customer management page")
    public void the_user_clicks_detail_button_of_record_on_the_customer_management_page(String status){
        aopoManager.getCustomerManagementPage().clickDetailBtn(status);
    }

    @And("the user clicks {string} button on the CM application information page")
    public void the_user_clicks_button_on_the_CM_application_information_page(String buttonName){
        aopoManager.getCmApplicationInfoPage().clickButtonByText(buttonName);
    }

    @And("the user clicks {string} button on the CM personal information page")
    public void the_user_clicks_button_on_the_CM_personal_information_page(String buttonName){
        aopoManager.getCmPersonalInfoPage().clickButtonByText(buttonName);
    }

    @And("the user clicks {string} button on the CM contact information page")
    public void the_user_clicks_button_on_the_CM_contact_information_page(String buttonName){
        aopoManager.getCmContactInfoPage().clickButtonByText(buttonName);
    }

    @And("the user clicks {string} button on the CM employee & financial page")
    public void the_user_clicks_button_on_the_CM_employee_financial_page(String buttonName){
        aopoManager.getCmEmployeeInfoPage().clickButtonByText(buttonName);
    }

    @And("the user clicks {string} button on the CM trading experience page")
    public void the_user_clicks_button_on_the_CM_trading_experience_page(String buttonName){
        aopoManager.getCmTradingExpPage().clickButtonByText(buttonName);
    }

    @Then("the user sees an existing record is updated to {string} status on the customer management list")
    public void the_user_sees_an_existing_record_is_updated_to_status_on_the_customer_management_list(String status){
        assertThat(aopoManager.getCustomerManagementPage().getStatusRow(status,aopoManager.getCustomerManagementPage().email)).hasText(status);
    }

    @And("the user fills {string} as reject reason on the CM trading experience page")
    public void the_user_fills_as_reject_reason_on_the_CM_trading_experience_page(String reason){
        aopoManager.cmTradingExpPage.fillReason(reason);
    }
}
