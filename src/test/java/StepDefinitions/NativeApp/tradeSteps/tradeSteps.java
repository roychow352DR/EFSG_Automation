package StepDefinitions.NativeApp.tradeSteps;

import Data.TradeRecord;
import Data.TradeSymbolConfig;
import PageObject.NativeApp.*;
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
        Thread.sleep(2000);
        appPoManager.getAppInstrumentDetailsPage().fillInTextField(textFieldName, direction, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol), 25);
    }

    @And("the user taps button {string} on the instrument details page")
    public void the_user_taps_button_buy_on_the_app_trade_view(String buttonName) throws InterruptedException {
        Thread.sleep(2000);
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

    @Then("the user sees an open position is disappeared on the position tab of app trade view")
    public void the_user_sees_an_open_position_is_disappeared_on_the_position_tab_of_app_trade_view() throws InterruptedException {
        Thread.sleep(2000);
        Assert.assertFalse(appPoManager.getAppTradeView().getPosition());
        Assert.assertFalse(appPoManager.getAppTradeView().getPositionDetail(AppTradeView.openPositionOpenPrice));
    }

    @And("the user taps button {string} on the confirmation pop up")
    public void the_user_taps_button_on_the_confirmation_pop_up(String buttonName) throws InterruptedException {
        Thread.sleep(500);
        appPoManager.getAppInstrumentDetailsPage().getExecutedPrice();
        appPoManager.getAppInstrumentDetailsPage().tapsButtonOnConfirm(buttonName);
    }

    @And("the user taps button {string} on the confirmation pop up of cancel pending order")
    public void the_user_taps_button_on_the_confirmation_pop_up_of_cancel_pending_order(String buttonName) throws InterruptedException {
        Thread.sleep(500);
        appPoManager.getAppInstrumentDetailsPage().getExecutedPrice();
        appPoManager.getAppTradeView().closeDialogue();
    }

    @And("the user taps {string} cta button on the app trade view")
    public void the_user_taps_cta_button_on_the_app_trade_view(String buttonName) throws InterruptedException {
        Thread.sleep(1000);
        if (TradeRecord.isOpenPosition) {
            appPoManager.getAppTradeView().getOpenPositionOpenPrice();
        } else {
            appPoManager.getAppTradeView().getPendingOrderTargetPrice();
        }
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
        for (String value : appPoManager.getAppTradeView().stopOrderConfirmationPageValues()) {
            Assert.assertEquals(appPoManager.getAppTradeView().getDetailValue(value),
                    appPoManager.getAppInstrumentDetailsPage().getValidationValue(value));
        }
        appPoManager.getAppInstrumentDetailsPage().closeConfirmation();
    }

    @Then("the user sees stop order values are displayed correctly with the user input value on the cancel order confirmation pop up")
    public void the_user_sees_stop_order_values_are_displayed_correctly_with_the_user_input_value_on_the_cancel_order_confirmation_pop_up() throws InterruptedException {
        Thread.sleep(2000);
        for (String value : appPoManager.getAppTradeView().stopOrderConfirmationPageValues()) {
            Assert.assertEquals(appPoManager.getAppTradeView().getDetailValue(value),
                    appPoManager.getAppInstrumentDetailsPage().getValidationValue(value));
        }
        appPoManager.getAppTradeView().closeDialogue();
        appPoManager.getAppTradeView().cancelOrder();
    }

    @Then("the user sees market order values are displayed correctly with the user input value on the confirmation pop up")
    public void the_user_sees_market_order_values_are_displayed_correctly_with_the_user_input_value_on_the_confirmation_pop_up() throws InterruptedException {
        Thread.sleep(2000);
        for (String value : appPoManager.getAppInstrumentDetailsPage().marketOrderConfirmationPageValues()) {
            Assert.assertEquals(appPoManager.getAppInstrumentDetailsPage().getDetailValue(value),
                    appPoManager.getAppInstrumentDetailsPage().getValidationValue(value));
        }
        appPoManager.getAppInstrumentDetailsPage().closeConfirmation();
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
        for (String value : appPoManager.getAppTradeView().marketOrderConfirmationPageValues()) {
            Assert.assertEquals(appPoManager.getAppTradeView().getPositionValueWithRetry(value, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol)),
                    appPoManager.getAppInstrumentDetailsPage().getValidationValue(value));
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
    public void the_user_sees_the_pending_order_is_disappeared_on_the_pending_order_list() throws InterruptedException {
        Thread.sleep(500);
        Assert.assertFalse(appPoManager.getAppTradeView().getPendingOrder());
        Assert.assertFalse(appPoManager.getAppTradeView().getPendingOrdersDetail(AppTradeView.openOrderTargetPrice));
    }

    @And("the user places a pending order with direction {string} and order type {string} on the instrument details page")
    public void the_user_places_a_pending_order_with_direction_and_order_type_on_the_app_trade_view(String direction, String orderType) throws InterruptedException, IOException {
        tradeRecord.placePendingOrder(direction, orderType, tradeSymbolConfig);
    }

    @And("the user creates a {string} position on the instrument details page")
    public void the_user_creates_a_position_on_the_app_trade_view(String direction) throws IOException, InterruptedException {
        tradeRecord.createOpenPosition(direction);
    }

    @And("the user creates a {string} position of symbol {string} on the instrument details page")
    public void the_user_creates_a_position_of_symbol_on_the_app_trade_view(String direction, String symbol) throws IOException, InterruptedException {
        tradeRecord.createOpenPosition(direction, symbol);
    }

    @Then("the user sees the open position is disappeared on the position list")
    public void the_user_sees_the_open_position_is_disappeared_on_the_position_list() {
        Assert.assertFalse(appPoManager.getAppTradeView().getPosition());
    }

    @Then("the user sees expected default volume on the instrument details page")
    public void the_user_sees_expected_default_volume_on_the_instrument_details_page() {
        Assert.assertEquals(appPoManager.getAppInstrumentDetailsPage().getInputFieldValue("Lots"), "1.00");
    }

    @And("the user fills in the text field {string} with the value less than minimum on the instrument details page")
    public void the_user_fills_in_the_text_field_with_the_value_less_than_minimum_on_the_instrument_details_page(String textFieldName) throws InterruptedException {
        appPoManager.getAppInstrumentDetailsPage().setLotSize(String.valueOf(tradeSymbolConfig.getMinLotSize(AppMarketsPage.tradeSymbol) - 0.01));
        appPoManager.getAppInstrumentDetailsPage().fillInTextField(textFieldName, AppTradeView.selectedDirection, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol));
    }

    @And("the user fills in the text field {string} with the value more than maximum on the instrument details page")
    public void the_user_fills_in_the_text_field_with_the_value_more_than_maximum_on_the_instrument_details_page(String textFieldName) throws InterruptedException {
        appPoManager.getAppInstrumentDetailsPage().setLotSize(String.valueOf(tradeSymbolConfig.getMaxLotSize(AppMarketsPage.tradeSymbol) + 0.01));
        appPoManager.getAppInstrumentDetailsPage().fillInTextField(textFieldName, AppTradeView.selectedDirection, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol));
    }

    @Then("the user sees an error message {string} is displayed on the instrument details page")
    public void the_user_sees_an_error_message_is_displayed_on_the_instrument_details_page(String text) {
        Assert.assertTrue(appPoManager.getAppInstrumentDetailsPage().getTextMessage(text));
    }

    @Then("the user sees the {string} value is displayed correctly on the instrument details page")
    public void the_user_sees_the_value_is_displayed_correctly_on_the_instrument_details_page(String label) {
        Assert.assertEquals(appPoManager.getAppInstrumentDetailsPage().getValue(label, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol)),
                appPoManager.getAppInstrumentDetailsPage().getValidationValue(label));
    }

    @Then("the user sees that the Take Profit and Stop Loss toggles are turned {string} on the instrument details page")
    public void the_user_sees_that_the_Take_Profit_and_Stop_Loss_toggles_are_turned_on_the_instrument_details_page(String toggleState) {
        if (toggleState.equalsIgnoreCase("on")) {
            Assert.assertTrue(appPoManager.getAppInstrumentDetailsPage().getToggleStatus());
        } else {
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
        appPoManager.getAppInstrumentDetailsPage().adjustPrice(ctaBtn, priceType);
    }

    @Then("the user sees the {string} price is increased by {int} point on the instrument details page")
    public void the_user_sees_the_price_is_increased_by_point_on_the_instrument_details_page(String priceType, int point) {
        Assert.assertEquals(appPoManager.getAppInstrumentDetailsPage().getInputFieldValue(priceType),
                String.format("%." + tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol) + "f", Float.parseFloat(appPoManager.getAppInstrumentDetailsPage().getValidationValue(priceType)) +
                        point / Math.pow(10, Integer.parseInt(tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol)))));
    }

    @Then("the user sees the {string} price is decreased by {int} point on the instrument details page")
    public void the_user_sees_the_price_is_decreased_by_point_on_the_instrument_details_page(String priceType, int point) {
        Assert.assertEquals(appPoManager.getAppInstrumentDetailsPage().getInputFieldValue(priceType),
                String.format("%." + tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol) + "f", Float.parseFloat(appPoManager.getAppInstrumentDetailsPage().getValidationValue(priceType)) -
                        point / Math.pow(10, Integer.parseInt(tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol)))));
    }

    @And("the user fills in the text field {string} with direction {string} and the price is greater than current price minus BS point on the instrument details page")
    public void the_user_fills_in_the_text_field_and_the_price_is_greater_than_current_price_minus_BS_point_on_the_app_trade_view(String textFieldName, String direction) throws InterruptedException {
        appPoManager.getAppInstrumentDetailsPage().fillInTextField(textFieldName, direction, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol), -10);
    }

    @Then("the user sees the {string} price is populate to the input field on the instrument details page")
    public void the_user_sees_the_price_is_populate_to_the_input_field_on_the_instrument_details_page(String priceType) {
        Assert.assertNotNull(appPoManager.getAppInstrumentDetailsPage().getInputFieldValue(priceType));
    }

    @And("the user fills in the text field {string} with direction {string} and the price is smaller than current price plus BS point on the instrument details page")
    public void the_user_fills_in_the_text_field_and_the_price_is_smaller_than_current_price_plus_BS_point_on_the_app_trade_view(String textFieldName, String direction) throws InterruptedException {
        appPoManager.getAppInstrumentDetailsPage().fillInTextField(textFieldName, direction, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol), -10);
    }

    @Then("the user sees the value {string} is displayed correctly on the edit position page")
    public void the_user_sees_the_value_is_displayed_correctly_on_the_edit_position_page(String value) throws InterruptedException {
        Assert.assertEquals(appPoManager.getAppTradeView().getPositionValueWithRetry(value, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol)),
                appPoManager.getAppInstrumentDetailsPage().getValidationValue(value));
        appPoManager.getAppTradeView().tapBack();
        appPoManager.getAppTradeView().closePosition();
    }

    @Then("the user sees the {string} input field is displayed empty by default on the edit position page")
    public void the_user_sees_the_input_field_is_displayed_empty_by_default_on_the_edit_position_page(String inputFieldName) {
        Assert.assertEquals(appPoManager.getAppEditPositionPage().getInputFieldValue(inputFieldName), "");
    }

    @And("the user taps button {string} of the {string} input text field on the edit position page")
    public void the_user_taps_button_of_the_input_text_field_on_the_edit_position_page(String ctaBtn, String inputField) {
        appPoManager.getAppEditPositionPage().adjustPrice(ctaBtn, inputField);
    }

    @Then("the user sees the {string} price is populate to the input field on the edit position page")
    public void the_user_sees_the_price_is_populate_to_the_input_field_on_the_edit_position_page(String inputField) {
        Assert.assertNotNull(appPoManager.getAppEditPositionPage().getInputFieldValue(inputField));
        appPoManager.getAppTradeView().tapBack();
        appPoManager.getAppTradeView().closePosition();
    }

    @And("the user fills in the text field {string} with direction {string} on the edit position page")
    public void the_user_fills_in_the_text_field_with_direction_on_the_edit_position_page(String textFieldName, String direction) {
        appPoManager.getAppEditPositionPage().fillInTextField(textFieldName, direction, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol), 25);
    }

    @Then("the user sees the {string} price is decreased by {int} point on the edit position page")
    public void the_user_sees_the_price_is_decreased_by_point_on_the_edit_position_page(String priceType, int point) {
        Assert.assertEquals(appPoManager.getAppEditPositionPage().getInputFieldValue(priceType),
                String.format("%." + tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol) + "f", Float.parseFloat(appPoManager.getAppEditPositionPage().getValidationValue(priceType)) -
                        point / Math.pow(10, Integer.parseInt(tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol)))));
        appPoManager.getAppTradeView().tapBack();
        appPoManager.getAppTradeView().closePosition();
    }

    @Then("the user sees the {string} price is increased by {int} point on the edit position page")
    public void the_user_sees_the_price_is_increased_by_point_on_the_edit_position_page(String priceType, int point) {
        Assert.assertEquals(appPoManager.getAppEditPositionPage().getInputFieldValue(priceType),
                String.format("%." + tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol) + "f", Float.parseFloat(appPoManager.getAppEditPositionPage().getValidationValue(priceType)) +
                        point / Math.pow(10, Integer.parseInt(tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol)))));
        appPoManager.getAppTradeView().tapBack();
        appPoManager.getAppTradeView().closePosition();
    }

    @Then("the user sees the input field {string} is empty on the edit position page")
    public void the_user_sees_the_input_field_is_empty_on_the_edit_position_page(String inputField) throws InterruptedException {
        Thread.sleep(500);
        Assert.assertTrue(appPoManager.getAppEditPositionPage().getInputFieldValue(inputField).isEmpty());
        appPoManager.getAppTradeView().tapBack();
        appPoManager.getAppTradeView().closePosition();
    }

    @And("the user taps button {string} on the edit position page")
    public void the_user_taps_button_on_the_edit_position_page(String buttonName) {
        appPoManager.getAppEditPositionPage().tapsButton(buttonName);
    }

    @Then("the user sees the value {string} is updated on the position details page")
    public void the_user_sees_the_value_is_updated_on_the_position_details_page(String value) throws InterruptedException {
        // Thread.sleep(500);
        Assert.assertEquals(appPoManager.getAppTradeView().getDetailValue(value), appPoManager.getAppEditPositionPage().getValidationValue(value));
        appPoManager.getAppTradeView().closePositionInDetails();


    }

    @And("the user fills in the text field {string} with direction {string} and the price is greater than current price minus BS point on the edit position page")
    public void the_user_fills_in_the_text_field_and_the_price_is_greater_than_current_price_minus_BS_point_on_the_edit_position_page(String textFieldName, String direction) throws InterruptedException {
        appPoManager.getAppEditPositionPage().fillInTextField(textFieldName, direction, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol), -10);
    }

    @Then("the user sees an error message {string} is displayed on the edit position page")
    public void the_user_sees_an_error_message_is_displayed_on_the_edit_position_page(String errorMsg) {
        Assert.assertTrue(appPoManager.getAppEditPositionPage().getTextMessage(errorMsg));
        appPoManager.getAppTradeView().tapBack();
        appPoManager.getAppTradeView().closePosition();
    }

    @And("the user fills in the text field {string} with direction {string} and the price is smaller than current price plus BS point on the edit position page")
    public void the_user_fills_in_the_text_field_and_the_price_is_smaller_than_current_price_plus_BS_point_on_the_edit_position_page(String textFieldName, String direction) throws InterruptedException {
        appPoManager.getAppEditPositionPage().fillInTextField(textFieldName, direction, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol), -10);
    }

    @When("the user taps button {string} on the portfolio page")
    public void the_user_taps_button_on_the_portfolio_page(String buttonText) {
        appPoManager.getAppPortfolioPage().clickButton(buttonText);
    }

    @Then("the user sees the portfolio filtering page with correct items displayed")
    public void the_user_sees_the_portfolio_filtering_page_with_correct_items_displayed() {
        System.out.println(appPoManager.getAppPortfolioPage().getTitleAos());
    }

    @Then("the user sees heading {string} on the portfolio filtering page")
    public void the_user_sees_heading_on_the_portfolio_filtering_page(String title) {
        Assert.assertEquals(appPoManager.getAppPortfolioPage().getTitleAos(), title);
    }

    @Then("the user sees the product {string} is selected on the portfolio filtering page")
    public void the_user_sees_the_product_is_selected_on_the_portfolio_filtering_page(String product) {
        Assert.assertEquals(appPoManager.getAppPortfolioPage().getCheckedProduct(), product);
    }

    @And("the user taps back button on the portfolio filtering page")
    public void the_user_taps_back_button_on_the_portfolio_filtering_page() {
        appPoManager.getAppPortfolioPage().tapBack();
    }

    @Then("the user sees {string} tab is selected on the portfolio page")
    public void the_user_sees_tab_is_selected_on_the_portfolio_page(String tabName) {
        Assert.assertTrue(appPoManager.getAppPortfolioPage().tabIsSelected(tabName));
    }

    @Then("the user sees a full volume lot size on the app close position page")
    public void the_user_sees_a_full_volume_lot_size_on_the_app_close_position_page() {
        Assert.assertEquals(appPoManager.getAppClosePositionPage().getEditFieldVal(), appPoManager.getAppInstrumentDetailsPage().getValidationValue("Volume"));
        appPoManager.getAppTradeView().tapBack();
        appPoManager.getAppTradeView().closePosition();
    }

    @When("the user taps button {string} on the close position page")
    public void the_user_taps_button_on_the_close_position_page(String btnName) {
        appPoManager.getAppClosePositionPage().clickBtn(btnName);
    }

    @Then("the user sees the lots value is decreased by default step size on the close position page")
    public void the_user_sees_the_lots_value_is_decreased_by_default_step_size_on_the_close_position_page() throws InterruptedException {
        Assert.assertEquals(appPoManager.getAppClosePositionPage().getEditFieldVal(),
                String.valueOf(Float.parseFloat(appPoManager.getAppInstrumentDetailsPage().getValidationValue("Volume")) - Float.parseFloat(tradeSymbolConfig.getStepSize())));
        appPoManager.getAppTradeView().tapBack();
        appPoManager.getAppTradeView().closePosition();
    }

    @Then("the user sees the lots value is increased by default step size on the close position page")
    public void the_user_sees_the_lots_value_is_increased_by_default_step_size_on_the_close_position_page() throws InterruptedException {
        Assert.assertEquals(appPoManager.getAppClosePositionPage().getEditFieldVal(), appPoManager.getAppInstrumentDetailsPage().getValidationValue("Volume"));
        appPoManager.getAppTradeView().tapBack();
        appPoManager.getAppTradeView().closePosition();
    }

    @And("the user places a TPSL pending order with direction {string} and order type {string} on the instrument details page")
    public void the_user_places_a_TPSL_pending_order_with_direction_and_order_type_on_the_instrument_details_page(String direction, String orderType) throws IOException, InterruptedException {
        tradeRecord.placeTPSLPendingOrder(direction, orderType, tradeSymbolConfig);
    }

    @And("the user edit price type {string} of the pending order on the modify order page")
    public void the_user_edit_price_type_of_the_pending_order_on_the_modify_order_page(String priceType) {
        appPoManager.getAppModifyOrderPage().editTextField(priceType, AppTradeView.selectedDirection, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol), 10);
    }

    @And("the user taps button {string} on the modify order page")
    public void the_user_taps_button_on_the_modify_order_page(String buttonName) {
        appPoManager.getAppModifyOrderPage().tapsButton(buttonName);
    }

    @And("the user scrolls down the modify order page")
    public void the_user_scrolls_down_the_modify_order_page() {
        appPoManager.getAppModifyOrderPage().scrollDown();
    }

    @Then("the user sees the value {string} is edited on the pending order details page")
    public void the_user_sees_the_value_is_edited_on_the_pending_order_details_page(String value) throws InterruptedException {
        Assert.assertEquals(appPoManager.getAppTradeView().getDetailValue(value), AppModifyOrderPage.editPrice);
        appPoManager.getAppTradeView().cancelPendingOrderInDetail();
    }

    @And("the user edit price type {string} of the pending order without acceptable range on the modify order page")
    public void the_user_edit_price_type_of_the_pending_order_without_acceptable_range_on_the_modify_order_page(String priceType) {
        appPoManager.getAppModifyOrderPage().editTextField(priceType, AppTradeView.selectedDirection, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol), -50);
    }

    @Then("the user sees an error message {string} is displayed on the modify order page")
    public void the_user_sees_an_error_message_is_displayed_on_the_modify_order_page(String errorMsg) throws InterruptedException {
        Assert.assertTrue(appPoManager.getAppModifyOrderPage().getTextMessage(errorMsg));
        appPoManager.getAppModifyOrderPage().tapBack();
        appPoManager.getAppTradeView().cancelOrder();
    }

    @When("the user selects tab {string} on the portfolio page")
    public void the_user_selects_tab_on_the_portfolio_page(String tabName) {
        appPoManager.getAppPortfolioPage().selectTab(tabName);
    }

    @And("the user taps {string} button of the record row on the portfolio page")
    public void the_user_taps_button_of_the_record_row_on_the_portfolio_page(String buttonName) {
        appPoManager.getAppPortfolioPage().tapButtonOnRow(buttonName);
    }

    @Then("the user sees {string} checkbox is unchecked on the confirmation pop up")
    public void the_user_sees_checkbox_is_unchecked_on_the_confirmation_pop_up(String checkboxLabel) {
        Assert.assertFalse(appPoManager.getAppInstrumentDetailsPage().getCheckboxStatus(checkboxLabel));
        appPoManager.getAppInstrumentDetailsPage().tapCross();
    }

    @And("the user toggles off trade confirmation on the app setting page")
    public void the_user_toggles_off_trade_confirmation_on_the_app_setting_page() {
        tradeRecord.tradeSettingToggleOff();
    }

    @And("the user toggles on trade confirmation on the app setting page")
    public void the_user_toggles_on_trade_confirmation_on_the_app_setting_page() {
        tradeRecord.tradeSettingToggleOn();
    }

    @Then("the user back to new order creation page")
    public void the_user_back_to_new_order_creation_page() {
        Assert.assertTrue(appPoManager.getAppInstrumentDetailsPage().getTpslToggleStatus());
    }

    @Then("the user sees close position page")
    public void the_user_sees_close_position_page() {
        Assert.assertTrue(appPoManager.getAppClosePositionPage().getHeader());
        appPoManager.getAppTradeView().tapBack();
        appPoManager.getAppTradeView().closePosition();
    }

    @Then("the user sees edit position page")
    public void the_user_sees_edit_position_page() throws InterruptedException {
        Thread.sleep(500);
        Assert.assertTrue(appPoManager.getAppEditPositionPage().getHeader());
        appPoManager.getAppTradeView().tapBack();
        appPoManager.getAppTradeView().closePosition();
    }

    @And("the user taps button {string} on the confirmation pop up of edit position page")
    public void the_user_taps_button_on_the_confirmation_pop_up_of_edit_position_page(String btnName) throws InterruptedException {
        appPoManager.getAppInstrumentDetailsPage().getExecutedPrice();
        appPoManager.getAppEditPositionPage().tapButtonOnDialogue(btnName);
    }

    @And("the user taps button {string} on the confirmation pop up of modify order page")
    public void the_user_taps_button_on_the_confirmation_pop_up_of_modify_order_page(String btnName) {
        appPoManager.getAppModifyOrderPage().tapButtonOnDialogue(btnName);
    }

    @Then("the user sees {string} page")
    public void the_user_sees_page(String pageName) {
        switch (pageName) {
            case "Edit Position" -> {
                Assert.assertTrue(appPoManager.getAppEditPositionPage().getHeader());
                appPoManager.getAppTradeView().tapBack();
                appPoManager.getAppTradeView().closePosition();
            }
            case "Close Position" -> {
                Assert.assertTrue(appPoManager.getAppClosePositionPage().getHeader());
                appPoManager.getAppTradeView().tapBack();
                appPoManager.getAppTradeView().closePosition();
            }
            case "Modify Order" -> {
                Assert.assertTrue(appPoManager.getAppModifyOrderPage().getHeader());
                appPoManager.getAppModifyOrderPage().tapBack();
                appPoManager.getAppTradeView().cancelOrder();
            }
        }
    }

    @When("the user taps symbol {string} on the app markets page")
    public void the_user_taps_symbol_on_the_app_markets_page(String symbol) {
        appPoManager.getAppMarketsPage().tapSymbol(symbol);
    }

    @Then("the user sees correct value {string} on the confirmation pop up of close position page")
    public void the_user_sees_correct_value_on_the_confirmation_pop_up_of_close_position_page(String label) throws InterruptedException {
        Thread.sleep(1000);
        Assert.assertEquals(appPoManager.getAppClosePositionPage().getDetailValue(label),
                appPoManager.getAppClosePositionPage().getFloatingPnL(tradeSymbolConfig.getContractSize(AppMarketsPage.tradeSymbol)));
        appPoManager.getAppClosePositionPage().confirmPositionClose();
    }

    @And("the initial margin is set to zero")
    public void the_initial_margin_is_set_to_zero() {
        TradeSymbolConfig.isInitialMarginZero = true;
    }

    @Then("the user sees correct value {string} on the position details page")
    public void the_user_sees_correct_value_on_the_position_details_page(String valueName) throws InterruptedException {
        if (valueName.equalsIgnoreCase("Contract Value")) {
            Assert.assertEquals(appPoManager.getAppPositionDetailsPage().getDetailValue(valueName),
                    appPoManager.getAppPositionDetailsPage().getContractValue(tradeSymbolConfig.getContractSize(AppMarketsPage.tradeSymbol)));
        } else if (valueName.equalsIgnoreCase("Initial Margin")) {
            Assert.assertEquals(appPoManager.getAppPositionDetailsPage().getDefaultInitialMargin(),
                    String.format("%.2f", Float.parseFloat(String.valueOf(tradeSymbolConfig.getInitialMargin(AppMarketsPage.tradeSymbol)))));
        } else {
            Assert.assertEquals(appPoManager.getAppPositionDetailsPage().getDetailValue(valueName),
                    appPoManager.getAppPositionDetailsPage().getValidationValue(valueName));
        }
        appPoManager.getAppTradeView().closePositionInDetails();
    }

    @And("the user places a pending order with direction {string} and order type {string} symbol {string} on the instrument details page")
    public void the_user_places_a_pending_order_with_direction_and_order_type_symbol_on_the_instrument_details_page(String direction, String orderType, String symbol) throws IOException, InterruptedException {
        tradeRecord.placePendingOrder(direction, orderType, tradeSymbolConfig, symbol);
    }

    @Then("the user sees correct value {string} on the pending order details page")
    public void the_user_sees_correct_value_on_the_pending_order_details_page(String valueName) throws InterruptedException {
        if (valueName.equalsIgnoreCase("Contract Value")) {
            Assert.assertEquals(appPoManager.getAppPendingOrderDetailsPage().getDetailValue(valueName),
                    appPoManager.getAppPendingOrderDetailsPage().getContractValue(tradeSymbolConfig.getContractSize(AppMarketsPage.tradeSymbol)));
        } else if (valueName.equalsIgnoreCase("Initial Margin")) {
            Assert.assertEquals(appPoManager.getAppPositionDetailsPage().getDefaultInitialMargin(),
                    String.format("%.2f", Float.parseFloat(String.valueOf(tradeSymbolConfig.getInitialMargin(AppMarketsPage.tradeSymbol)))));
        } else if (valueName.equalsIgnoreCase("Estimated Margin")) {
            Assert.assertEquals(appPoManager.getAppPendingOrderDetailsPage().getDetailValue(valueName),
                    String.format("%.2f", Float.parseFloat(appPoManager.getAppPendingOrderDetailsPage().getEstimatedMarin(tradeSymbolConfig.getInitialMargin(AppMarketsPage.tradeSymbol),
                            tradeSymbolConfig.getContractSize(AppMarketsPage.tradeSymbol)))));
        } else {
            Assert.assertEquals(appPoManager.getAppPendingOrderDetailsPage().getDetailValue(valueName),
                    appPoManager.getAppPendingOrderDetailsPage().getValidationValue(valueName));
        }
        appPoManager.getAppTradeView().cancelPendingOrderInDetail();
    }

    @Then("the user sees the open position date is displayed as correct format on the open position tab")
    public void the_user_sees_the_open_position_date_is_displayed_as_correct_format_on_the_open_position_tab() throws InterruptedException {
        Assert.assertTrue(appPoManager.getAppTradeView().isOpenPositionDateValid());
        appPoManager.getAppTradeView().closePosition();
    }

    @And("the user taps back button on the app trade view")
    public void the_user_taps_back_button_on_the_app_trade_view() {
        appPoManager.getAppTradeView().tapBack();
    }

    @Then("the user sees confirmation dialogue on the portfolio page")
    public void the_user_sees_confirmation_dialogue_on_the_portfolio_page() {
        Assert.assertTrue(appPoManager.getAppPortfolioPage().confirmationDialogueIsDisplayed());
        appPoManager.getAppPortfolioPage().tapButtonOnPortfolioPage("Cancel Order");
    }

    @Then("the user sees the open position date is displayed as correct format on the position details page")
    public void the_user_sees_the_open_position_date_is_displayed_as_correct_format_on_the_position_details_page() throws InterruptedException {
        Assert.assertTrue(appPoManager.getAppPositionDetailsPage().isOpenPositionDateValid());
        appPoManager.getAppTradeView().closePositionInDetails();
    }

    @Then("the user sees the last update time is displayed as correct format on the portfolio page")
    public void the_user_sees_the_last_update_time_is_displayed_as_correct_format_on_the_portfolio_page() throws InterruptedException {
        Assert.assertTrue(appPoManager.getAppPortfolioPage().isPendingOrderDateValid());
        appPoManager.getAppPortfolioPage().cancelPendingOrder();
    }

    @Then("the user sees the value {string} is displayed correctly on the portfolio page")
    public void the_user_sees_the_value_is_displayed_correctly_on_the_portfolio_page(String value) {
        Assert.assertTrue(appPoManager.getAppPortfolioPage().isValueDisplayedCorrect(appPoManager.getAppPortfolioPage().getValidationValue(value)));
        appPoManager.getAppPortfolioPage().cancelPendingOrder();
    }
}
