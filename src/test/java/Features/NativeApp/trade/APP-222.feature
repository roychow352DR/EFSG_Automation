Feature: Native App AO Application

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: User sees market Buy order Confirmation page info based on the input values in previous page
      Given the user launch the app
      And the user lands on app home page
      And the user lands on the app login page
      And the user fills username "autol3" and password "Test1234@" on App login page
      And the user taps Login button on the app login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol "XAUUSD" on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user switches on take profit and stop loss on the app trade view
      Then the user sees client agreement page


