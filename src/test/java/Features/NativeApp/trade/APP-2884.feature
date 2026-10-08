Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: The Take Profit field is empty by default
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user taps take profit and stop loss toggle on the instrument details page
      Then the user sees the input field "Take Profit" is empty on the instrument details page


