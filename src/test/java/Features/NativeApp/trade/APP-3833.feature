Feature: Native App trade

  @App @Smoke @Regression @Trade @EBL_MT5 @AppTest
  Scenario: Display an order type of sell limit order as limit on the pending order tab
    Given the user launch the app
    And the user logs in on the App login page
    And the user taps button "Markets" on the app footer
    When the user places a pending order with direction "SELL" and order type "Sell Limit" on the instrument details page
    Then the user sees order type "Limit" of pending order is displayed on the pending order tab


