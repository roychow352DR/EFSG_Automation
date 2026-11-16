Feature: Native App trade

    @App @Smoke @Regression @Trade @EIEHK
    Scenario: User can search symbol on the watchlist page
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      When the user taps the search button on the markets page
      And the user fills in the search field with value "XAGUSD" on the markets page
      Then the user sees the symbol "XAGUSD" in the markets search results


