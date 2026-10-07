Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: The lot value can be adjust by tapping minus button on the instrument details page
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user fills in the text field "Lot Size" with value "0.5" on the instrument details page
      And the user taps button "-" of the "Lots" input text field on the instrument details page
      Then the user sees the lots value is decreased by default step size on the instrument details page



