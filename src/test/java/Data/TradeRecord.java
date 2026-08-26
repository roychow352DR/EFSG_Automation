package Data;

import PageObject.NativeApp.AppMarketsPage;
import PageObject.NativeApp.AppPOManager;
import PageObject.NativeApp.AppSettingPage;

import java.io.IOException;

import static utils.BaseTest.getProperty;

public class TradeRecord {

    private AppPOManager appPoManager;
    public final String LOT_SIZE = "0.5";
    public String VALIDITY = "Today";
    public static boolean isOpenPosition = false;

    public TradeRecord(AppPOManager appPOManager) {
        this.appPoManager = appPOManager;
    }

    public String getDefaultSymbol() throws IOException {
        String path = "//src//main//java//DataResources//TradeSymbol.properties";
        return System.getProperty("symbol") != null ?
                System.getProperty("symbol") : getProperty(path, "symbol");
    }

    public void placePendingOrder(String direction, String orderType, TradeSymbolConfig tradeSymbolConfig) throws InterruptedException, IOException {
        isOpenPosition = false;
        tapSymbol();
        selectDirection(direction);
        selectOrderType("Limit / Stop Order");
        fillLotSize();
        selectStopLimitOption(orderType);
        fillStopLimitPrice(direction, tradeSymbolConfig);
        confirmPlaceOrder(direction);
    }

    public void placePendingOrder(String direction, String orderType, TradeSymbolConfig tradeSymbolConfig,String symbol) throws InterruptedException, IOException {
        isOpenPosition = false;
        tapSymbol(symbol);
        selectDirection(direction);
        selectOrderType("Limit / Stop Order");
        fillLotSize();
        selectStopLimitOption(orderType);
        fillStopLimitPrice(direction, tradeSymbolConfig);
        confirmPlaceOrder(direction);
    }

    public void placeTPSLPendingOrder(String direction, String orderType, TradeSymbolConfig tradeSymbolConfig) throws InterruptedException, IOException {
        isOpenPosition = false;
        tapSymbol();
        selectDirection(direction);
        selectOrderType("Limit / Stop Order");
        fillLotSize();
        selectStopLimitOption(orderType);
        fillStopLimitPrice(direction, tradeSymbolConfig);
        selectValidity();
        switchStopLossOn();
        scrollPageDown();
        fillStopLossPrice(direction, tradeSymbolConfig);
        fillTakeProfitPrice(direction,tradeSymbolConfig);
        confirmPlaceOrder(direction);
    }

    public void createOpenPosition(String direction) throws IOException, InterruptedException {
        isOpenPosition = true;
        tapSymbol();
        selectDirection(direction);
        fillLotSize();
        confirmPlaceOrder(direction);
    }

    public void createOpenPosition(String direction,String symbol) throws IOException, InterruptedException {
        isOpenPosition = true;
        tapSymbol(symbol);
        selectDirection(direction);
        fillLotSize();
        confirmPlaceOrder(direction);
    }

    public void tapSymbol() throws IOException {
        appPoManager.getAppMarketsPage().tapSymbol(getDefaultSymbol());
    }

    public void tapSymbol(String symbol) throws IOException {
        appPoManager.getAppMarketsPage().tapSymbol(symbol);
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
        appPoManager.getAppInstrumentDetailsPage().tapsButton(direction);
        if (AppSettingPage.isTradeConfirmNeeded) {
            appPoManager.getAppInstrumentDetailsPage().getExecutedPrice();
            appPoManager.getAppInstrumentDetailsPage().tapsButtonOnConfirm(direction);
        }
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

    public void tradeSettingToggleOff(){
        appPoManager.getAppMePage().tapWidget("Setting");
        appPoManager.getAppSettingPage().tradeSettingsToggleOff();
        appPoManager.getAppSettingPage().tabBack();
    }

    public void tradeSettingToggleOn(){
        appPoManager.getAppMePage().tapWidget("Setting");
        appPoManager.getAppSettingPage().tradeSettingsToggleOn();
        appPoManager.getAppSettingPage().tabBack();
    }

    public void selectValidity(){
        appPoManager.getAppInstrumentDetailsPage().selectValidity(VALIDITY);
    }
}
