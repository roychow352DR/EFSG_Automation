package StepDefinitions.NativeApp.tradeSteps;

import PageObject.NativeApp.AppPOManager;
import io.cucumber.java.en.And;
import StepDefinitions.NativeApp.login.loginSteps;
import io.cucumber.java.en.When;
import io.cucumber.java.en_scouse.An;

public class tradeSteps {

    AppPOManager appPoManager = loginSteps.appPOManager;

    @When("the user taps symbol {string} on the app markets page")
    public void the_user_taps_symbol_on_the_app_markets_page(String symbol) {
        appPoManager.getAppMarketsPage().tapSymbol(symbol);
    }

    @And("the user selects direction {string} on the app trade view")
    public void the_user_selects_direction_on_the_app_trade_view(String direction) {
        appPoManager.getAppTradeView().selectDirection(direction);
        System.out.println(appPoManager.getAppTradeView().getStopLossPrice());
        System.out.println(appPoManager.getAppTradeView().getTakeProfitPrice());
    }

    @And("the user switches on take profit and stop loss on the app trade view")
    public void the_user_switches_on_take_profit_and_stop_loss_on_the_app_trade_view(){
        appPoManager.getAppTradeView().switchProfitStopLoss();
    }
}
