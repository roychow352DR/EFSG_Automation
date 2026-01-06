package StepDefinitions.NativeApp.common;

import PageObject.NativeApp.AppPOManager;
import io.cucumber.java.en.And;
import utils.BaseTest;
import StepDefinitions.NativeApp.login.loginSteps;

public class appCommonSteps extends BaseTest {
    AppPOManager appPoManager = loginSteps.appPOManager;

    @And("the user taps button {string} on the app footer")
    public void the_user_taps_button_on_the_app_footer(String footerButton) throws InterruptedException {
        Thread.sleep(3000);
        appPoManager.getAppHomePage().tapFooterButton(footerButton);
    }

    @And("the user taps button {string} on the app me page")
    public void the_user_taps_button_on_the_app_me_page(String btnName) {
        appPoManager.getAppMePage().tapButtonOnMe(btnName);
    }
}
