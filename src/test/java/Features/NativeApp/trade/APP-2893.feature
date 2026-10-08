Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest
    Scenario: User sees an error message when the take profit price of buy order is smaller than current price plus BS point
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user switches on take profit and stop loss on the instrument details page
      And the user fills in the text field "Take Profit" with direction "BUY" and the price is smaller than current price plus BS point on the instrument details page
      Then the user sees an error message "Invalid limit profit price" is displayed on the instrument details page




