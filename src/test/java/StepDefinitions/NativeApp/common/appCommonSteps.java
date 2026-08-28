package StepDefinitions.NativeApp.common;

import PageObject.NativeApp.AppPOManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.testng.Assert;
import utils.BaseTest;
import StepDefinitions.NativeApp.login.loginSteps;

public class appCommonSteps extends BaseTest {
    AppPOManager appPoManager = loginSteps.appPOManager;

    @And("the user taps button {string} on the app footer")
    public void the_user_taps_button_on_the_app_footer(String btnName) throws InterruptedException {
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

    @Then("the user sees header {string} on the page")
    public void the_user_sees_header_on_the_page(String expectedHeader) throws InterruptedException {
        String actualHeaderTitle = "";
        switch (expectedHeader) {
            case "Position Details" -> actualHeaderTitle = appPoManager.getAppPositionDetailsPage().getHeader();
            case "Pending Order Details" -> actualHeaderTitle = appPoManager.getAppPendingOrderDetailsPage().getHeader();
            case "Close Position" -> actualHeaderTitle = appPoManager.getAppClosePositionPage().getHeaderText();
            case "Edit Position" -> actualHeaderTitle = appPoManager.getAppEditPositionPage().getHeaderText();
        }
        Assert.assertEquals(actualHeaderTitle, expectedHeader);

        if (expectedHeader.equals("Position Details")) {
            return;
        }
        if (!expectedHeader.equals("Close Position")) {
            appPoManager.getAppTradeView().tapBack();
        }
        appPoManager.getAppTradeView().closePosition();
    }

    @Then("the user is redirected to the {string} on the page")
    public void the_user_is_redirected_to_the_page(String expectedHeader) throws InterruptedException {
        String actualHeaderTitle = "";
        switch (expectedHeader) {
            case "Position Details" -> actualHeaderTitle = appPoManager.getAppPositionDetailsPage().getHeader();
            case "Pending Order Details" -> {
                actualHeaderTitle = appPoManager.getAppPendingOrderDetailsPage().getHeader();
                appPoManager.getAppTradeView().cancelPendingOrderInDetail();
            }
        }
        Assert.assertEquals(actualHeaderTitle, expectedHeader);

    }
}
