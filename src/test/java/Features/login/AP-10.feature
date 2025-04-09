Feature: Admin Portal login

  Background:
    Given the user lands on Admin Portal login page

  @Regression
  Scenario Outline: Login as suspended account
    Given the user fills in with username <name> and password <password>
    When the user clicks Sign In button
    Then the user sees "User account is suspended! Please contact administration" message pop up

    Examples:
      | name          | password  |
      | qaautosuspend | Test1234@ |