Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
  Scenario: Display a full volume lot size in the close position page
    Given the user launch the app
    And the user logs in on the App login page
    And the user taps button "Markets" on the app footer
    And the user creates a "BUY" position on the instrument details page
    When the user taps "close" cta button on the app trade view
    Then the user sees a full volume lot size on the app close position page
