package StepDefinitions.NativeApp.tradeSteps;

import Data.TradeRecord;
import Data.TradeSymbolConfig;
import PageObject.NativeApp.AppInstrumentDetailsPage;
import PageObject.NativeApp.AppMarketsPage;
import PageObject.NativeApp.AppPOManager;
import PageObject.NativeApp.AppTradeView;
import io.cucumber.java.en.And;
import StepDefinitions.NativeApp.login.loginSteps;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import utils.BaseTest;

import java.io.IOException;

public class tradeSteps extends BaseTest {

    AppPOManager appPoManager = loginSteps.appPOManager;
    TradeSymbolConfig tradeSymbolConfig = new TradeSymbolConfig();
    TradeRecord tradeRecord = new TradeRecord(appPoManager);

    @When("the user taps symbol on the app markets page")
    public void the_user_taps_symbol_on_the_app_markets_page() throws IOException {
        tradeRecord.tapSymbol();
    }

    @And("the user selects direction {string} on the app trade view")
    public void the_user_selects_direction_on_the_app_trade_view(String direction) {
        appPoManager.getAppTradeView().selectDirection(direction);

    }

    @And("the user switches on take profit and stop loss on the instrument details page")
    public void the_user_switches_on_take_profit_and_stop_loss_on_the_app_trade_view() throws InterruptedException {
        appPoManager.getAppInstrumentDetailsPage().switchProfitStopLoss();
    }

    @And("the user fills in the text field {string} with direction {string} on the instrument details page")
    public void the_user_fills_in_the_text_field_on_the_app_trade_view(String textFieldName, String direction) throws InterruptedException {
        Thread.sleep(8000);
        appPoManager.getAppInstrumentDetailsPage().fillInTextField(textFieldName, direction, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol),-25);
    }

    @And("the user taps button {string} on the instrument details page")
    public void the_user_taps_button_buy_on_the_app_trade_view(String buttonName) throws InterruptedException {
       // Thread.sleep(200);
        appPoManager.getAppInstrumentDetailsPage().tapsButton(buttonName);
    }

    @Then("the user sees the value {string} is displayed correctly with the user input value on the confirmation pop up")
    public void the_user_sees_the_value_is_displayed_correctly_with_the_user_input_value_on_the_confirmation_pop_up(String value) throws InterruptedException {
        Thread.sleep(2000);
        Assert.assertEquals(appPoManager.getAppTradeView().getDetailValue(value),
                appPoManager.getAppTradeView().getValidationValue(value));
        //appPoManager.getAppTradeView().closeConfirmationPopUp();
    }

    @And("the user fills in the text field {string} with value {string} on the instrument details page")
    public void the_user_fills_in_the_text_field_with_value_on_the_app_trade_view(String textFieldName, String value) throws InterruptedException {
        appPoManager.getAppInstrumentDetailsPage().fillValueIntoTextField(textFieldName, value);
        appPoManager.getAppInstrumentDetailsPage().setEstMargin(tradeSymbolConfig.getInitialMargin(AppMarketsPage.tradeSymbol));
    }

    @Then("the user sees a new open position is displayed at the position tab of app trade view")
    public void the_user_sees_a_new_open_position_is_displayed_at_the_position_tab_of_app_trade_view() throws InterruptedException {
        Thread.sleep(2000);
        Assert.assertTrue(appPoManager.getAppTradeView().getPosition());
        Assert.assertTrue(appPoManager.getAppTradeView().getPositionDetail(AppTradeView.selectedDirection));
        appPoManager.getAppTradeView().closePosition();

    }

    @And("the user taps button {string} on the confirmation pop up")
    public void the_user_taps_button_on_the_confirmation_pop_up(String buttonName) throws InterruptedException {
       // Thread.sleep(1000);
        appPoManager.getAppInstrumentDetailsPage().getExecutedPrice();
        appPoManager.getAppInstrumentDetailsPage().tapsButtonOnConfirm(buttonName);
    }

    @And("the user taps {string} cta button on the position tab of app trade view")
    public void the_user_taps_cta_button_on_the_position_tab_of_app_trade_view(String buttonName) {
        appPoManager.getAppTradeView().tapCtaButton(buttonName);
    }

    @And("the user selects order type {string} on the instrument details page")
    public void the_user_selects_order_type_on_the_app_trade_view(String orderType) throws InterruptedException {
        Thread.sleep(2000);
        appPoManager.getAppInstrumentDetailsPage().selectOrderType(orderType);
    }

    @And("the user selects stop limit order option {string} on the instrument details page")
    public void the_user_selects_stop_limit_order_option_as_detail_on_the_app_trade_view(String option) {
        appPoManager.getAppInstrumentDetailsPage().selectStopLimitOption(option);
    }

    @And("the user scrolls down the instrument details page")
    public void the_user_scrolls_down_the_app_trade_view() throws InterruptedException {
        Thread.sleep(500);
        appPoManager.getAppInstrumentDetailsPage().scrollDown();
    }

    @Then("the user sees a new pending order is displayed at the pending order tab of app trade view")
    public void the_user_sees_a_new_pending_order_is_displayed_at_the_pending_order_tab_of_app_trade_view() throws InterruptedException {
        Thread.sleep(2000);
        Assert.assertTrue(appPoManager.getAppTradeView().getPendingOrdersDetail(AppInstrumentDetailsPage.executedPrice));
        Assert.assertTrue(appPoManager.getAppTradeView().getPendingOrdersDetail(AppTradeView.selectedDirection));
        Assert.assertTrue(appPoManager.getAppTradeView().getPendingOrdersDetail(AppInstrumentDetailsPage.stopOrderType.split(" ")[1].trim()));
        appPoManager.getAppTradeView().cancelOrder();
    }

    @And("the user selects validity option {string} on the instrument details page")
    public void the_user_selects_validity_option_on_the_app_trade_view(String validity) {
        appPoManager.getAppInstrumentDetailsPage().selectValidity(validity);
    }

    @Then("the user sees stop order values are displayed correctly with the user input value on the confirmation pop up")
    public void the_user_sees_stop_order_values_are_displayed_correctly_with_the_user_input_value_on_the_confirmation_pop_up() throws InterruptedException {
        Thread.sleep(2000);
        for (String value : appPoManager.getAppTradeView().stopOrderConfirmationPageValues()) {
            Assert.assertEquals(appPoManager.getAppTradeView().getDetailValue(value),
                    appPoManager.getAppTradeView().getValidationValue(value));
        }
    }

    @Then("the user sees market order values are displayed correctly with the user input value on the confirmation pop up")
    public void the_user_sees_market_order_values_are_displayed_correctly_with_the_user_input_value_on_the_confirmation_pop_up() throws InterruptedException {
        Thread.sleep(2000);
        for (String value : appPoManager.getAppInstrumentDetailsPage().marketOrderConfirmationPageValues()) {
            Assert.assertEquals(appPoManager.getAppInstrumentDetailsPage().getDetailValue(value),
                    appPoManager.getAppInstrumentDetailsPage().getValidationValue(value));
        }
    }

    @Then("the user sees the message {string} is displayed at the dialogue")
    public void the_user_sees_the_message_is_displayed_at_the_dialogue(String dialogueMsg) throws InterruptedException {
        Thread.sleep(500);
        Assert.assertEquals(appPoManager.getAppTradeView().getDialogueTextAos(), dialogueMsg);
    }

    @And("the user edit price type {string} of the position on the instrument details page")
    public void the_user_edit_price_type_of_the_position_on_the_app_trade_view(String priceType) {
        appPoManager.getAppInstrumentDetailsPage().editTextField(priceType, AppTradeView.selectedDirection, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol));
    }

    @Then("the user sees market order values are displayed correctly with the user input value on the position details page")
    public void the_user_sees_market_order_values_are_displayed_correctly_with_the_user_input_value_on_the_position_details_page() throws InterruptedException {
        Thread.sleep(5000);
        for (String value : appPoManager.getAppTradeView().marketOrderConfirmationPageValues()) {
            Assert.assertEquals(appPoManager.getAppTradeView().getPositionValueWithRetry(value, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol)),
                    appPoManager.getAppTradeView().getValidationValue(value));
        }
        appPoManager.getAppTradeView().closePositionInDetails();
    }

    @And("the user edit price type {string} of the pending order on the instrument details page")
    public void the_user_edit_price_type_of_the_pending_order_on_the_app_trade_view(String priceType) {
        appPoManager.getAppInstrumentDetailsPage().editTextField(priceType, AppTradeView.selectedDirection, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol));
    }

    @Then("the user sees the value {string} is updated on the pending order details page")
    public void the_user_sees_the_value_is_updated_on_the_pending_order_details_page(String value) throws InterruptedException {
        Thread.sleep(500);
        Assert.assertEquals(appPoManager.getAppTradeView().getDetailValue(value), AppInstrumentDetailsPage.editPrice);
        appPoManager.getAppTradeView().cancelPendingOrderInDetail();
    }

    @Then("the user sees the pending order is disappeared on the pending order list")
    public void the_user_sees_the_pending_order_is_disappeared_on_the_pending_order_list() {
        Assert.assertFalse(appPoManager.getAppTradeView().getPendingOrder());
    }

    @And("the user places a pending order with direction {string} and order type {string} on the instrument details page")
    public void the_user_places_a_pending_order_with_direction_and_order_type_on_the_app_trade_view(String direction, String orderType) throws InterruptedException, IOException {
        tradeRecord.placePendingOrder(direction, orderType, tradeSymbolConfig);
    }

    @And("the user creates a {string} position on the instrument details page")
    public void the_user_creates_a_position_on_the_app_trade_view(String direction) throws IOException, InterruptedException {
        tradeRecord.createOpenPosition(direction);
    }

    @Then("the user sees the open position is disappeared on the position list")
    public void the_user_sees_the_open_position_is_disappeared_on_the_position_list(){
        Assert.assertFalse(appPoManager.getAppTradeView().getPosition());
    }

    @Then("the user sees expected default volume on the instrument details page")
    public void the_user_sees_expected_default_volume_on_the_instrument_details_page(){
        Assert.assertEquals(appPoManager.getAppInstrumentDetailsPage().getInputFieldValue("Lots"),"1.00");
    }

    @And("the user fills in the text field {string} with the value less than minimum on the instrument details page")
    public void the_user_fills_in_the_text_field_with_the_value_less_than_minimum_on_the_instrument_details_page(String textFieldName) throws InterruptedException {
        appPoManager.getAppInstrumentDetailsPage().setLotSize(String.valueOf(tradeSymbolConfig.getMinLotSize(AppMarketsPage.tradeSymbol)-0.01));
        appPoManager.getAppInstrumentDetailsPage().fillInTextField(textFieldName,AppTradeView.selectedDirection,tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol));
    }

    @And("the user fills in the text field {string} with the value more than maximum on the instrument details page")
    public void the_user_fills_in_the_text_field_with_the_value_more_than_maximum_on_the_instrument_details_page(String textFieldName) throws InterruptedException {
        appPoManager.getAppInstrumentDetailsPage().setLotSize(String.valueOf(tradeSymbolConfig.getMaxLotSize(AppMarketsPage.tradeSymbol)+0.01));
        appPoManager.getAppInstrumentDetailsPage().fillInTextField(textFieldName,AppTradeView.selectedDirection,tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol));
    }

    @Then("the user sees an error message {string} is displayed on the instrument details page")
    public void the_user_sees_an_error_message_is_displayed_on_the_instrument_details_page(String text){
        Assert.assertTrue(appPoManager.getAppInstrumentDetailsPage().getTextMessage(text));
    }

    @Then("the user sees the {string} value is displayed correctly on the instrument details page")
    public void the_user_sees_the_value_is_displayed_correctly_on_the_instrument_details_page(String label){
        Assert.assertEquals(appPoManager.getAppInstrumentDetailsPage().getValue(label,tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol)),
                appPoManager.getAppInstrumentDetailsPage().getValidationValue(label));
    }

    @Then("the user sees that the Take Profit and Stop Loss toggles are turned {string} on the instrument details page")
    public void the_user_sees_that_the_Take_Profit_and_Stop_Loss_toggles_are_turned_on_the_instrument_details_page(String toggleState){
        if (toggleState.equalsIgnoreCase("on")) {
            Assert.assertTrue(appPoManager.getAppInstrumentDetailsPage().getToggleStatus());
        }
        else {
            Assert.assertFalse(appPoManager.getAppInstrumentDetailsPage().getToggleStatus());
        }
    }

    @And("the user taps take profit and stop loss toggle on the instrument details page")
    public void the_user_taps_take_profit_and_stop_loss_toggle_on_the_instrument_details_page() throws InterruptedException {
        appPoManager.getAppInstrumentDetailsPage().switchProfitStopLoss();
    }

    @Then("the user sees the input field {string} is empty on the instrument details page")
    public void the_user_sees_the_input_field_is_empty_on_the_instrument_details_page(String inputField) throws InterruptedException {
        Thread.sleep(500);
        Assert.assertTrue(appPoManager.getAppInstrumentDetailsPage().getInputFieldValue(inputField).isEmpty());
    }

    @And("the user taps button {string} of the {string} input text field on the instrument details page")
    public void the_user_taps_button_of_the_input_text_field_on_the_instrument_details_page(String ctaBtn, String priceType) throws InterruptedException {
        Thread.sleep(2000);
        appPoManager.getAppInstrumentDetailsPage().adjustPrice(ctaBtn,priceType);
    }

    @Then("the user sees the {string} price is increased by {int} point on the instrument details page")
    public void the_user_sees_the_price_is_increased_by_point_on_the_instrument_details_page(String priceType,int point){
        Assert.assertEquals(appPoManager.getAppInstrumentDetailsPage().getInputFieldValue(priceType),
                String.format("%." + tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol) + "f", Float.parseFloat(appPoManager.getAppInstrumentDetailsPage().getValidationValue(priceType))+
                        point / Math.pow(10, Integer.parseInt(tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol)))));
    }

    @Then("the user sees the {string} price is decreased by {int} point on the instrument details page")
    public void the_user_sees_the_price_is_decreased_by_point_on_the_instrument_details_page(String priceType,int point){
        Assert.assertEquals(appPoManager.getAppInstrumentDetailsPage().getInputFieldValue(priceType),
                String.format("%." + tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol) + "f", Float.parseFloat(appPoManager.getAppInstrumentDetailsPage().getValidationValue(priceType))-
                        point / Math.pow(10, Integer.parseInt(tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol)))));
    }

    @And("the user fills in the text field {string} with direction {string} and the price is greater than current price minus BS point on the instrument details page")
    public void the_user_fills_in_the_text_field_and_the_price_is_greater_than_current_price_minus_BS_point_on_the_app_trade_view(String textFieldName, String direction) throws InterruptedException {
        Thread.sleep(2000);
        appPoManager.getAppInstrumentDetailsPage().fillInTextField(textFieldName, direction, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol),10);
    }

    @Then("the user sees the {string} price is populate to the input field on the instrument details page")
    public void the_user_sees_the_price_is_populate_to_the_input_field_on_the_instrument_details_page(String priceType){
        Assert.assertNotNull(appPoManager.getAppInstrumentDetailsPage().getInputFieldValue(priceType));
    }

    @And("the user fills in the text field {string} with direction {string} and the price is smaller than current price plus BS point on the instrument details page")
    public void the_user_fills_in_the_text_field_and_the_price_is_smaller_than_current_price_plus_BS_point_on_the_app_trade_view(String textFieldName, String direction) throws InterruptedException {
        Thread.sleep(2000);
        appPoManager.getAppInstrumentDetailsPage().fillInTextField(textFieldName, direction, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol),-10);
    }

}
