Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: The format of the last update time is displayed correctly on the portfolio pending orders tab
    Given the user launch the app
    And the user login as username "autol3" and password "Test1234@" on App login page
    And the user taps button "Markets" on the app footer
    And the user places a pending order with direction "BUY" and order type "Buy Limit" on the instrument details page
    And the user taps back button on the app trade view
    And the user taps button "Portfolio" on the app footer
    When the user selects tab "Pending Orders" on the portfolio page
    Then the user sees correct value "Product Name" on the pending order details page