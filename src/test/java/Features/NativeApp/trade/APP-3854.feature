Feature: Native App trade

  @App @Smoke @Regression @Trade @EBL_MT5 @AppTest
  Scenario: Display an order type of buy stop order as stop on the pending order tab
    Given the user launch the app
    And the user logs in on the App login page
    And the user taps button "Markets" on the app footer
    When the user places a pending order with direction "BUY" and order type "Buy Stop" on the instrument details page
    Then the user sees order type "Stop" of pending order is displayed on the pending order tab


