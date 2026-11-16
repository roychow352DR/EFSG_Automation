Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: Redirect user to the modify order page from portfolio page
    Given the user launch the app
    And the user logs in on the App login page
    And the user taps button "Markets" on the app footer
    And the user places a pending order with direction "BUY" and order type "Buy Limit" on the instrument details page
    And the user taps back button on the app trade view
    When the user taps button "Portfolio" on the app footer
    And the user selects tab "Pending Orders" on the portfolio page
    And the user taps "edit" button of the record row on the portfolio page
    Then the user sees header "Modify Order" on the page