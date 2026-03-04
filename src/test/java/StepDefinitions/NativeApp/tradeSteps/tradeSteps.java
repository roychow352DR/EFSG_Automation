package StepDefinitions.NativeApp.tradeSteps;

import PageObject.NativeApp.AppPOManager;
import PageObject.NativeApp.AppTradeView;
import io.cucumber.java.en.And;
import StepDefinitions.NativeApp.login.loginSteps;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

public class tradeSteps {

    AppPOManager appPoManager = loginSteps.appPOManager;

    @When("the user taps symbol {string} on the app markets page")
    public void the_user_taps_symbol_on_the_app_markets_page(String symbol) {
        appPoManager.getAppMarketsPage().tapSymbol(symbol);
    }

    @And("the user selects direction {string} on the app trade view")
    public void the_user_selects_direction_on_the_app_trade_view(String direction) {
        appPoManager.getAppTradeView().selectDirection(direction);

    }

    @And("the user switches on take profit and stop loss on the app trade view")
    public void the_user_switches_on_take_profit_and_stop_loss_on_the_app_trade_view() throws InterruptedException {
        appPoManager.getAppTradeView().switchProfitStopLoss();
    }

    @And("the user fills in the text field {string} with direction {string} on the app trade view")
    public void the_user_fills_in_the_text_field_on_the_app_trade_view(String textFieldName, String direction) throws InterruptedException {
        Thread.sleep(8000);
        appPoManager.getAppTradeView().fillInTextField(textFieldName, direction);
    }

    @And("the user taps button {string} on the app trade view")
    public void the_user_taps_button_buy_on_the_app_trade_view(String buttonName) throws InterruptedException {
        Thread.sleep(200);
        appPoManager.getAppTradeView().tapsButton(buttonName);
    }

    @Then("the user sees the value {string} is displayed correctly with the user input value on the confirmation pop up")
    public void the_user_sees_the_value_is_displayed_correctly_with_the_user_input_value_on_the_confirmation_pop_up(String value) throws InterruptedException {
        Thread.sleep(2000);
        Assert.assertEquals(appPoManager.getAppTradeView().getConfirmationValue(value),
                appPoManager.getAppTradeView().getValidationValue(value));
        //appPoManager.getAppTradeView().closeConfirmationPopUp();
    }

    @And("the user fills in the text field {string} with value {string} on the app trade view")
    public void the_user_fills_in_the_text_field_with_value_on_the_app_trade_view(String textFieldName, String value) throws InterruptedException {
        appPoManager.getAppTradeView().fillValueIntoTextField(textFieldName, value);
    }

    @Then("the user sees a new open position is displayed at the position tab of instrument details page")
    public void the_user_sees_a_new_open_position_is_displayed_at_the_position_tab_of_instrument_details_page() throws InterruptedException {
        Thread.sleep(2000);
        Assert.assertTrue(appPoManager.getAppTradeView().getPositionDetail(AppTradeView.executedPrice));
        Assert.assertTrue(appPoManager.getAppTradeView().getPositionDetail(AppTradeView.selectedDirection));
        appPoManager.getAppTradeView().closePosition();

    }

    @And("the user taps button {string} on the confirmation pop up")
    public void the_user_taps_button_on_the_confirmation_pop_up(String buttonName) throws InterruptedException {
        Thread.sleep(200);
        appPoManager.getAppTradeView().getExecutedPrice();
        appPoManager.getAppTradeView().tapsButton(buttonName);
    }

    @And("the user taps {string} cta button on the position tab of instrument details page")
    public void the_user_taps_cta_button_on_the_position_tab_of_instrument_details_page(String buttonName) {
        appPoManager.getAppTradeView().tapCtaButton(buttonName);
    }

    @And("the user selects order type {string} on the app trade view")
    public void the_user_selects_order_type_on_the_app_trade_view(String orderType) throws InterruptedException {
        Thread.sleep(2000);
        appPoManager.getAppTradeView().selectOrderType(orderType);
    }

    @And("the user selects stop limit order option {string} on the app trade view")
    public void the_user_selects_stop_limit_order_option_as_detail_on_the_app_trade_view(String option) {
        appPoManager.getAppTradeView().selectStopLimitOption(option);
    }

    @And("the user scrolls down the app trade view")
    public void the_user_scrolls_down_the_app_trade_view() throws InterruptedException {
        Thread.sleep(500);
        appPoManager.getAppTradeView().scrollDown();
    }

    @Then("the user sees a new pending order is displayed at the pending order tab of instrument details page")
    public void the_user_sees_a_new_pending_order_is_displayed_at_the_pending_order_tab_of_instrument_details_page() throws InterruptedException {
        Thread.sleep(2000);
        Assert.assertTrue(appPoManager.getAppTradeView().getPendingOrdersDetail(AppTradeView.executedPrice));
        Assert.assertTrue(appPoManager.getAppTradeView().getPendingOrdersDetail(AppTradeView.selectedDirection));
        Assert.assertTrue(appPoManager.getAppTradeView().getPendingOrdersDetail(AppTradeView.stopOrderType.split(" ")[1].trim()));
        appPoManager.getAppTradeView().cancelOrder();
    }

    @And("the user selects validity option {string} on the app trade view")
    public void the_user_selects_validity_option_on_the_app_trade_view(String validity) {
        appPoManager.getAppTradeView().selectValidity(validity);
    }

    @Then("the user sees stop order values are displayed correctly with the user input value on the confirmation pop up")
    public void the_user_sees_stop_order_values_are_displayed_correctly_with_the_user_input_value_on_the_confirmation_pop_up() throws InterruptedException {
        Thread.sleep(2000);
        for (String value : appPoManager.getAppTradeView().stopOrderConfirmationPageValues()) {
            Assert.assertEquals(appPoManager.getAppTradeView().getConfirmationValue(value),
                    appPoManager.getAppTradeView().getValidationValue(value));
        }
    }

    @Then("the user sees market order values are displayed correctly with the user input value on the confirmation pop up")
    public void the_user_sees_market_order_values_are_displayed_correctly_with_the_user_input_value_on_the_confirmation_pop_up() throws InterruptedException {
        Thread.sleep(2000);
        for (String value : appPoManager.getAppTradeView().marketOrderConfirmationPageValues()) {
            Assert.assertEquals(appPoManager.getAppTradeView().getConfirmationValue(value),
                    appPoManager.getAppTradeView().getValidationValue(value));
        }
    }
}
