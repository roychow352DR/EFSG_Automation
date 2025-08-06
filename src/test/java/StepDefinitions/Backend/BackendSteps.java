package StepDefinitions.Backend;

import Data.SQLDatabase;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.sql.SQLException;

import static StepDefinitions.AdminPortal.aoapplication.ApplicationSteps.aopoManager;

public class BackendSteps {
    SQLDatabase sqlDb = new SQLDatabase();

    @Then("{string} is {string} in CM {string} database table where {string} retrieved by {string}")
    public void is_in_CM_database_table(String columnName,String expectedVal,String tableName,String filterCol,String filterVal) throws SQLException {
        String retrievedValue = sqlDb.retrieveValueFromDb(columnName,tableName,filterCol, sqlDb.getValueBasedOnEmail(filterVal,aopoManager.getApplicationListPage().email));
        Assert.assertEquals(retrievedValue,expectedVal);
    }
}
