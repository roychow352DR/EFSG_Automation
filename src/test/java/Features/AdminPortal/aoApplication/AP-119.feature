Feature: AO Application List

  @Regression @AdminPortal @AO
  Scenario: User sees "Active Live Trading Account" button in the Details page of Pending Deposit record
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    When the user clicks detail button of "Pending Deposit" record on the application page
    Then the user sees "Activate live trading account" button on application information page