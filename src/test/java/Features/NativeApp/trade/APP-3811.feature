Feature: Native App trade


  Scenario: User can cancel stop pending order successfully
    Given the user launch the app
    And the user logs in on the App login page
    And the user taps button "Markets" on the app footer
    And the user places a pending order with direction "BUY" and order type "Buy Stop" on the instrument details page
    When the user taps "close" cta button on the app trade view
    And the user taps button "Cancel Order" on the instrument details page
    Then the user sees the message "Your order is cancelled" is displayed at the dialogue
    And the user sees the pending order is disappeared on the pending order list


