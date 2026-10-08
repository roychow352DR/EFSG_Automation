Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: The take profit price is populate to the input field after plus button is tapped
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user taps take profit and stop loss toggle on the instrument details page
      And the user taps button "+" of the "Take Profit" input text field on the instrument details page
      Then the user sees the "Take Profit" price is populate to the input field on the instrument details page


