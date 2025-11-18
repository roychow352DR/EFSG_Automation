package StepDefinitions.AdminPortal.aoRolesPermissionSteps;

import com.microsoft.playwright.options.LoadState;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utils.BaseTest;

import java.io.IOException;

import static StepDefinitions.AdminPortal.aoApplicationSteps.ApplicationSteps.aopoManager;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RolesPermissionSteps extends BaseTest {

    @And("the user retrieves number of assigned admin user of the role {string} on the ao roles and permission page")
    public void the_user_retrieves_number_of_assigned_admin_user_of_the_role_on_the_roles_and_permission_page(String roleName) throws IOException {
        String numberOfCount = aopoManager.getAoRolesPermissionPage().getEntityRoleNum(roleName);
        Assert.assertNotNull(numberOfCount);
        setRetrievedData(numberOfCount);
    }

    @Then("the user sees the number of assigned user of the role {string} is {string} on the ao roles and permission page")
    public void the_user_sees_the_number_of_assigned_user_of_the_role_is_on_the_roles_and_permission_page(String roleName, String condition) throws IOException {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        if (condition.equalsIgnoreCase("increased")) {
            Assert.assertEquals(Integer.parseInt(aopoManager.getAoRolesPermissionPage().getEntityRoleNum(roleName)), Integer.parseInt(getRetrievedData()) + 1);
        } else {
            Assert.assertEquals(Integer.parseInt(aopoManager.getAoRolesPermissionPage().getEntityRoleNum(roleName)), Integer.parseInt(getRetrievedData()) - 1);
        }
    }

    @Then("the user sees button {string} is enabled on the ao roles and permission page")
    public void the_user_sees_button_is_enabled_on_the_roles_and_permission_page(String buttonText) {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(aopoManager.getAoRolesPermissionPage().getButtonIsEnable(buttonText));
    }

    @When("the user clicks button {string} on the ao role and permission page")
    public void the_user_clicks_button_on_the_ao_role_and_permission_page(String buttonText) {
        aopoManager.getAoRolesPermissionPage().clickBtnByText(buttonText);

        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    @Then("the user sees dialogue with heading {string} on the ao role and permission page")
    public void the_user_sees_dialogue_with_heading_on_the_ao_role_and_permission_page(String headerText) {
        assertThat(aopoManager.getAoRolesPermissionPage().getDialogueHeader()).hasText(headerText);
    }

    @Then("the user sees button {string} is disabled on the ao roles and permission page")
    public void the_user_sees_button_is_disabled_on_the_ao_roles_and_permission_page(String buttonText) {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertFalse(aopoManager.getAoRolesPermissionPage().getButtonIsEnable(buttonText));
    }

    @And("the user clicks detail button of roles {string} on the role and permission page")
    public void the_use_clicks_detail_button_of_roles_on_the_role_and_permission_page(String roleName) throws IOException {
        aopoManager.getAoRolesPermissionPage().clickButtonByRole(roleName);
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    @And("the user fills value {string} into the text field {string} on the role and permission page")
    public void the_user_fills_value_into_the_text_field_on_the_role_and_permission_page(String inputVal, String textField) throws IOException {
        aopoManager.getAoRolesPermissionPage().getTextFieldByName(textField).fill(inputVal);
    }

    @Then("the user sees a dialogue with wordings {string} is prompted on the role and permission page")
    public void the_user_sees_an_error_dialogue_with_wordings_is_prompted_on_the_role_and_permission_page(String dialogueText) {
        assertThat(aopoManager.getAoRolesPermissionPage().getAlert()).containsText(dialogueText);
    }

    @And("the user uncheck checkbox {string} of the module {string} on the role and permission page")
    public void the_user_uncheck_checkbox_of_the_module_on_the_role_and_permission_page(String checkboxAccess, String module) {
        aopoManager.getAoRolesPermissionPage().uncheckCheckboxByModule(checkboxAccess, module);
    }

    @Then("the user sees button {string} is disabled based on role entity on the ao roles and permission page")
    public void the_user_sees_button_is_disabled_based_on_role_entity_on_the_ao_roles_and_permission_page(String buttonText) throws IOException {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertFalse(aopoManager.getAoRolesPermissionPage().getButtonByEntity(buttonText));
    }

    @And("the user clicks checkbox {string} of the module {string} on the role and permission page")
    public void the_user_clicks_checkbox_of_the_module_on_the_role_and_permission_page(String checkboxAccess, String module) {
        aopoManager.getAoRolesPermissionPage().clickCheckboxByModule(checkboxAccess, module);
    }

    @Then("the user sees button {string} is enabled based on role entity on the ao roles and permission page")
    public void the_user_sees_button_is_enabled_based_on_role_entity_on_the_ao_roles_and_permission_page(String buttonText) throws IOException {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(aopoManager.getAoRolesPermissionPage().getButtonByEntity(buttonText));
    }

    @And("the user fill in the role creation form on the ao role and permission page")
    public void the_user_fill_in_the_role_creation_form_on_the_ao_role_and_permission_page() throws IOException {
        setRetrievedData(aopoManager.getAoRolesPermissionPage().fillAddRoleForm());
        page.waitForTimeout(500);
    }

    @Then("the user sees a new role is created on the ao role and permission page")
    public void the_user_sees_a_new_role_is_created_on_the_ao_role_and_permission_page() {
        Assert.assertEquals(aopoManager.getAoRolesPermissionPage().getRoleName().textContent(), getRetrievedData());
    }

    @Then("the user sees a role is deleted on the ao role and permission page")
    public void the_user_sees_a_role_is_deleted_on_the_ao_role_and_permission_page() throws IOException {
        Assert.assertFalse(aopoManager.getAoRolesPermissionPage().getRoleOnList().isVisible());
    }

    @Then("the user sees an error label with content {string} is prompted on the role and permission page")
    public void the_user_sees_an_error_label_with_content_is_prompted_on_the_role_and_permission_page(String errorContent) {
        assertThat(aopoManager.getAoRolesPermissionPage().getErrorText()).hasText(errorContent);
    }

    @Then("the user sees button {string} is hidden on the role and permission page")
    public void the_user_sees_button_is_hidden_on_the_role_and_permission_page(String buttonText) {
        assertThat(aopoManager.getAoRolesPermissionPage().getButton(buttonText)).isHidden();
    }
}
