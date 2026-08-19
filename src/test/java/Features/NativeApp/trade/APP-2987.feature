Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest
    Scenario: The don't show again checkbox is not checked by default after the disclaimer prompt is closed
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user taps button "BUY" on the instrument details page
      And the user taps button "Don't Show Again" on the confirmation pop up
      And the user taps button "Cross" on the confirmation pop up
      Then the user sees "Don't Show Again" checkbox is unchecked on the confirmation pop up




