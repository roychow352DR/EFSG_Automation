Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: Redirect user to the close position page from portfolio page
    Given the user launch the app
    And the user login as username "autol3" and password "Test1234@" on App login page
    And the user taps button "Markets" on the app footer
    And the user creates a "BUY" position on the instrument details page
    And the user taps back button on the app trade view
    When the user taps button "Portfolio" on the app footer
    And the user taps "close" button of the record row on the portfolio page
    Then the user sees header "Close Position" on the page