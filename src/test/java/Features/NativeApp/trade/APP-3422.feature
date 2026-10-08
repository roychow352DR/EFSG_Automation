Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: The direction of the open position is displayed as open on the position details page
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      When the user creates a "BUY" position on the instrument details page
      And the user taps "detail" cta button on the app trade view
      Then the user sees correct value "Direction" on the position details page
