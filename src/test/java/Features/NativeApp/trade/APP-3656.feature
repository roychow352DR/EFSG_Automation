Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @AppTest
    Scenario: The default history filtering option of product is all
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Portfolio" on the app footer
      And the user selects tab "History" on the portfolio page
      When the user taps filtering button on the portfolio page
      Then the user sees option "All" is selected under "Product" session on the filtering page




