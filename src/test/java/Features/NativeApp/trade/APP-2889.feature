Feature: Native App AO Application

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: The take profit price decreased by 1 point after minus button is tapped
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user taps take profit and stop loss toggle on the instrument details page
      And the user fills in the text field "Take Profit" with direction "BUY" on the instrument details page
      And the user taps button "-" of the "Take Profit" input text field on the instrument details page
      Then the user sees the "Take Profit" price is decreased by 1 point on the instrument details page


