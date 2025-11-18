Feature: AO Application List


  @Regression @AdminPortal @AO @EBL_MT5 @EIEHK @L3IndividualCreation
  Scenario: Pending Deposit record is inserted to the CM database upon L3 account creation
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    When the user sees an existing record is updated to "Pending Deposit" status on the application list
    Then the "Pending Deposit" account record is retrieved in CM database