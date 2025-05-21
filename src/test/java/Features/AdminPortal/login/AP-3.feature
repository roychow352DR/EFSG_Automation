Feature: Admin Portal login

  Background:
    Given the user lands on Admin Portal login page

  @Regression @AdminPortal @Smoke
  Scenario Outline: Login with unauthorized account credentials - invalid username and password
    Given the user fills in with username <name> and password <password>
    When  the user clicks Sign In button
    Then the user sees "Invalid username or password." message pop up

    Examples:
      | name   | password   |
      | qaauto | Test1234@@ |
    