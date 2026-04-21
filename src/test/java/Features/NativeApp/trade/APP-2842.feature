Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: The trading volume and estimated margin are displayed correctly after the "1 lot" button is tapped
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user fills in the text field "Lot Size" with value "1" on the instrument details page
      Then the user sees the "Est. Margin" value is displayed correctly on the instrument details page
      And the user sees the "Lots" value is displayed correctly on the instrument details page


