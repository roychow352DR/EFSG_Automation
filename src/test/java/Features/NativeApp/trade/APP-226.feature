Feature: Native App AO Application

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: User can create sell market position successfully
      Given the user launch the app
      And the user lands on app home page
      And the user lands on the app login page
      And the user fills username "autol3" and password "Test1234@" on App login page
      And the user taps Login button on the app login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol "XAGUSD" on the app markets page
      And the user selects direction "SELL" on the app trade view
      And the user fills in the text field "Lot Size" with value "0.5" on the app trade view
      And the user taps button "SELL" on the app trade view
      And the user taps button "SELL" on the confirmation pop up
      Then the user sees a new open position is displayed at the position tab of instrument details page




