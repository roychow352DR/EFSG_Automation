Feature: AO Username

  @Regression @AdminPortal @AO @Username @EBL_MT5 @EGM
  Scenario: Create Individual Account with status in Pending Verification with valid username inputs - Length with maximum 30 Characters
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills mandatory information on application information page
    And the user fills username with digits number 30 on application information page
    And the user clicks "Next to Personal Information" button on the application information page
    And the user submit ao application from personal information page
    Then the user sees a record in "Pending Verification" status is created on the application list