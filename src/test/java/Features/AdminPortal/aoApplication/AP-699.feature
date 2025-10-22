Feature: AO Username

  @Regression @AdminPortal @AO @Username @EBL_MT5
  Scenario: Username text field is not editable in Awaiting Response status
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    When the user clicks detail button of "Awaiting Response" record on the application page
    Then the user sees text field "username" is not editable on the application information page