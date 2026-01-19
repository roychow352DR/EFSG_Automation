package StepDefinitions.NativeApp.common;

import PageObject.NativeApp.AppPOManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import utils.BaseTest;
import StepDefinitions.NativeApp.login.loginSteps;

public class appCommonSteps extends BaseTest {
    AppPOManager appPoManager = loginSteps.appPOManager;

    @And("the user taps button {string} on the app footer")
    public void the_user_taps_button_on_the_app_footer(String btnName) throws InterruptedException {
        Thread.sleep(3000);
        appPoManager.getAppFooter().tapFooterButton(btnName);
    }

    @And("the user taps button {string} on the app me page")
    public void the_user_taps_button_on_the_app_me_page(String btnName) throws InterruptedException {
        Thread.sleep(3000);
        appPoManager.getAppMePage().tapButtonOnMe(btnName);
    }

    @Then("the user sees button {string} is displayed at the app markets page")
    public void the_user_sees_button_is_displayed_at_the_app_markets_page(String btnName) {

    }

    @And("the user taps button {string} on the app markets page")
    public void the_user_taps_button_on_the_app_markets_page(String btnName) throws InterruptedException {
        appPoManager.getAppMarketsPage().tapButtonOnMarketsPage(btnName);
        Thread.sleep(5000);
    }
}
