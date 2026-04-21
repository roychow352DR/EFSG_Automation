Feature: Native App trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: The default volume is 1
    Given the user launch the app
    When the user login as username "autol3" and password "Test1234@" on App login page
    And the user taps button "Markets" on the app footer
    And the user taps symbol on the app markets page
    And the user selects direction "BUY" on the app trade view
    Then the user sees expected default volume on the instrument details page



