Feature: Native App trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: The validity is displayed as GTC in the pending order detail
    Given the user launch the app
    And the user logs in on the App login page
    And the user taps button "Markets" on the app footer
    And the user places a pending order with direction "BUY",validity "GTC" and order type "Buy Limit" on the instrument details page
    When the user taps "detail" cta button on the app trade view
    Then the user sees correct value "Validity" on the pending order details page




