Feature: Native App trade

    @App @Smoke @Regression @Trade @EBL_MT5 @EIEHK @XPro
    Scenario: The initial margin of XAGUSD is displayed correctly on the position details page if default initial margin equals to 0
      Given the user launch the app
      And the user login as username "autol3qa" and password "Test1234@" on App login page
      And the initial margin is set to zero
      And the user taps button "Markets" on the app footer
      And the user creates a "BUY" position of symbol "XAGUSD" on the instrument details page
      When the user taps "detail" cta button on the app trade view
      Then the user sees correct value "Initial Margin" on the position details page




