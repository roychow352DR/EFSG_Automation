Feature: Admin Portal login

  Background:
    Given the user lands on Admin Portal login page

   @Smoke
  Scenario Outline: Login with unauthorized account credentials - invalid username and password
    When the user fills in with username <name> and password <password>
    And  the user clicks Sign In button
    Then  the user sees "Invalid username or password." message pop up

    Examples:
      | name       | password |
      | roychowsitt | Test1234@@|