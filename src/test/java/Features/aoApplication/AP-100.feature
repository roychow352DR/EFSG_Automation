Feature: AO Application List

  Background:
    Given the user logged in to Admin Portal

  @Smoke
  Scenario: Create Individual Account with status in Pending Verification
    Given the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills application information page
    And the user fills personal information page
    And the user fills contact information page
    And the user fills employee & financial information page
    And the user fills trading experience page
    When the user clicks "submit" button on the trading experience page
    Then the user sees a record in "Pending Verification" status is created on the application list



