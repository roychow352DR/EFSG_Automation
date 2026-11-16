Feature: Native App trade

    @App @Smoke @Regression @Trade @EIEHK
    Scenario: User can see Buy stop order details
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      And the user places a pending order with direction "BUY" and order type "Buy Stop" on the instrument details page
      When the user taps "detail" cta button on the app trade view
      Then the user sees limit order values are displayed correctly with the user input value on the pending order details page


