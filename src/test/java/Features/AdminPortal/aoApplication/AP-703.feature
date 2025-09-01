Feature: AO Username

  @Regression @AdminPortal @AO @Username @EBL
  Scenario: Username text field is not editable in Approved status
    Given the user logged in to Admin Portal as username "samuellai" and password "Aa12345678!"
    When the user clicks detail button of "Approved" record on the application page
    Then the text field "username" is not editable