Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: The volume input value is reduced by step size when the minus button is tapped
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user fills in the text field "Lot Size" with value "0.5" on the instrument details page
      When the user taps button "-" of the "Lot Size" input text field on the instrument details page
      Then the user sees the value "Lot Size" is decreased by the step size on the instrument details page


