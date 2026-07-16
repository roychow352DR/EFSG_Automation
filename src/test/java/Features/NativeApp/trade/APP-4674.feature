Feature: Native App trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: The estimated margin of HKGHKD is displayed correctly at pending order details page if initial margin equals to 0
    Given the user launch the app
    And the user login as username "autol3qa" and password "Test1234@" on App login page
    And the initial margin is set to zero
    And the user taps button "Me" on the app footer
    And the user taps button "Markets" on the app footer
    And the user places a pending order with direction "BUY" and order type "Buy Stop" symbol "HKGHKD" on the instrument details page
    When the user taps "detail" cta button on the app trade view
    Then the user sees correct value "Estimated Margin" on the pending order details page




