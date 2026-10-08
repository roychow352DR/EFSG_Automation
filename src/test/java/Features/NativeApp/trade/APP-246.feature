Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: User can redirect to the Records page after tapping the "Records" button on the Portfolio page
    Given the user launch the app
    And the user logs in on the App login page
    And the user taps button "Portfolio" on the app footer
    When the user taps button "Records" on the portfolio page
    Then the user sees header "Records" on the page
