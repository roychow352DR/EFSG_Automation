Feature: Native App trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest
  Scenario: The default volume is correct
    Given the user launch the app
    And the user logs in on the App login page
    When the user taps button "Markets" on the app footer
    And the user taps symbol on the app markets page
    And the user selects direction "BUY" on the app trade view
    Then the user sees expected default volume on the instrument details page



