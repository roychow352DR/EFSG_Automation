Feature: AO Username

  @Regression @AdminPortal @AO @Username @EBL
  Scenario: Prompted error text for invalid username inputs - Full-width character
    Given the user logged in to Admin Portal as username "samuellai" and password "Aa12345678!"
    And the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills mandatory information on application information page
    And the user fills full width username with digits number 5 on application information page
    And the user clicks "Next to Personal Information" button on the application information page
    Then the user sees "Invalid Username" error message displayed on application information page