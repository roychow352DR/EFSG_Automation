Feature: AO Username

  @Regression @AdminPortal @AO @Username @EBL
  Scenario: Username text field is not editable in Pending Verification status
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    When the user clicks detail button of "Pending Verification" record on the application page
    Then the text field "username" is not editable