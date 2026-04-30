Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: User can close position successfully
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Markets" on the app footer
      And the user taps symbol on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user fills in the text field "Lot Size" with value "0.5" on the instrument details page
      And the user taps button "BUY" on the instrument details page
      And the user taps button "BUY" on the confirmation pop up
      When the user taps "close" cta button on the app trade view
      And the user taps button "Close Position" on the instrument details page
      And the user taps button "Close Position" on the confirmation pop up
      Then the user sees the message "Your position is closed" is displayed at the dialogue




