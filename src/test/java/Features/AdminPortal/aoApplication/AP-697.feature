Feature: AO Username

  @Regression @AdminPortal @AO @Username @EBL
  Scenario: Username text field is editable in Draft status
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    When the user clicks detail button of "Draft" record on the application page
    Then the text field "username" is editable