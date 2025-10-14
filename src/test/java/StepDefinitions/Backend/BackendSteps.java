package StepDefinitions.Backend;

import API.CoreService;
import Data.SQLDatabase;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.testng.Assert;
import utils.BaseTest;

import java.io.IOException;
import java.sql.SQLException;

import static StepDefinitions.AdminPortal.aoApplicationSteps.ApplicationSteps.aopoManager;

public class BackendSteps extends BaseTest {
    SQLDatabase sqlDb = new SQLDatabase();
    CoreService coreService = new CoreService(page, productEnv);
    public static String data;

    public BackendSteps() throws IOException {
    }

    @Then("{string} is {string} in CM {string} database table where {string} retrieved by {string}")
    public void is_in_CM_database_table(String columnName, String expectedVal, String tableName, String filterCol, String filterVal) throws SQLException {
        String retrievedValue = sqlDb.retrieveValueFromDb(columnName, tableName, filterCol, sqlDb.getValueBasedOnEmail(filterVal, aopoManager.getCustomerManagementPage().email));
        Assert.assertEquals(retrievedValue, expectedVal);
    }

    @Given("{string} retrieved from api endpoint")
    public void retrieved_from_api_endpoint(String value) {
        coreService.getAoAccountDetail("7e4f1f5a-24fc-48a9-8729-38372009e46c", retrieveLocalStorageVal(), value);
    }

    @Then("{string} is updated to modified value in CM {string} database table where {string} retrieved by {string}")
    public void is_updated_to_modified_value_in_CM_database_table_where_retrieved_by(String columnName, String tableName, String filterCol, String filterVal) throws SQLException {
        String retrievedValue = sqlDb.retrieveValueFromDb(columnName, tableName, filterCol, sqlDb.getValueBasedOnEmail(filterVal, aopoManager.getCustomerManagementPage().email));
        Assert.assertEquals(retrievedValue, getRetrievedData());
    }

    @And("the user extracts value {string} from the cm page api")
    public void the_user_extracts_value_from_the_cm_page_api(String value) {
        coreService.getCmList(retrieveLocalStorageVal(), value);
    }

    @And("the parameter {string} is set to the value {string}")
    public void the_parameter_is_set_to_the_value(String param, String value) {
        coreService.setParamVal(param, value);
    }

    @And("the App client data {string} is found in the AO application list")
    public void App_client_account_is_registered_in_AO_list(String retrieveVal) {
        data = coreService.getAoListItem(retrieveLocalStorageVal(), retrieveVal, "createdBy", "Customer");
        Assert.assertNotNull(data);
    }

    @And("value {string} is retrieved according to the param value {string} of param {string} from the ao page api")
    public void value_is_retrieved_according_to_the_param_value_of_param_from_the_ao_page_api(String retrieveVal, String paramVal, String param) {
        data = coreService.getAoListItem(retrieveLocalStorageVal(), retrieveVal, param, paramVal);
        setRetrievedData(data);
    }
}
