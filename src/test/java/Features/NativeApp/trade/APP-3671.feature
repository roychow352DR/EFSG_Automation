Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @AppTest
    Scenario: User can filter out the transaction history by using action
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Portfolio" on the app footer
      And the user selects tab "History" on the portfolio page
      When the user taps filtering button on the portfolio page
      And the user taps option "Open" under "Action" session on the filtering page
      And the user taps button "Done" on the filtering page
      Then the user sees the transaction history matched with the selected option on the portfolio page




