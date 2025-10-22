Feature: AO Application List

  @Regression @AdminPortal @AO @EBL_MT5 @EIEHK @XPro
  Scenario: The Application status is in Draft after the submission quit
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the user clicks "Create Account" button on the application page
    And the user selects "Individual" radio button on the create account pop up
    And the user clicks "submit" button on the create account pop up
    And the user fills application information page
    When the user clicks "AO Application List" on the ao admin portal menu
    Then the user sees a record in "Draft" status is created on the application list