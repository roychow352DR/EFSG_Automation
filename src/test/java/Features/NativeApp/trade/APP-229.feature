Feature: Native App trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest
  Scenario: User can place buy stop pending order successfully
    Given the user launch the app
    And the user login as username "autol3" and password "Test1234@" on App login page
    And the user taps button "Markets" on the app footer
    When the user taps symbol on the app markets page
    And the user selects tab "Positions" on the app trade view
    And the user selects list "Pending Orders" on the app trade view
    And the total count of the pending order is retrieved on the app trade view
    And the user taps back button on the app trade view
    And the user taps symbol on the app markets page
    And the user selects direction "BUY" on the app trade view
    And the user selects order type "Limit / Stop Order" on the instrument details page
    And the user fills in the text field "Lot Size" with value "0.5" on the instrument details page
    And the user selects stop limit order option "Buy Stop" on the instrument details page
    And the user fills in the text field "Price" with direction "BUY" on the instrument details page
    And the user taps button "BUY" on the instrument details page
    And the user taps button "BUY" on the confirmation pop up
    Then the user sees a new pending order is displayed at the pending order tab of app trade view




