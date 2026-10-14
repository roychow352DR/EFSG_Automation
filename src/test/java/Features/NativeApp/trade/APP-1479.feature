Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: User can place TPSL buy limit pending order with stop loss price only
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user selects order type "Limit / Stop Order" on the instrument details page
      And the user fills in the text field "Lot Size" with value "0.5" on the instrument details page
      And the user fills in the text field "Price" with direction "BUY" on the instrument details page
      And the user switches on take profit and stop loss on the instrument details page
      And the user fills in the text field "Stop Loss" with direction "BUY" and the price is smaller than current price minus BS point on the instrument details page
      And the user taps button "BUY" on the instrument details page
      And the user taps button "BUY" on the confirmation pop up
      Then the user sees a new pending order is displayed at the pending order tab of app trade view




