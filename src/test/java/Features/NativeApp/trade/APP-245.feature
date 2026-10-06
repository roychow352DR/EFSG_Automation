Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: User can redirect to the Portfolio page after tapping the back button on the Withdraw page
    Given the user launch the app
    And the user login as username "autol3" and password "Test1234@" on App login page
    And the user taps button "Portfolio" on the app footer
    When the user taps button "Withdraw" on the portfolio page
    And the user taps back button on the withdraw page
    Then the user lands on app portfolio page