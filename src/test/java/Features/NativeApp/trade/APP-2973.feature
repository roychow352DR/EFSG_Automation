Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: User can modify take profit price of the TPSL pending order
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Markets" on the app footer
      And the user places a TPSL pending order with direction "BUY" and order type "Buy Limit" on the instrument details page
      When the user taps "edit" cta button on the app trade view
      And the user scrolls down the modify order page
      And the user edit price type "Take Profit" of the pending order on the modify order page
      And the user taps button "Modify Order" on the modify order page
      And the user taps button "Modify Order" on the confirmation pop up
      And the user taps "detail" cta button on the app trade view
      Then the user sees the value "Take Profit Price" is edited on the pending order details page
