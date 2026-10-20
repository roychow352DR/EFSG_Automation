Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @AppTest
    Scenario: User sees action on the position details page of open position history
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      And the user creates a "BUY" position on the instrument details page
      And the user taps back button on the app trade view
      And the user taps button "Portfolio" on the app footer
      And the user selects tab "History" on the portfolio page
      When the user taps "arrow" button of the record row on the portfolio page
      Then the user sees correct value "Action" on the position details page




