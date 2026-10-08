Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: Take Profit & Stop Loss toggle on successfully
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user switches on take profit and stop loss on the instrument details page
      Then the user sees that the Take Profit and Stop Loss toggles are turned "on" on the instrument details page




