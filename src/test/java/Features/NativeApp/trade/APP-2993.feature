Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest
    Scenario: Back to new order creation page after the trade confirmation dialogue of market order is closed
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Me" on the app footer
      And the user toggles on trade confirmation on the app setting page
      And the user taps button "Markets" on the app footer
      When the user taps symbol on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user taps button "BUY" on the instrument details page
      And the user taps button "x" on the confirmation pop up
      Then the user back to new order creation page



