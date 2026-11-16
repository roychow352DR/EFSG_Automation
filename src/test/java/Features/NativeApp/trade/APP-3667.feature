Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @AppTest
    Scenario: The filtering options under action are displayed correctly
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Portfolio" on the app footer
      And the user selects tab "History" on the portfolio page
      When the user taps filtering button on the portfolio page
      Then the user sees option "All" under "Action" session on the filtering page
      And the user sees option "Open" under "Action" session on the filtering page
      And the user sees option "Close" under "Action" session on the filtering page




