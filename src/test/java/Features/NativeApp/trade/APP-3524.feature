Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @AppTest
    Scenario: display all only as the filtering option for history empty state
      Given the user launch the app
      And the user login as username "autol3empty" and password "Test1234@" on App login page
      And the user taps button "Portfolio" on the app footer
      And the user selects tab "History" on the portfolio page
      When the user taps filtering button on the portfolio page
      Then the user sees option "All" under "Product" session on the filtering page




