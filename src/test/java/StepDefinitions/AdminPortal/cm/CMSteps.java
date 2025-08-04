package StepDefinitions.AdminPortal.cm;

import io.cucumber.java.en.Then;

import static StepDefinitions.AdminPortal.aoapplication.ApplicationSteps.aopoManager;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class CMSteps {

    @Then("the user sees {string} status on customer management page")
    public void the_user_sees_status_on_customer_management_page(String status){
        assertThat(aopoManager.getCustomerManagementPage().getStatusRow(status,aopoManager.getApplicationListPage().email)).hasText(status);
    }
}
