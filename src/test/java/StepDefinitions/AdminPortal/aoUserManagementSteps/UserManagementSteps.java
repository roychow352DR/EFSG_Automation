package StepDefinitions.AdminPortal.aoUserManagementSteps;

import com.microsoft.playwright.options.LoadState;
import io.cucumber.java.en.*;
import org.testng.Assert;
import utils.BaseTest;

import java.io.IOException;

import static StepDefinitions.AdminPortal.aoApplicationSteps.ApplicationSteps.aopoManager;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UserManagementSteps extends BaseTest {


    @Then("the user sees button {string} on the user management page")
    public void the_user_sees_button_on_the_user_management_page(String buttonText) {
        assertThat(aopoManager.getUserManagementPage().getButton(buttonText).first()).isEnabled();
    }

    @Then("the user sees button {string} is disabled on the user management page")
    public void the_user_sees_button_is_disabled_on_the_user_management_page(String buttonText) {
        assertThat(aopoManager.getUserManagementPage().getButton(buttonText).first()).isDisabled();
    }

    @When("the user clicks detail button of username {string} on the user management page")
    public void the_user_clicks_detail_button_of_username_on_the_user_management_page(String username) {
        aopoManager.getUserManagementPage().clickDetailByUsername(username);
        page.waitForTimeout(1000);
    }

    @And("the user modifies dropdown {string} to option {string} on the user management page")
    public void the_user_modifies_dropdown_to_option_on_the_user_management_page(String dropdownName, String option) {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertNotEquals(aopoManager.getUserManagementPage().getDropdownVal(dropdownName).textContent(), option);
        aopoManager.getUserManagementPage().editDropdown(dropdownName, option);
    }

    @And("the user clicks button {string} on the user management page")
    public void the_user_clicks_button_on_the_user_management_page(String buttonText) {
        aopoManager.getUserManagementPage().clickBtnByText(buttonText);
        page.waitForTimeout(1000);
    }

    @Then("the user sees text field {string} is not editable on the user management page")
    public void the_user_sees_text_field_is_not_editable_on_the_user_management_page(String textFieldName) {
        if (!textFieldName.equalsIgnoreCase("entity")) {
            assertThat(aopoManager.getUserManagementPage().getTextFieldState(textFieldName)).isDisabled();
        } else {
            Assert.assertFalse(aopoManager.getUserManagementPage().getEntityState());
        }
    }

    @And("the user changes entity role on the user management page")
    public void the_use_changes_entity_role_on_the_user_management_page() throws IOException {
        String data = aopoManager.getUserManagementPage().setSelectedRole();
        setRetrievedData(data);
    }

    @Then("the user sees value of text field {string} is updated on the user management page")
    public void the_user_sees_value_of_text_field_is_updated_on_the_user_management_page(String textFieldName) throws IOException {
        page.waitForTimeout(1000);
        Assert.assertEquals(aopoManager.getUserManagementPage().getTextFieldVal(textFieldName), getRetrievedData());
    }

    @Then("the user sees button {string} is hidden on the user management page")
    public void the_user_sees_button_is_hidden_on_the_user_management_page(String buttonText) {
        assertThat(aopoManager.getUserManagementPage().getButton(buttonText)).isHidden();
    }

    @And("the user changes entity role to value {string} on the user management page")
    public void the_user_changes_entity_role_to_value_on_the_user_management_page(String roleVal) throws IOException {
        aopoManager.getUserManagementPage().setSelectedRole(roleVal);
    }

    @And("the user clicks entity checkbox on the user management filter dialogue")
    public void the_user_clicks_entity_checkbox_on_the_user_management_filter_dialogue() {
        aopoManager.getUserManagementPage().clickEntityCheckbox(productEntity);
    }

    @Then("the user sees relevant entity records displayed as filtered result on the user management page")
    public void the_user_sees_relevant_entity_records_displayed_as_filtered_result_on_the_user_management_page() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(aopoManager.getUserManagementPage().filteredEntityVal(productEntity));
    }
}
