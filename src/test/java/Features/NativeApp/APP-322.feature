Feature: Native App login

    @App @Smoke @Regression @Login @EBL_MT5 @EIEHK @XPro
    Scenario: logout successfully using L2 account
      Given the user launch the app
      And the user lands on app home page
      And the user lands on the app login page
      When the user fills username "autol2" and password "Test1234@" on App login page
      And the user taps Login button on the app login page
      And the user taps button "Me" on the app footer
      And the user taps button "Logout" on the app me page
      And the user taps button "Yes" on the app me page
      Then the user sees button "Sign Up / Login" is displayed at the app home page


