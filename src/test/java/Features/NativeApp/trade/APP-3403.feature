Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: Validate the date format on open position tab
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Me" on the app footer
      And the user toggles on trade confirmation on the app setting page
      And the user taps button "Markets" on the app footer
      When the user creates a "BUY" position on the instrument details page
      Then the user sees the open position date is displayed as correct format on the open position tab
