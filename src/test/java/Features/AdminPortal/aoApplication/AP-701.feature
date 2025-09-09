Feature: AO Username

  @Regression @AdminPortal @AO @Username @EBL
  Scenario: Username is not editable in Pending Deposit status record
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    When the user clicks detail button of "Pending Deposit" record on the application page
    Then the text field "username" is not editable