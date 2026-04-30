Feature: Native App trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: User can edit pending order successfully
    Given the user launch the app
    And the user login as username "autol3" and password "Test1234@" on App login page
    And the user taps button "Markets" on the app footer
    And the user taps symbol on the app markets page
    And the user selects direction "BUY" on the app trade view
    And the user selects order type "Limit / Stop Order" on the instrument details page
    And the user fills in the text field "Lot Size" with value "0.5" on the instrument details page
    And the user selects stop limit order option "Buy Stop" on the instrument details page
    And the user fills in the text field "Price" with direction "BUY" on the instrument details page
    And the user taps button "BUY" on the instrument details page
    And the user taps button "BUY" on the confirmation pop up
    When the user taps "edit" cta button on the app trade view
    And the user edit price type "Stop" of the pending order on the instrument details page
    And the user taps button "Modify Order" on the instrument details page
    And the user taps button "Modify Order" on the confirmation pop up
    And the user taps "detail" cta button on the app trade view
    Then the user sees the value "Target Price" is updated on the pending order details page



