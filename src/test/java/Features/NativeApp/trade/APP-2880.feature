Feature: Native App AO Application

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: User sees an error message when the stop loss price of buy order is greater than current price minus BS point
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user fills in the text field "Lot Size" with value "0.5" on the instrument details page
      And the user switches on take profit and stop loss on the instrument details page
      And the user fills in the text field "Stop Loss" with direction "BUY" and the price is greater than current price minus BS point on the instrument details page
      Then the user sees an error message "Invalid stop loss price" is displayed on the instrument details page




