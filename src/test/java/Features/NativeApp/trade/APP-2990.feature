Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: No trade confirmation dialogue is prompted upon pending order is placed
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Me" on the app footer
      And the user toggles off trade confirmation on the app setting page
      And the user taps button "Markets" on the app footer
      When the user taps symbol on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user selects order type "Limit / Stop Order" on the instrument details page
      And the user selects stop limit order option "Buy Stop" on the instrument details page
      And the user fills in the text field "Price" with direction "BUY" on the instrument details page
      And the user taps button "BUY" on the instrument details page
      Then the user sees a new pending order is displayed at the pending order tab of app trade view




