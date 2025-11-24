Feature: AO Username

  @Regression @AdminPortal @AO @Username @EBL_MT5
  Scenario: Company - Username field leave empty will pop-up validation error
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Create Account" button on the application page
    And the user selects "Company" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fill mandatory information on create company account page
    And the user empties the text field "username" on create company account page
    When the user clicks "Submit" button on the create company account page
    Then the user sees "Username is required" error message displayed on company account detail page