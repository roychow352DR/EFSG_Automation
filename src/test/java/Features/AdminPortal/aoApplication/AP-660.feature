Feature: AO Application List

  @Regression @AdminPortal @Smoke @AO @Username @EBL_MT5 @EGM
  Scenario: Create Individual Account with status in Pending Verification with valid username inputs - Alphanumeric characters
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    And the record in status "Pending Verification" is created in the application list
    Then the user sees a record in "Pending Verification" status is created on the application list
