Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: User can redirect to the Deposit page after tapping the "Deposit" button on the Portfolio page
    Given the user launch the app
    And the user logs in on the App login page
    And the user taps button "Portfolio" on the app footer
    When the user taps button "Deposit" on the portfolio page
    Then the user sees header "Deposit" on the page