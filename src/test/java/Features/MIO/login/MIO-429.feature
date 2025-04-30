Feature: MIO Admin Portal login

  Background:
    Given the user lands on MIO Admin Portal login page

    @Smoke @MIO @Regression
    Scenario Outline: Login end to end flow
      Given the user fills username <name> and password <password> on MIO Admin Portal login page
      When  the user clicks Sign In button on MIO Admin Portal login page
      Then  the user sees "Welcome Back admin!" display on the MIO Admin Portal landing screen

      Examples:
        | name   | password  |
        | admin | 123456Aa!! |