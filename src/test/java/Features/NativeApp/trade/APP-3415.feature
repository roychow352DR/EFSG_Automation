Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: The product code is displayed correctly on the position details
    Given the user launch the app
    And the user logs in on the App login page
    And the user taps button "Markets" on the app footer
    And the user creates a "BUY" position on the instrument details page
    When the user taps "detail" cta button on the app trade view
    Then the user sees correct value "Product" on the position details page