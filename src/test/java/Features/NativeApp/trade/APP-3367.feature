Feature: Native App trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: The Contract value is displayed correctly on the pending order details page of RKGCNH
    Given the user launch the app
    And the user login as username "autol3" and password "Test1234@" on App login page
    And the user taps button "Me" on the app footer
    And the user taps button "Markets" on the app footer
    And the user places a pending order with direction "BUY" and order type "Buy Stop" symbol "RKGCNH" on the instrument details page
    When the user taps "detail" cta button on the app trade view
    Then the user sees correct value "Contract Value" on the pending order details page




