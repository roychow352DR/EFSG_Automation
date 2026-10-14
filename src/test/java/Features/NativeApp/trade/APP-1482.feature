Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest
    Scenario: User sees an error message when the buy limit order price is greater than current price minus BS point
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user selects order type "Limit / Stop Order" on the instrument details page
      And the user fills in the text field "Price" with direction "BUY" and the price is greater than current price minus BS point on the instrument details page
      Then the user sees an error message "Invalid order price" is displayed on the instrument details page




