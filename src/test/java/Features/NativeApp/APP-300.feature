Feature: Native App login

    @App @Smoke @Regression @Login @EBL_MT5 @EIEHK @XPro
    Scenario: log in successfully using L3 account
      Given the user launch the app
      And the user lands on app home page
      And the user lands on the app login page
      When the user fills username "autol3" and password "Test1234@" on App login page
      And the user taps Login button on the app login page
      And the user taps button "Me" on the app footer
      Then the user sees trade account label on the app me page
