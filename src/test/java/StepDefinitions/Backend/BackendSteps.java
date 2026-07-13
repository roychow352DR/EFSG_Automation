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
import java.util.Optional;

public class BackendSteps extends BaseTest {
    public SQLDatabase sqlDb = new SQLDatabase();
    public CoreService coreService = new CoreService(page, productEnv);
    public static String data;

    public BackendSteps() throws IOException {
    }


    @Then("{string} is {string} in CM {string} database table where {string} retrieved by {string}")
    public void is_in_CM_database_table(String columnName, String expectedVal, String tableName, String filterCol, String filterVal) throws SQLException {
        System.out.println(aopoManager.getCustomerManagementPage().email);
        Optional<String> filterValue = sqlDb.getValueBasedOnEmail(filterVal, aopoManager.getCustomerManagementPage().email);
        Optional<String> retrievedValue = sqlDb.retrieveValueFromDb(columnName, tableName, filterCol, filterValue.orElse(null));
        Assert.assertEquals(retrievedValue.orElse(""), expectedVal);
    }

    @Given("{string} retrieved from api endpoint")
    public void retrieved_from_api_endpoint(String value) {
        coreService.getAoAccountDetail("7e4f1f5a-24fc-48a9-8729-38372009e46c", retrieveLocalStorageVal(), value);
    }

    @Then("{string} is updated to modified value in CM {string} database table where {string} retrieved by {string}")
    public void is_updated_to_modified_value_in_CM_database_table_where_retrieved_by(String columnName, String tableName, String filterCol, String filterVal) throws SQLException {
        Optional<String> filterValue = sqlDb.getValueBasedOnEmail(filterVal, aopoManager.getCustomerManagementPage().email);
        Optional<String> retrievedValue = sqlDb.retrieveValueFromDb(columnName, tableName, filterCol, filterValue.orElse(null));
        Assert.assertEquals(retrievedValue.orElse(""), getRetrievedData());
    }

    @And("the user extracts value {string} from the cm page api")
    public void the_user_extracts_value_from_the_cm_page_api(String value) {
        coreService.getCmList(retrieveLocalStorageVal(), value);
    }

    @And("the parameter {string} is set to the value {string}")
    public void the_parameter_is_set_to_the_value(String param, String value) {
        coreService.setParamVal(param, value);
    }

//    @And("the data created by {string} in status {string} is found in the AO application list")
//    public void the_App_client_data_in_status_is_found_in_the_AO_application_list(String createType,String status) throws IOException {
//        data = coreService.getAoAppClient(retrieveLocalStorageVal(), "email", "statusLabel", status,createType);
//        setRetrievedData(data);
//        Assert.assertNotNull(data);
//    }

    @And("value {string} is retrieved according to the param value {string} of param {string} from the ao page api")
    public void value_is_retrieved_according_to_the_param_value_of_param_from_the_ao_page_api(String retrieveVal, String paramVal, String param) {
        data = coreService.getAoListItem(retrieveLocalStorageVal(), retrieveVal, param, paramVal);
        setRetrievedData(data);
    }

}
