package StepDefinitions.Background;

import utils.BaseTest;
import utils.SetCondition;
import Data.SQLDatabase;
import io.cucumber.java.en.Given;
import org.testng.Assert;

import java.sql.SQLException;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BackgroundSteps extends BaseTest {
    SQLDatabase sqlDb = new SQLDatabase();
    public boolean isExistedEmail;
    public boolean isExistedPhoneNumber;
    public boolean isBelow18;
    public boolean isExpired;
    public boolean isEdd;
    public boolean isExpiredBeforeCurrent;
    public boolean isCrossEntity;
   //public static SetCondition setCondition;

    @Given("the user created up to 4 account with existing ID {string}")
    public void the_user_created_up_to_4_account_with_existing_ID(String id) throws SQLException {
        Assert.assertEquals(sqlDb.getPersonIdCount(id), "4");
    }
//
//    @Given("the {string} condition is satisfied")
//    public void the_condition_is_satisfied(String condition) {
//        switch (condition) {
//            case "DOB Below 18" -> isBelow18 = true;
//            case "Exist Email" -> isExistedEmail = true;
//            case "Exist Phone Number" -> isExistedPhoneNumber = true;
//            case "Expired date" -> isExpired = true;
//            case "EDD" -> isEdd = true;
//            case "Expired date before current date" -> isExpiredBeforeCurrent = true;
//            case "Cross Entity" -> isCrossEntity = true;
//        }
//        initializeCondition = new SetCondition(isExistedEmail, isExistedPhoneNumber, isBelow18, isExpired, isEdd,isExpiredBeforeCurrent,isCrossEntity);
//    }

}
