Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @AppTest
    Scenario: The filtering option can be reset to default
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Portfolio" on the app footer
      And the user selects tab "History" on the portfolio page
      When the user taps filtering button on the portfolio page
      And the user taps option "XAUUSD" under "Product" session on the filtering page
      And the user taps option "Open" under "Action" session on the filtering page
      And the user taps button "Default" on the filtering page
      Then the user sees option "All" is selected under "Product" session on the filtering page
      And the user sees option "All" is selected under "Action" session on the filtering page




