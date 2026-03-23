Feature: Native App AO Application

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: User can cancel pending order successfully
    Given the user launch the app
    And the user login as username "autol3" and password "Test1234@" on App login page
    And the user taps button "Markets" on the app footer
    And the user places a pending order with direction "BUY" and order type "Buy Stop" on the app trade view
    When the user taps "close" cta button on the position tab of instrument details page
    And the user taps button "Cancel Order" on the app trade view
    Then the user sees the message "Your order is cancelled" is displayed at the dialogue
    And the user sees the pending order is disappeared on the pending order list




