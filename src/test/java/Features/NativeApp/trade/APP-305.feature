Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest
    Scenario: User can redirect to the modify order page via pending order tab
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      And the user places a pending order with direction "BUY" and order type "Buy Limit" on the instrument details page
      When the user taps "edit" cta button on the app trade view
      Then the user sees header "Modify Order" on the page




