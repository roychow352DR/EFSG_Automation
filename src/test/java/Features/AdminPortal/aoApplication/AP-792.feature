Feature: AO Username

  @Regression @AdminPortal @AO @Username @EBL_MT5 @EGM
  Scenario: Company - Cannot create account if username already in used in L3 account
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Create Account" button on the application page
    And the user selects "Company" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fill mandatory information on create company account page
    And the user fills value "QaAutoL3" in the text field "username" on create company account page
    When the user clicks "Submit" button on the create company account page
    And the user clicks "Confirm" button on the create company account page
    Then the user sees an error dialogue with wordings "The username is already in use" on the create company account page