package Onboarding;

import PageObject.ApplicationPage;
import PageObject.LoginPage;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.*;
import utils.BaseTest;
import utils.Retry;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


public class loginSuccess extends BaseTest {

   // @Parameters({"username","password"})
  // @Test
  // @BeforeClass


    @Test (retryAnalyzer = Retry.class)
    public void loginETE() throws IOException, InterruptedException {
        List<HashMap<String,String>> data = getJsonDataToMap();
        login.loginApplication(data.getFirst().get("email"),data.getFirst().get("password"));
        Assert.assertTrue(applicationPage().menuTitle().isDisplayed());
    }
}
