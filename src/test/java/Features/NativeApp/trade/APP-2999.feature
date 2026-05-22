Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: Back to pending order tab after the confirmation dialogue of cancel pending order is closed
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Me" on the app footer
      And the user toggles on trade confirmation on the app setting page
      And the user taps button "Markets" on the app footer
      And the user places a pending order with direction "BUY" and order type "Buy Limit" on the instrument details page
      When the user taps "close" cta button on the app trade view
      And the user taps button "x" on the confirmation pop up of cancel pending order
      And the user taps "detail" cta button on the app trade view
      Then the user is redirected to the "Pending Order Details" on the page
