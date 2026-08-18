Feature: Native App trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: User can cancel pending order successfully
    Given the user launch the app
    And the user login as username "autol3" and password "Test1234@" on App login page
    And the user taps button "Markets" on the app footer
    And the user taps symbol on the app markets page
    And the user selects tab "Positions" on the app trade view
    And the total count of the pending order is retrieved on the app trade view
    And the user taps back button on the app trade view
    And the user places a pending order with direction "BUY" and order type "Buy Stop" on the instrument details page
    When the user taps "close" cta button on the app trade view
    And the user taps button "Cancel Order" on the instrument details page
    Then the user sees the message "Your order is cancelled" is displayed at the dialogue
    And the user sees the pending order is disappeared on the pending order list




