Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: The Floating PnL is displayed correctly on the close position page of Gold
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Markets" on the app footer
      And the user creates a "BUY" position of symbol "XAUUSD" on the instrument details page
      When the user taps "close" cta button on the app trade view
      And the user taps button "Close Position" on the instrument details page
      Then the user sees correct value "Floating P/L" on the confirmation pop up of close position page




