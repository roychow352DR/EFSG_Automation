Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @AppTest
    Scenario: User can filter out the pending order by using product on the pending order tab
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      And the user places a pending order with direction "BUY" and order type "Buy Limit" on the instrument details page
      And the user taps back button on the app trade view
      When the user taps button "Portfolio" on the app footer
      And the user selects tab "Pending Orders" on the portfolio page
      And the user taps filtering button on the portfolio pending order tab
      And the user taps option "XAUUSD" under "Product" session on the filtering page
      Then the user sees the transaction history matched with the selected option on the portfolio pending order tab




