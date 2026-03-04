Feature: Native App AO Application

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: User sees Buy Stop order Confirmation page info based on the input values in previous page
      Given the user launch the app
      And the user lands on app home page
      And the user lands on the app login page
      And the user fills username "autol3" and password "Test1234@" on App login page
      And the user taps Login button on the app login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol "XAUUSD" on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user selects order type "Limit / Stop Order" on the app trade view
      And the user fills in the text field "Lot Size" with value "0.5" on the app trade view
      And the user selects stop limit order option "Buy Stop" on the app trade view
      And the user selects validity option "Today" on the app trade view
      And the user fills in the text field "Price" with direction "BUY" on the app trade view
      And the user switches on take profit and stop loss on the app trade view
      And the user scrolls down the app trade view
      And the user fills in the text field "Stop Loss" with direction "BUY" on the app trade view
      And the user fills in the text field "Take Profit" with direction "BUY" on the app trade view
      And the user taps button "BUY" on the app trade view
      Then the user sees stop order values are displayed correctly with the user input value on the confirmation pop up


