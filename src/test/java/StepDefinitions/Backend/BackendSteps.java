package StepDefinitions.Backend;

import API.CoreService;
import Data.SQLDatabase;
import com.fasterxml.jackson.databind.ser.Serializers;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.checkerframework.checker.units.qual.C;
import org.testng.Assert;
import utils.BaseTest;

import java.sql.SQLException;

import static StepDefinitions.AdminPortal.aoapplication.ApplicationSteps.aopoManager;

public class BackendSteps extends BaseTest {
    SQLDatabase sqlDb = new SQLDatabase();
    CoreService coreService = new CoreService();

    @Then("{string} is {string} in CM {string} database table where {string} retrieved by {string}")
    public void is_in_CM_database_table(String columnName,String expectedVal,String tableName,String filterCol,String filterVal) throws SQLException {
        String retrievedValue = sqlDb.retrieveValueFromDb(columnName,tableName,filterCol, sqlDb.getValueBasedOnEmail(filterVal,aopoManager.getApplicationListPage().email));
        Assert.assertEquals(retrievedValue,expectedVal);
    }

    @Given("{string} retrieved from api endpoint")
    public void retrieved_from_api_endpoint(String value){
        coreService.getAoAccountDetail("7e4f1f5a-24fc-48a9-8729-38372009e46c",retrieveLocalStorageVal(),value);
    }

    @Then("{string} is updated to modified value in CM {string} database table where {string} retrieved by {string}")
    public void is_updated_to_modified_value_in_CM_database_table_where_retrieved_by(String columnName,String tableName,String filterCol,String filterVal) throws SQLException {
        String retrievedValue = sqlDb.retrieveValueFromDb(columnName,tableName,filterCol,sqlDb.getValueBasedOnEmail(filterVal,aopoManager.getCustomerManagementPage().email));
        Assert.assertEquals(retrievedValue,aopoManager.cmPersonalInfoPage.changeValue);
    }
}
