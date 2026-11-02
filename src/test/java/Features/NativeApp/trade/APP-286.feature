Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest
    Scenario: User can redirect to the close position page from watchlist details page
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      And the user creates a "BUY" position on the instrument details page
      And the user taps back button on the app trade view
      When the user taps "close" cta button of open position on the watchlist details page
      Then the user sees header "Close Position" on the page




