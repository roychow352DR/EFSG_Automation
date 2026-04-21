Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: It prompted error message when the inputted lot size of the market order is less than minimum lot
      Given the user launch the app
      And the user login as username "autol3" and password "Test1234@" on App login page
      And the user taps button "Markets" on the app footer
      When the user taps symbol on the app markets page
      And the user selects direction "BUY" on the app trade view
      And the user fills in the text field "Lot Size" with the value less than minimum on the instrument details page
      Then the user sees an error message "Invalid lot size" is displayed on the instrument details page


