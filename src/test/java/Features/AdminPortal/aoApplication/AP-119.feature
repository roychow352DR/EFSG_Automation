Feature: AO Application List

  @Regression @AdminPortal @AO @EBL_MT5 @EIEHK
  Scenario: User sees Active Live Trading Account button in the Details page of Pending Deposit record
    Given the user logged in to Admin Portal as username "qaauto" and password "Test1234@"
    When the user clicks detail button of "Pending Deposit" record on the application page
    Then the user sees "Activate live trading account" button on application information page