package Data;

import PageObject.NativeApp.AppMarketsPage;
import PageObject.NativeApp.AppPOManager;

import java.io.IOException;

import static utils.BaseTest.getProperty;

public class TradeRecord {

    private AppPOManager appPoManager;
    public final String LOT_SIZE = "1";

    public TradeRecord(AppPOManager appPOManager) {
        this.appPoManager = appPOManager;
    }

    public String getDefaultSymbol() throws IOException {
        String path = "//src//main//java//DataResources//TradeSymbol.properties";
        return System.getProperty("symbol") != null ?
                System.getProperty("symbol") : getProperty(path, "symbol");
    }

    public void placePendingOrder(String direction, String orderType, TradeSymbolConfig tradeSymbolConfig) throws InterruptedException, IOException {
        tapSymbol();
        selectDirection(direction);
        selectOrderType("Limit / Stop Order");
        fillLotSize();
        selectStopLimitOption(orderType);
        fillStopLimitPrice(direction, tradeSymbolConfig);
        confirmPlaceOrder(direction);
    }

    public void placeTPSLPendingOrder(String direction, String orderType, TradeSymbolConfig tradeSymbolConfig) throws InterruptedException, IOException {
        tapSymbol();
        selectDirection(direction);
        selectOrderType("Limit / Stop Order");
        fillLotSize();
        selectStopLimitOption(orderType);
        fillStopLimitPrice(direction, tradeSymbolConfig);
        switchStopLossOn();
        scrollPageDown();
        fillStopLossPrice(direction, tradeSymbolConfig);
        fillTakeProfitPrice(direction,tradeSymbolConfig);
        confirmPlaceOrder(direction);
    }

    public void createOpenPosition(String direction) throws IOException, InterruptedException {
        tapSymbol();
        selectDirection(direction);
        fillLotSize();
        confirmPlaceOrder(direction);
    }

    public void tapSymbol() throws IOException {
        appPoManager.getAppMarketsPage().tapSymbol(getDefaultSymbol());
    }

    public void selectDirection(String direction) {
        appPoManager.getAppTradeView().selectDirection(direction);
    }

    public void selectOrderType(String orderType) throws InterruptedException {
        appPoManager.getAppInstrumentDetailsPage().selectOrderType(orderType);
    }

    public void fillLotSize() throws InterruptedException {
        appPoManager.getAppInstrumentDetailsPage().fillValueIntoTextField("Lot Size", LOT_SIZE);
    }

    public void selectStopLimitOption(String stopLimitOption) {
        appPoManager.getAppInstrumentDetailsPage().selectStopLimitOption(stopLimitOption);
    }

    public void fillStopLimitPrice(String direction, TradeSymbolConfig tradeSymbolConfig) throws InterruptedException, IOException {
        Thread.sleep(500);
        appPoManager.getAppInstrumentDetailsPage().fillInTextField("Price", direction, tradeSymbolConfig.getDecimalPlace(getDefaultSymbol()));
    }

    public void confirmPlaceOrder(String direction) throws InterruptedException {
        Thread.sleep(200);
        appPoManager.getAppInstrumentDetailsPage().tapsButton(direction);
        Thread.sleep(500);
        appPoManager.getAppInstrumentDetailsPage().getExecutedPrice();
        appPoManager.getAppInstrumentDetailsPage().tapsButtonOnConfirm(direction);
    }

    public void fillStopLossPrice(String direction,TradeSymbolConfig tradeSymbolConfig){
        appPoManager.getAppInstrumentDetailsPage().fillInTextField("Stop Loss", direction, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol),25);
    }

    public void fillTakeProfitPrice(String direction,TradeSymbolConfig tradeSymbolConfig){
        appPoManager.getAppInstrumentDetailsPage().fillInTextField("Take Profit", direction, tradeSymbolConfig.getDecimalPlace(AppMarketsPage.tradeSymbol),25);
    }

    public void switchStopLossOn() throws InterruptedException {
        appPoManager.getAppInstrumentDetailsPage().switchProfitStopLoss();
    }

    public void scrollPageDown(){
        appPoManager.getAppInstrumentDetailsPage().scrollDown();
    }
}
