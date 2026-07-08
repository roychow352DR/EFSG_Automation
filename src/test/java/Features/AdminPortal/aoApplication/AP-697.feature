Feature: AO Username

  @Regression @AdminPortal @AO @Username @EBL_MT5 @EGM
  Scenario: Username text field is editable in Draft status
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the record in status "Draft" is created in the application list
    When the user clicks detail button of "Draft" record on the application page
    Then the user sees text field "username" is editable on the application information page