Feature: Customer Management

  @Regression @AdminPortal @CM @Backend
  Scenario: status equals to 1 in cm.trade database table for Approved account
    Given the user logged in to Admin Portal as username "aoadmin01" and password "P@ssw0rd!"
    And the user sees a record in "Approved" status on the application list
    Then "status" is "1" in CM "trade" database table where "account_id" retrieved by "Account ID"