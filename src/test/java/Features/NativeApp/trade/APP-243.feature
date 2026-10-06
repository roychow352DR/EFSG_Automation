Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: User can redirect to the Withdraw page after tapping the "Withdraw" button on the Portfolio page
    Given the user launch the app
    And the user login as username "autol3" and password "Test1234@" on App login page
    And the user taps button "Portfolio" on the app footer
    When the user taps button "Withdraw" on the portfolio page
    Then the user sees header "Withdraw" on the page