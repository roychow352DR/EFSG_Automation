package Onboarding;


import PageObject.ApplicatInformationPage;
import PageObject.ApplicationPage;
import PageObject.PersonalInfoPage;
import org.openqa.selenium.By;
import org.testng.annotations.Test;
import utils.BaseTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class createApplication extends BaseTest {

   @Test
    public void aoApplication() throws InterruptedException, IOException {
       login.loginApplication(getJsonDataToMap().getFirst().get("email"),getJsonDataToMap().getFirst().get("password"));
       applicationPage().clickButton("Create Account");
        driver.findElement(By.cssSelector("button[class*='MuiButtonBase-root MuiButton-root MuiButton-contained']")).click();
    //WebElement a = buttons.stream().filter(button->button.getText().equalsIgnoreCase("Create Account"));
       // fillInApplicant.clickEntityDropdown("EBL_MF4");
       // fillInApplicant.fillInApplicantInfo();
       // PersonalInfoPage personalInfo = new PersonalInfoPage(driver);
      //  personalInfo.fillInPersonalInfo();


    }

}
