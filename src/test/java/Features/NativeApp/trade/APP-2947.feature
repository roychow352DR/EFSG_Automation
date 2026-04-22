Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: Redirect user to the filtering page upon Show All is tapped on portfolio page
    Given the user launch the app
    And the user login as username "autol3" and password "Test1234@" on App login page
    And the user taps button "Portfolio" on the app footer
    When the user taps button "Show all" on the portfolio page
    Then the user sees heading "Show" on the portfolio filtering page
    And the user sees the product "All" is selected on the portfolio filtering page