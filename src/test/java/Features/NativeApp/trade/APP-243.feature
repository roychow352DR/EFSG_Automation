Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: User can redirect to the Withdraw page after tapping the "Withdraw" button on the Portfolio page
    Given the user launch the app
    And the user logs in on the App login page
    And the user taps button "Portfolio" on the app footer
    When the user taps button "Withdraw" on the portfolio page
    Then the user sees header "Withdraw" on the page