Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: Back to pending order tab after the back button is tapped on portfolio filtering page
    Given the user launch the app
    And the user logs in on the App login page
    And the user taps button "Portfolio" on the app footer
    And the user selects tab "Pending Orders" on the portfolio page
    When the user taps button "Show all" on the portfolio page
    And the user taps back button on the portfolio filtering page
    Then the user sees "Pending Orders" tab is selected on the portfolio page