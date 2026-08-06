Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: User sees the position record is disappeared on the open position page after position closed
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Markets" on the app footer
      And the user creates a "BUY" position on the instrument details page
      When the user taps "close" cta button on the app trade view
      And the user taps button "Close Position" on the instrument details page
      And the user taps button "Close Position" on the confirmation pop up
      Then the user sees the message "Your position is closed" is displayed at the dialogue




