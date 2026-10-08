Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: User can redirect to the Portfolio page after tapping the back button on the Deposit page
    Given the user launch the app
    And the user logs in on the App login page
    And the user taps button "Portfolio" on the app footer
    When the user taps button "Deposit" on the portfolio page
    And the user taps back button on the deposit page
    Then the user lands on app portfolio page