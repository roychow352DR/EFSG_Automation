Feature: AO Username

  @Regression @AdminPortal @AO @Username @EBL_MT5
  Scenario: Cannot create individual account if username is duplicated in case insensitive
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user selects entity "EBL_MT5" on the application information page
    And the user fills mandatory information on application information page
    And the user fills value "qaautol3" in the text field "username" on application information page
    When the user clicks "Next to Personal Information" button on the application information page
    Then the user sees an error dialogue with wordings "The username is already in use" on the application information page