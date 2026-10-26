Feature: Native App trade


  Scenario: The estimated margin is displayed correctly on the stop limit pending order details page when initial margin bigger than 0
    Given the user launch the app
    And the user logs in on the App login page
    And the user taps button "Markets" on the app footer
    And the user places a pending order with direction "BUY" and order type "Buy Stop" on the instrument details page
    When the user taps "detail" cta button on the app trade view
    Then the user sees correct value "Estimated Margin" on the pending order details page


