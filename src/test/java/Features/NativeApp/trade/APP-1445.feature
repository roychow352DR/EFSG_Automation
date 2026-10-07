Feature: Native App trade

    @App @Smoke @Regression @Trade @EIEHK
    Scenario: It prompted error message when the inputted lot size of the limit order is more than maximum lot
      Given the user launch the app
      And the user logs in on the App login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user toggles on pending order on the instrument details page
      And the user fills in the text field "Lot Size" with the value more than maximum on the instrument details page
      Then the user sees an error message "Invalid lot size" is displayed on the instrument details page