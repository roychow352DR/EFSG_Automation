Feature: AO Application List

  @Regression @AdminPortal @AO
  Scenario: The Application status is in Draft after the submission quit
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    And the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills application information page
    When the user clicks "AO Application List" on the menu
    Then the user sees a record in "Draft" status is created on the application list