package StepDefinitions.Background;

import Data.SQLDatabase;
import io.cucumber.java.en.Given;
import org.testng.Assert;

import java.sql.SQLException;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BackgroundSteps {
    SQLDatabase sqlDb = new SQLDatabase();

    @Given("the user created up to 4 account with existing ID {string}")
    public void the_user_created_up_to_4_account_with_existing_ID(String id) throws SQLException {
        Assert.assertEquals(sqlDb.getPersonIdCount(id),"4");
    }

}
