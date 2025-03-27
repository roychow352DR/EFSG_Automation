Feature: Admin Portal login

  Background:
    Given the user lands on Admin Portal login page

   @Smoke
  Scenario Outline: Login with empty value
    Given the user input nothing as username and password
    Then the user sees the Sign In button is unclickable

    Examples:
      | name       | password |
      | roychowsitt | Test1234@@|