Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: User sees correct details in cancel pending order confirmation pop up
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Me" on the app footer
      And the user toggles on trade confirmation on the app setting page
      And the user taps button "Markets" on the app footer
      And the user places a TPSL pending order with direction "BUY" and order type "Buy Limit" on the instrument details page
      When the user taps "close" cta button on the app trade view
      Then the user sees stop order values are displayed correctly with the user input value on the cancel order confirmation pop up


