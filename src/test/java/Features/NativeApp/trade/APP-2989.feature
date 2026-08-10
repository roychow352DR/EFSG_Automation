Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: No trade confirmation dialogue is prompted upon market order is closed
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Me" on the app footer
      And the user toggles off trade confirmation on the app setting page
      And the user taps button "Markets" on the app footer
      And the user taps symbol on the app markets page
      And the user selects tab "Positions" on the app trade view
      And the total count of the positions is retrieved on the app trade view
      And the user taps back button on the app trade view
      And the user creates a "BUY" position on the instrument details page
      When the user taps "close" cta button on the app trade view
      And the user taps button "Close Position" on the instrument details page
      Then the user sees an open position is disappeared on the position tab of app trade view




