Feature: Admin Portal login


  @Regression @AdminPortal @Smoke
  Scenario: Login with unauthorized account credentials - valid username but incorrect password
    Given the user lands on Admin Portal login page
    And the user fills in with username "qaauto" and password "Test1234@@"
    When the user clicks Sign In button
    Then the user sees "Invalid username or password." message pop up
