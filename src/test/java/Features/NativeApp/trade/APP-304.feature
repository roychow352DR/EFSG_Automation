Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest
    Scenario: User sees a cancel confirmation window after tapping Cancel on the pending orders page
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Me" on the app footer
      And the user taps button "Markets" on the app footer
      And the user places a pending order with direction "BUY" and order type "Buy Limit" on the instrument details page
      When the user taps "cancel" cta button on the app trade view
      Then the user sees a dialogue with title "Confirmation" is prompted on the app trade view




