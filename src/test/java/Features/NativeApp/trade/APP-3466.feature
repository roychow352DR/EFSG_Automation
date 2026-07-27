Feature: Native App trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: The direction is displayed correctly on the sell limit pending order details page
    Given the user launch the app
    And the user login as username "autol3" and password "Test1234@" on App login page
    And the user taps button "Markets" on the app footer
    And the user places a pending order with direction "SELL" and order type "Sell Limit" on the instrument details page
    When the user taps "detail" cta button on the app trade view
    Then the user sees correct value "Direction" on the pending order details page


