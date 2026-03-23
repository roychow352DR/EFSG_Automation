Feature: Native App AO Application

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: User sees sell Limit order Confirmation page info based on the input values in previous page
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol "XAUUSD" on the app markets page
      And the user selects direction "SELL" on the app trade view
      And the user selects order type "Limit / Stop Order" on the app trade view
      And the user fills in the text field "Lot Size" with value "0.5" on the app trade view
      And the user selects stop limit order option "Sell Limit" on the app trade view
      And the user selects validity option "GTC" on the app trade view
      And the user fills in the text field "Price" with direction "SELL" on the app trade view
      And the user switches on take profit and stop loss on the app trade view
      And the user scrolls down the app trade view
      And the user fills in the text field "Stop Loss" with direction "SELL" on the app trade view
      And the user fills in the text field "Take Profit" with direction "SELL" on the app trade view
      And the user taps button "SELL" on the app trade view
      Then the user sees stop order values are displayed correctly with the user input value on the confirmation pop up


