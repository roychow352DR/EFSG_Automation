Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest
    Scenario: User sees the action of open position is close on the portfolio history list
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      And the user creates a "BUY" position on the instrument details page
      When the user taps "close" cta button on the app trade view
      And the user taps button "Close Position" on the instrument details page
      And the user taps button "Close Position" on the confirmation pop up
      And the user taps back button on the app trade view
      And the user taps button "Portfolio" on the app footer
      And the user selects tab "History" on the portfolio page
      Then the user sees transaction record item "Action" is "Close" on the portfolio history list




