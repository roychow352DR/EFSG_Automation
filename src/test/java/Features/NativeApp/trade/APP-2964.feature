Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: Prompted confirmation dialogue of cancel order after the cancel button is tapped on pending order tab
    Given the user launch the app
    And the user login as username "autol3" and password "Test1234@" on App login page
    And the user taps button "Markets" on the app footer
    And the user places a pending order with direction "BUY" and order type "Buy Stop" on the instrument details page
    And the user taps back button on the app trade view
    When the user taps button "Portfolio" on the app footer
    And the user selects tab "Pending Orders" on the portfolio page
    And the user taps "cancel" button of the record row on the portfolio page
    Then the user sees confirmation dialogue on the portfolio page