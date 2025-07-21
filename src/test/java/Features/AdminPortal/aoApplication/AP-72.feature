Feature: AO Application List

  @Regression @AdminPortal
  Scenario: Prompt error if DOB is below 18
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    And the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills application information page
    When the user submits mandatory information with "DOB below 18" on personal information page
    Then the user sees "Must be 18 years old above" error message displayed on personal information page
