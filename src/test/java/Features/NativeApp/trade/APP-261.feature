Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: User can redirect to the Deposit page after tapping the "Deposit" button on the Portfolio page
    Given the user launch the app
    And the user login as username "autol3" and password "Test1234@" on App login page
    And the user taps button "Portfolio" on the app footer
    When the user taps button "Deposit" on the portfolio page
    Then the user sees header "Deposit" on the page