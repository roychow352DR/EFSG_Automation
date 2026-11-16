Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: Take Profit & Stop Loss toggle off successfully on the modify order page
    Given the user launch the app
    And the user logs in on the App login page
    And the user taps button "Markets" on the app footer
    And the user places a pending order with direction "BUY" and order type "Buy Limit" on the instrument details page
    When the user taps "edit" cta button on the app trade view
    And the user switches on take profit and stop loss on the modify order page
    And the user switches off take profit and stop loss on the modify order page
    Then the user sees that the Take Profit and Stop Loss toggles are turned "off" on the modify order page
