Feature: AO Application List


  @Regression @AdminPortal @AO
  Scenario: Prompt error if email already in used
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    And the "Exist Email" condition is satisfied
    When the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user submits mandatory information on application information page
    Then the user sees "This email address is already in use" error message displayed on application information page
