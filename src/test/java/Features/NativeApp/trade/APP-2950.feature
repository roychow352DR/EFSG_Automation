Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: The lot value can be adjust by tapping minus button on Close Position page
    Given the user launch the app
    And the user login as username "autol3" and password "Test1234@" on App login page
    And the user taps button "Markets" on the app footer
    And the user creates a "BUY" position on the instrument details page
    When the user taps "close" cta button on the app trade view
    And the user taps button "-" on the close position page
    Then the user sees the lots value is decreased by default step size on the close position page
