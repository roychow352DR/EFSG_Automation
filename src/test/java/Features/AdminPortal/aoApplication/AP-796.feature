Feature: AO Username

  @Regression @AdminPortal @AO @Username @EBL_MT5 @EGM
  Scenario: Individual - Username field leave empty will pop up validation error
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills mandatory information on application information page
    And the user empties the text field "username" on application information page
    When the user clicks "Next to Personal Information" button on the application information page
    Then the user sees "Username is required" error message displayed on application information page