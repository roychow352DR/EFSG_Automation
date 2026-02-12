package StepDefinitions.MIO.transactionManagement;


import PageObject.MIOadmin.MIOPOManager;
import StepDefinitions.MIO.login.MIOLoginSteps;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class MIOTransactionManagementSteps {

    MIOPOManager mioPoManager = MIOLoginSteps.mioPoManager;

    @When("the user clicks dropdown {string} on MIO Admin Portal deposit management page")
    public void the_user_clicks_dropdown_on_MIO_Admin_Portal_deposit_management_page(String dropdown){
        mioPoManager.getDepositManagementPage().clickDropdown(dropdown);
    }
}
