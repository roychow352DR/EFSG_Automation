Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest
    Scenario: Prompted error message upon the TPSL pending order is modified with unacceptable range of stop loss price
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Markets" on the app footer
      And the user places a TPSL pending order with direction "BUY" and order type "Buy Limit" on the instrument details page
      When the user taps "edit" cta button on the app trade view
      And the user scrolls down the modify order page
      And the user edit price type "Stop Loss" of the pending order without acceptable range on the modify order page
      Then the user sees an error message "Invalid stop loss price" is displayed on the modify order page
