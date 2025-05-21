Feature: Admin Portal login

  Background:
    Given the user lands on Admin Portal login page

  @Smoke @AdminPortal @Test
  Scenario Outline: Login End to End flow
    Given the user fills in with username <name> and password <password>
    When  the user clicks Sign In button
    Then  the user sees Menu display on the screen

    Examples:
      | name   | password  |
      | qaauto | Test1234@ |