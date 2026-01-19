Feature: Native App AO Application

    @App @Smoke @Regression @AO @EBL_MT5
    Scenario: Username is display at Me Page
      Given the user launch the app
      And the user lands on app home page
      And the user lands on the app login page
      When the user fills username "autol2" and password "Test1234@" on App login page
      And the user taps Login button on the app login page
      And the user taps button "Me" on the app footer
      Then the user sees username "autol2" is display at the app me page


