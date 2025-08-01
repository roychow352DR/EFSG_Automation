Feature: AO Application List


  @Regression @AdminPortal @AO @L3Creation
  Scenario: Pending Deposit record is inserted to the CM database upon L3 account creation
    Given the user logged in to Admin Portal as username "aoadmin02" and password "P@ssw0rd!"
    When the user sees an existing record is updated to "Pending Deposit" status on the application list
    Then the "Pending Deposit" account record is retrieved in CM database