Feature: Customer Management

  @Regression @AdminPortal @CM @Backend
  Scenario: status equals to 2 in cm.trade database table for Pending Deposit account
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    And the user sees a record in "Pending Deposit" status on the application list
    Then "status" is "2" in CM "trade" database table where "account_id" retrieved by "Account ID"