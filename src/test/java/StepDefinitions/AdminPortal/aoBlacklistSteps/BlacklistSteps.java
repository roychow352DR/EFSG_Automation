package StepDefinitions.AdminPortal.aoBlacklistSteps;

import PageObject.AdminPortalPW.AOPOManager;
import com.microsoft.playwright.options.LoadState;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en_scouse.An;
import org.testng.Assert;

import java.io.IOException;

import static StepDefinitions.AdminPortal.aoApplicationSteps.ApplicationSteps.aopoManager;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static utils.BaseTest.page;
import static utils.BaseTest.productEntity;

public class BlacklistSteps {

    @When("the user clicks button {string} on the blacklist page")
    public void the_user_clicks_button_on_the_blacklist_page(String buttonText){
        aopoManager.getBlackListPage().clickButton(buttonText);
    }

    @And("the user fills mandatory information on the blacklist dialogue")
    public void the_user_fills_mandatory_information_on_the_blacklist_dialogue() throws IOException {
        aopoManager.getBlackListPage().fillMandatory();
    }

    @Then("the user sees a new created blacklist record is listed on the blacklist page")
    public void the_user_sees_a_new_created_blacklist_record_is_listed_on_the_blacklist_page() throws IOException {
        assertThat(aopoManager.getBlackListPage().getBlacklistRecordRow()).isVisible();
    }

    @When("the user clicks detail button of status {string} on the blacklist page")
    public void the_user_clicks_detail_button_of_status_on_the_blacklist_page(String blacklistStatus) throws IOException {
        aopoManager.getBlackListPage().clickDetailBtn(blacklistStatus);
    }

    @And("the user clicks radio button {string} on the blacklist dialogue")
    public void the_user_clicks_radio_button_on_the_blacklist_dialogue(String radioLabel) {
        aopoManager.getBlackListPage().clickRadioButton(radioLabel);
    }

    @Then("the user sees an existing record is updated to status {string} on the blacklist page")
    public void the_user_sees_an_existing_record_is_updated_to_status_on_the_blacklist_page(String blacklistStatus){
        Assert.assertEquals(aopoManager.getBlackListPage().getModifiedRowStatus(),blacklistStatus);
    }

    @Then("the {string} button is not displayed on the blacklist page")
    public void the_button_is_not_displayed_on_the_blacklist_page(String buttonName){
        assertThat(aopoManager.getBlackListPage().getButton(buttonName)).isHidden();
    }

    @And("the user clicks entity checkbox on the blacklist filter dialogue")
    public void the_user_clicks_entity_checkbox_on_the_blacklist_filter_dialogue() {
        aopoManager.getBlackListPage().clickEntityCheckbox(productEntity);
    }

    @Then("the user sees relevant entity records displayed as filtered result on the blacklist page")
    public void the_user_sees_relevant_entity_records_displayed_as_filtered_result_on_the_blacklist_page() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
        Assert.assertTrue(aopoManager.getBlackListPage().filteredEntityVal(productEntity));
    }

}
