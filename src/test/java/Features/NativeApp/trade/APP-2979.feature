Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: Redirect user to the position details page from the history tab of portfolio page
    Given the user launch the app
    And the user login as username "autol3" and password "Test1234@" on App login page
    And the user taps button "Portfolio" on the app footer
    When the user selects tab "History" on the portfolio page
    And the user taps "arrow" button of the record row on the portfolio page
    Then the user sees header "Position Details" on the page