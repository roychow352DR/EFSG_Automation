Feature: Native App AO Application

    @App @Smoke @Regression @AO @EBL_MT5 @EIEHK @XPro
    Scenario: L2 user sees AO entry point on the Markets page
      Given the user launch the app
      And the user lands on app home page
      And the user lands on the app login page
      When the user fills username "autol2" and password "Test1234@" on App login page
      And the user taps Login button on the app login page
      And the user taps button "Markets" on the app footer
      And the user taps button "Open a Live Trading Accounts" on the app markets page
      Then the user sees client agreement page


