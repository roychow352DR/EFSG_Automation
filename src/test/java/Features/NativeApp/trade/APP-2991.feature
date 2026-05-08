Feature: Native App trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: No trade confirmation dialogue is prompted upon pending order is cancelled
    Given the user launch the app
    And the user login as username "autol3" and password "Test1234@" on App login page
    And the user taps button "Me" on the app footer
    And the user toggles off trade confirmation on the app setting page
    And the user taps button "Markets" on the app footer
    And the user places a pending order with direction "BUY" and order type "Buy Stop" on the instrument details page
    When the user taps "close" cta button on the app trade view
    Then the user sees the pending order is disappeared on the pending order list




