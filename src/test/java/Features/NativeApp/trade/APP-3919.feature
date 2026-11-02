Feature: Native App trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: The take profit and stop loss price are displayed correctly on the pending order detail page 
    Given the user launch the app
    And the user logs in on the App login page
    And the user taps button "Markets" on the app footer
    When the user places a TPSL pending order with direction "BUY" and order type "Buy Limit" on the instrument details page
    And the user taps "detail" cta button on the app trade view
    Then the user sees correct value "TP/SL" on the pending order details page


