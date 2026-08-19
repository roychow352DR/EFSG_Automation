Feature: App Trade

  @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro @AppTest
  Scenario: Populate full volume upon All button is tapped on Close Position page
    Given the user launch the app
    And the user login as username "autol3" and password "Test1234@" on App login page
    And the user taps button "Markets" on the app footer
    And the user creates a "BUY" position on the instrument details page
    When the user taps "close" cta button on the app trade view
    And the user taps button "-" on the close position page
    And the user taps button "All" on the close position page
    Then the user sees a full volume lot size on the app close position page
