Feature: MIO Admin Portal login

    @Smoke @Regression @MIO @EBL_MT5
    Scenario: Login end to end flow
      Given the user lands on MIO Admin Portal login page
      And the user fills username "admin" and password "123456Aa!" on MIO Admin Portal login page
      And the user clicks Sign In button on MIO Admin Portal login page
      When the user clicks dropdown "Type" on MIO Admin Portal deposit management page
      Then the user sees "admin" is displayed as profile name
