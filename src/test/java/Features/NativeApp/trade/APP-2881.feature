Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest
    Scenario: User can create sell market position successfully upon stop loss price is greater than market price plus BS point
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol on the app markets page
      And the user selects tab "Positions" on the app trade view
      And the total count of the positions is retrieved on the app trade view
      And the user taps back button on the app trade view
      And the user taps symbol on the app markets page
      And the user selects direction "SELL" on the app trade view
      And the user fills in the text field "Lot Size" with value "0.5" on the instrument details page
      And the user switches on take profit and stop loss on the instrument details page
      And the user fills in the text field "Stop Loss" with direction "SELL" on the instrument details page
      And the user taps button "SELL" on the instrument details page
      And the user taps button "SELL" on the confirmation pop up
      Then the user sees a new open position is displayed at the position tab of app trade view




